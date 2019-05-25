package io.github.qianxingchuan.sample.doraemon;

import io.github.qianxingchuan.doraemon.sample.facade.AuthFacade;
import io.github.qianxingchuan.framework.doraemon.bundle.AsgardClassLoader;
import io.github.qianxingchuan.framework.doraemon.main.RunDoraemonBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingchuan.qxc
 * @date 2019/5/24 10:08
 */
public class DoraemonSample {

    public static void main(String[] args) {
        String bundle1Path = DoraemonSample.class.getResource("/sample-auth-bundle1-2048-SNAPSHOT-release.zip").getPath();
        String bundle2Path = DoraemonSample.class.getResource("/sample-auth-bundle2-2048-SNAPSHOT-release.zip").getPath();
        System.out.println("bundle1 path is " + bundle1Path);
        System.out.println("bundle2 path is " + bundle2Path);
        RunDoraemonBundle[] runDoraemonBundles = new RunDoraemonBundle[2];
        RunDoraemonBundle runDoraemonBundle = new RunDoraemonBundle(bundle1Path, null);
        runDoraemonBundle.run();
        runDoraemonBundles[0] = runDoraemonBundle;

        RunDoraemonBundle runDoraemonBundle2 = new RunDoraemonBundle(bundle2Path, null);
        runDoraemonBundle2.run();
        runDoraemonBundles[1] = runDoraemonBundle2;

        // test data
        Map<String, Integer> authIndexMap = new HashMap<>();
        authIndexMap.put("xingchuan", 0);
        authIndexMap.put("hanhaiyang", 0);
        authIndexMap.put("zhuxintian", 1);
        authIndexMap.put("xiaying", 1);

        AsgardClassLoader bundle1ClassLoader = runDoraemonBundle.getBundle().getBundleClassLoader();
        AsgardClassLoader bundle2ClassLoader = runDoraemonBundle2.getBundle().getBundleClassLoader();
        ClassLoader defaultClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(defaultClassLoader);
        System.out.println(bundle1ClassLoader);
        System.out.println(bundle2ClassLoader);

        Thread.currentThread().setContextClassLoader(bundle1ClassLoader);
        Map<String, String> params = new HashMap<>();
        params.put("user", "xingchuan");
        params.put("pass", "xxxxxx");
        boolean isAuth = runDoraemonBundles[authIndexMap.get("xingchuan")].getBundleBean(AuthFacade.class).auth(params);
        System.out.println("用户xingchuan" + "正确的用户名和错误的错误码，结果是 " + isAuth);
        params.put("pass", "123456789");
        isAuth = runDoraemonBundles[authIndexMap.get("xingchuan")].getBundleBean(AuthFacade.class).auth(params);
        System.out.println("用户用户xingchuan" + "正确的用户名和正确的错误码，结果是 " + isAuth);

        Thread.currentThread().setContextClassLoader(bundle2ClassLoader);
        Map<String, String> params2 = new HashMap<>();
        params2.put("user", "xiaying");
        params2.put("pass", "aaaaa");
        boolean isAuth2 = runDoraemonBundles[authIndexMap.get("xiaying")].getBundleBean(AuthFacade.class).auth(params2);
        System.out.println("用户xiaying" + "正确的用户名和错误的错误码，结果是 " + isAuth2);
        params2.put("pass", "888888");
        isAuth2 = runDoraemonBundles[authIndexMap.get("xiaying")].getBundleBean(AuthFacade.class).auth(params2);
        System.out.println("用户xiaying" + "正确的用户名和正确的错误码，结果是 " + isAuth2);

    }
}
