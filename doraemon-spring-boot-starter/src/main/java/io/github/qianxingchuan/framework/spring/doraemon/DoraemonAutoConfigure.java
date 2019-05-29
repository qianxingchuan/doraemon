package io.github.qianxingchuan.framework.spring.doraemon;

import io.github.qianxingchuan.framework.doraemon.main.RunDoraemonBundle;
import io.github.qianxingchuan.framework.spring.doraemon.config.DoraemonConfig;
import io.github.qianxingchuan.framework.spring.doraemon.service.DoraemonBundleContainer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author xingchuan.qxc
 * @version 2019/5/30 20:57
 */
@Configuration
@ConditionalOnClass(DoraemonBundleContainer.class)
@EnableConfigurationProperties(DoraemonConfig.class)
public class DoraemonAutoConfigure {

    private Logger logger = LoggerFactory.getLogger(DoraemonAutoConfigure.class);

    @Autowired
    private DoraemonConfig doraemonConfig;

    @Bean
    @ConditionalOnMissingBean
    public DoraemonBundleContainer buildBundleContainer() {
        DoraemonBundleContainer bundleContainer = new DoraemonBundleContainer();
        String bundlePath = doraemonConfig.getBundlePath();
        String extractPath = doraemonConfig.getExtractPath();
        logger.info("load doraemon config bundlePath {},extractpath {}", bundlePath, extractPath);
        if (StringUtils.isBlank(bundlePath)) {
            return bundleContainer;
        }
        File bundlePathDir = new File(bundlePath);
        if (!bundlePathDir.exists() || !bundlePathDir.isDirectory()) {
            return bundleContainer;
        }
        File[] bundleZipFiles = bundlePathDir.listFiles(f -> StringUtils.endsWith(f.getName(), ".zip"));
        if (bundleZipFiles == null) {
            return bundleContainer;
        }
        for (File bundleZipFile : bundleZipFiles) {
            RunDoraemonBundle runDoraemonBundle = new RunDoraemonBundle(bundleZipFile.getAbsolutePath(), extractPath);
            runDoraemonBundle.run();
            bundleContainer.addDoraemonBundle(runDoraemonBundle);
        }
        return bundleContainer;
    }
}
