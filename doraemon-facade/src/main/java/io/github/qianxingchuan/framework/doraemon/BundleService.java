package io.github.qianxingchuan.framework.doraemon;

/**
 * @author xingchuan.qxc
 * @version 2019-05-23 20:34
 */
public interface BundleService {

    /**
     * custom run in bundle
     */
    void doIt();

    /**
     * get instance specified class
     *
     * @param typeClass 类型对应的class
     * @param <T>       待获取的类型的泛型
     * @return 此类型的实例
     */
    <T> T getBundleBean(Class<T> typeClass);
}
