package io.github.xingchuan.framework.doraemon.bundle;

import io.github.xingchuan.framework.doraemon.BundleService;
import io.github.xingchuan.framework.doraemon.exception.AsgardBundleError;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * load extra .zip file as bundle.
 * bundle should be init when container start.
 * <p>
 * Bundle must be zip format file.
 * The directory tree in zip file like this :
 * <p>
 * META-INF/config.properties
 * BUNDLE-CLASS/
 * lib/
 *
 * @author xingchuan.qxc
 * @date 2019/5/14 16:54
 */
public class AsgardBundle {

    public static final String PARAM_ERROR_NAME = "errorName";
    private Logger logger = LoggerFactory.getLogger(AsgardBundle.class);
    /**
     * the class should be exported in the bundle.
     * key : class full name.
     * value : class
     */
    private ConcurrentHashMap<String, Class<?>> shareClassMap = new ConcurrentHashMap<>();

    private AsgardClassLoader bundleClassLoader;

    /**
     * bundle config
     */
    private BundleConfiguration bundleConfiguration;

    /**
     * parent class loader
     */
    private ClassLoader parentClassLoader;

    /**
     * flag for bundle running
     */
    private volatile boolean bundleRunning = false;

    /**
     * bundle name
     */
    private String bundleName;

    /**
     * build bundleService
     */
    private BundleService bundleService;

    /**
     * create bundle.
     *
     * @param bundleConfiguration bundle config.
     * @param parentClassLoader   parent classloader
     */
    public AsgardBundle(BundleConfiguration bundleConfiguration, ClassLoader parentClassLoader) {
        this.bundleConfiguration = bundleConfiguration;
        this.parentClassLoader = parentClassLoader;
        this.bundleName = FilenameUtils.getBaseName(bundleConfiguration.getBundleZipFilePath());
    }

    /**
     * bundle start.
     */
    public synchronized void init() {
        if (bundleRunning) {
            return;
        }
        bundleRunning = true;
        String bundleZipFilePath = bundleConfiguration.getBundleZipFilePath();
        //unpack zip
        File bundleFile = new File(bundleZipFilePath);
        if (!bundleFile.exists()) {
            AsgardBundleError.BUNDLE_FILE_NOT_EXIST.custom().withParam("bundleName", bundleZipFilePath).format().throwException();
        }
        String extractPath = bundleConfiguration.getBundleExtractPath();
        unpackBundleZip(bundleFile);

        // init bundle classloader
        initBundleClassLoader();

        // load configure properties
        loadConfigure(extractPath);
        // load export classes
        try {
            loadBundleExportClasses(bundleClassLoader);
        } catch (ClassNotFoundException e) {
            logger.error("load bundle {} export classes error.", bundleZipFilePath, e);
            AsgardBundleError.BUNDLE_COMMON_ERROR.custom().withParam(PARAM_ERROR_NAME, "load bundle classes [class not found].").format().throwException();
        }
        // init instance & invoke method "doIt"
        bundleCustomRun();
    }

    /**
     * find the class exported from this bundle
     *
     * @param classFullName the fullname should be found.
     * @return
     */
    public Class<?> getSharedClass(String classFullName) {
        return shareClassMap.get(classFullName);
    }

    /**
     * get bundle name
     *
     * @return
     */
    public String getBundleName() {
        return bundleName;
    }

    /**
     * get bundle service
     *
     * @return
     */
    public BundleService getBundleService() {
        return bundleService;
    }

    public AsgardClassLoader getBundleClassLoader() {
        return bundleClassLoader;
    }

    /**
     * init bundle classLoader
     */
    private void initBundleClassLoader() {
        String extractPath = bundleConfiguration.getBundleExtractPath();

        URL[] classPathUrls = buildClassPathUrls(extractPath);
        // init bundle classloader
        bundleClassLoader = new AsgardClassLoader(classPathUrls, parentClassLoader, bundleConfiguration);
    }

