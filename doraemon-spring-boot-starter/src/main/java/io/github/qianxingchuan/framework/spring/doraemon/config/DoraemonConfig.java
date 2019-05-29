package io.github.qianxingchuan.framework.spring.doraemon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xingchuan.qxc
 * @version 2019/5/30 20:28
 */
@ConfigurationProperties("doraemon.config")
public class DoraemonConfig {

    private String bundlePath;

    private String extractPath;

    public String getBundlePath() {
        return bundlePath;
    }

    public void setBundlePath(String bundlePath) {
        this.bundlePath = bundlePath;
    }

    public String getExtractPath() {
        return extractPath;
    }

    public void setExtractPath(String extractPath) {
        this.extractPath = extractPath;
    }
}
