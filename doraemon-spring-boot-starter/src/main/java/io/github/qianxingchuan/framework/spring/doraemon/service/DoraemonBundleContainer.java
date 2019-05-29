package io.github.qianxingchuan.framework.spring.doraemon.service;

import io.github.qianxingchuan.framework.doraemon.main.RunDoraemonBundle;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xingchuan.qxc
 * @version  2019/5/30 21:20
 */
public class DoraemonBundleContainer {

    private ConcurrentHashMap<String, RunDoraemonBundle> doraemonBundlesMap = new ConcurrentHashMap<>();

    /**
     * add bundle to map
     *
     * @param bundle bundle
     */
    public void addDoraemonBundle(RunDoraemonBundle bundle) {
        this.doraemonBundlesMap.put(bundle.getBundleName(), bundle);
    }

    public RunDoraemonBundle getBundleByName(String name) {
        return doraemonBundlesMap.get(name);
    }


}