    /**
     * build class path urls
     *
     * @param extractPath bundle extract path
     * @return
     */
    private URL[] buildClassPathUrls(String extractPath) {
        URL bundleClassPathURL = null;
        try {
            bundleClassPathURL = new URL("file:" + extractPath + "BUNDLE-CLASS/");
        } catch (MalformedURLException e) {
            logger.error("parse bundle class path {} to  url  error .", "file:" + extractPath + "BUNDLE-CLASS/", e);
            throw AsgardBundleError.BUNDLE_COMMON_ERROR.custom().withParam(PARAM_ERROR_NAME, "parse bundle class path to url error.").format().getAsgardException();
        }
        File jarPath = new File(extractPath + "lib/");
        List<URL> bundleJarPathURLList = Arrays.stream(jarPath.listFiles(file -> StringUtils.endsWith(file.getName(), ".jar"))).map(file -> {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                logger.error("parse file {} to  url  error .", file.getName(), e);
            }
            throw AsgardBundleError.BUNDLE_COMMON_ERROR.custom().withParam(PARAM_ERROR_NAME, "jar parse url.").format().getAsgardException();
        }).collect(Collectors.toList());
        bundleJarPathURLList.add(bundleClassPathURL);
        URL[] classPathUrls = new URL[bundleJarPathURLList.size()];
        bundleJarPathURLList.toArray(classPathUrls);
        return classPathUrls;
    }

    /**
     * init instance & invoke the custom method
     */
    private void bundleCustomRun() {
        String initInstanceClass = bundleConfiguration.getInitInstanceClass();
        if (StringUtils.isNotBlank(initInstanceClass)) {
            ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(bundleClassLoader);
                Class<BundleService> clazz = (Class<BundleService>) bundleClassLoader.loadClass(initInstanceClass);
                this.bundleService = clazz.newInstance();
                Method method = clazz.getMethod("doIt");
                method.invoke(bundleService);
            } catch (Exception e) {
                logger.error("asgard bundle custom run error.", e);
                AsgardBundleError.BUNDLE_COMMON_ERROR.custom().withParam(PARAM_ERROR_NAME, "init bundle instance.").format().throwException();
            } finally {
                Thread.currentThread().setContextClassLoader(oldContextClassLoader);
            }
        }
    }

    /**
     * load bundle configuration
     *
     * @param extractPath
     */
    private void loadConfigure(String extractPath) {
        try (InputStream input = new FileInputStream(extractPath + "/META-INF/bundle.properties")) {
            Properties prop = new Properties();
            // load a properties
            prop.load(input);
            String classesToExport = prop.getProperty("export-class");
            if (StringUtils.isNotBlank(classesToExport)) {
                Set<String> classesExportConfig = Arrays.stream(StringUtils.split(classesToExport, ",")).collect(Collectors.toSet());
                bundleConfiguration.addExportClassFullName(classesExportConfig);
            }
            // find the class and invoke initialize method.
            String initInstanceClass = prop.getProperty("init-class");
            if (StringUtils.isNotBlank(initInstanceClass)) {
                bundleConfiguration.setInitInstanceClass(initInstanceClass);
            }

            String skipClassSet = prop.getProperty("skip-class");
            if (StringUtils.isNotBlank(skipClassSet)) {
                String[] skipClassArray = StringUtils.split(skipClassSet, ",");
                bundleConfiguration.addSkipClassFullName(Arrays.stream(skipClassArray).collect(Collectors.toSet()));
            }
        } catch (IOException ex) {
            logger.error("load configure {} error.", extractPath, ex);
        }
    }

    /**
     * load bundle classes when container start.
     */
    private void loadBundleExportClasses(AsgardClassLoader bundleClassLoader) throws ClassNotFoundException {
        // load all class in bundle
        Set<String> exportClasses = bundleConfiguration.getExportClassList();
        for (String exportClass : exportClasses) {
            shareClassMap.put(exportClass, bundleClassLoader.loadClass(exportClass));
        }
    }


    /**
     * unpack bundle zip
     *
     * @param bundleFile
     */
    private void unpackBundleZip(File bundleFile) {
        // read zip bundle & unpack
        String bundleExtractPath = bundleConfiguration.getBundleExtractPath();
        File extractPath = new File(bundleExtractPath);
        ZipUtil.unpack(bundleFile, extractPath);
    }
}
