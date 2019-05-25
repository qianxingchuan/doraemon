package io.github.qianxingchuan.doraemon.sample.auth.run;

import io.github.qianxingchuan.framework.doraemon.BundleService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xingchuan.qxc
 * @date 2019/5/22 21:44
 */
public class SampleBundleRun implements BundleService {

    private ClassPathXmlApplicationContext applicationContext;

    @Override
    public void doIt() {
        this.applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    }


    @Override
    public <T> T getBundleBean(Class<T> typeClass) {
        return applicationContext.getBean(typeClass);
    }
}
