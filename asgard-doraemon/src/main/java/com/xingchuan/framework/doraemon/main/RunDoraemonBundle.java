package com.xingchuan.framework.doraemon.main;

import com.xingchuan.framework.doraemon.BundleService;
import com.xingchuan.framework.doraemon.bundle.AsgardBundle;
import com.xingchuan.framework.doraemon.bundle.BundleConfiguration;

/**
 * @author xingchuan.qxc
 * @date 2019/5/22 20:55
 */
public class RunDoraemonBundle {

    private BundleConfiguration bundleConfiguration;

    private AsgardBundle bundle;

    public RunDoraemonBundle(String bundlePath, String extractPath) {
        this.bundleConfiguration = new BundleConfiguration(bundlePath, extractPath);
        this.bundle = new AsgardBundle(bundleConfiguration, RunDoraemonBundle.class.getClassLoader());
    }

    public void run() {
        bundle.init();
    }

    public AsgardBundle getBundle() {
        return bundle;
    }

    public String getBundleName() {
        return bundle != null ? bundle.getBundleName() : "";
    }

    public <T> T getBundleBean(Class<T> typeClass) {
        BundleService bundleService = this.bundle.getBundleService();
        if (bundleService != null) {
            return bundleService.getBundleBean(typeClass);
        }
        return null;
    }
}
