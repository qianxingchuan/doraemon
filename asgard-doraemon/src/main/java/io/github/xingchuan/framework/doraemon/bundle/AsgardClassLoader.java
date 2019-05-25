package io.github.xingchuan.framework.doraemon.bundle;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * custom classloader .
 * on bundle depend on one AsgardClassloader.
 * The AsgardClassLoader will load all classes in bundle exclude [exported classes].
 *
 * @author xingchuan.qxc
 * @date 2019/5/14 16:58
 */
public class AsgardClassLoader extends URLClassLoader {


    private BundleConfiguration bundleConfiguration;

    private ConcurrentHashMap<String, Class<?>> classLoaded = new ConcurrentHashMap<>();

    /**
     * default constructor
     *
     * @param urls                bundle url.
     * @param parent              parent classloader
     * @param bundleConfiguration bundle configure
     */
    public AsgardClassLoader(URL[] urls, ClassLoader parent, BundleConfiguration bundleConfiguration) {
        super(urls, parent);
        this.bundleConfiguration = bundleConfiguration;
    }

    @Override
    public Class<?> findClass(String name) {
        Class<?> clazz = classLoaded.get(name);
        if (clazz != null) {
            return clazz;
        }
        try {
            return super.findClass(name);
        } catch (Exception e) {
            // skip this exception
            return null;
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (bundleConfiguration.containSkipClass(name)) {
            //使用父加载器加载
            return super.loadClass(name);
        }
        //优先模块加载器加载
        Class<?> classLoadInBundle = findClass(name);
        if (classLoadInBundle != null) {
            classLoaded.put(name, classLoadInBundle);
            return classLoadInBundle;
        }
        return super.loadClass(name);
    }
}
