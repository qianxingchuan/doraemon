package io.github.xingchuan.framework.doraemon.bundle;

import io.github.xingchuan.framework.doraemon.BundleService;
import io.github.xingchuan.framework.doraemon.main.RunDoraemonBundle;
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
    public void test_INIT_bundle() throws Exception {
        String bundleFileName = "/sample-auth-bundle1-2048-SNAPSHOT-release.zip";
        URL url = AsgardBundleTest.class.getResource(bundleFileName);
        String moduleName = StringUtils.substringBeforeLast("sample-auth-bundle1-2048-SNAPSHOT-release.zip", ".zip");
        String bundleExtractPath = BUNDLE_TEST_PATH + moduleName + "/";
        AsgardBundle asgardBundle = initBundle(url.getFile()).getBundle();
        assertThat(asgardBundle.getBundleName()).isEqualTo("sample-auth-bundle1-2048-SNAPSHOT-release");

        File extractPath = new File(bundleExtractPath);
        assertThat(extractPath.exists()).isTrue();
        File bundleClassPath = new File(bundleExtractPath + "BUNDLE-CLASS");
        assertThat(bundleClassPath.exists() && bundleClassPath.isDirectory()).isTrue();
        File bundleJarPath = new File(bundleExtractPath + "lib");
        assertThat(bundleJarPath.exists() && bundleJarPath.isDirectory()).isTrue();
        File metaInfPath = new File(bundleExtractPath + "META-INF");
        assertThat(metaInfPath.exists() && metaInfPath.isDirectory()).isTrue();
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