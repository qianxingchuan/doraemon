package com.xingchuan.framework.doraemon.bundle;

import com.vivo.cloud.test.XingChuanUtils;
import com.xingchuan.framework.doraemon.BundleService;
import com.xingchuan.framework.doraemon.main.RunDoraemonBundle;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * @author xingchuan.qxc
 * @date 2019/5/17 16:15
 */
public class AsgardBundleTest {

    public static final String BUNDLE_TEST_PATH = "~/doraemon-bundle/";

    @Test
    public void test初始化bundle() throws Exception {
        assertThat(XingChuanUtils.getValidString()).isEqualTo("This is a test.[in test class path]");
        String bundleFileName = "/zip-test.zip";
        URL url = AsgardBundleTest.class.getResource(bundleFileName);
        String moduleName = StringUtils.substringBeforeLast("zip-test.zip", ".zip");
        String bundleExtractPath = BUNDLE_TEST_PATH + moduleName + "/";
        AsgardBundle asgardBundle = initBundle(url.getFile()).getBundle();
        assertThat(asgardBundle.getBundleName()).isEqualTo("zip-test");

        File extractPath = new File(bundleExtractPath);
        assertThat(extractPath.exists()).isTrue();
        File bundleClassPath = new File(bundleExtractPath + "BUNDLE-CLASS");
        assertThat(bundleClassPath.exists() && bundleClassPath.isDirectory()).isTrue();
        File bundleJarPath = new File(bundleExtractPath + "lib");
        assertThat(bundleJarPath.exists() && bundleJarPath.isDirectory()).isTrue();
        File metaInfPath = new File(bundleExtractPath + "META-INF");
        assertThat(metaInfPath.exists() && metaInfPath.isDirectory()).isTrue();

        Class<?> clazz = asgardBundle.getSharedClass("com.vivo.cloud.auth.impl.AuthFacadeImpl");
        assertThat(clazz).isNotNull();
        Object target = clazz.newInstance();
        Method method = clazz.getMethod("auth", String.class);
        Object rs = method.invoke(target, "bundle-test-xingchuan");
        assertThat("jar.1.auth.result").isEqualTo(rs);
    }

    @Test
    public void testInitBundleWithSpringDependencies() {
        String bundleFileName = "/spring-module-test.zip";
        URL url = AsgardBundleTest.class.getResource(bundleFileName);
        RunDoraemonBundle runDoraemonBundle = initBundle(url.getFile());
        BundleService testInstance = runDoraemonBundle.getBundleBean(BundleService.class);
        assertThat(testInstance).isNotNull();
        testInstance.doIt();
    }


    /**
     * init bundle
     *
     * @param bundleFileName
     * @return
     */
    private RunDoraemonBundle initBundle(String bundleFileName) {
        RunDoraemonBundle runDoraemonBundle = new RunDoraemonBundle(bundleFileName, null);
        runDoraemonBundle.run();
        return runDoraemonBundle;
    }
}