package io.github.qianxingchuan.framework.doraemon;

/**
 * @author xingchuan.qxc
 * @date 2019-05-23 20:34
 */
public interface BundleService {

    /**
     * custom run in bundle
     */
    void doIt();

    /**
     * get instance specified class
     * @param typeClass
     * @param <T>
     * @return
     */
    <T> T getBundleBean(Class<T> typeClass);
}
