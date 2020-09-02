package io.github.qianxingchuan.doraemonspringbootexpample.controller;

import java.util.HashMap;
import java.util.Map;

import io.github.qianxingchuan.doraemon.sample.facade.AuthFacade;
import io.github.qianxingchuan.framework.doraemon.main.RunDoraemonBundle;
import io.github.qianxingchuan.framework.spring.doraemon.service.DoraemonBundleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xingchuan.qxc
 * @since 2020/09/02
 */
@RestController
public class MyTestAuthController {

    @Autowired
    private DoraemonBundleContainer doraemonBundleContainer;

    @RequestMapping("/testAuth.do")
    public boolean auth1(String userName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("user", userName);
        params.put("pass", password);
        RunDoraemonBundle bundle = doraemonBundleContainer.getBundleByName(
            "sample-auth-bundle1-2048-SNAPSHOT-release");
        boolean auth = bundle.getBundleBean(AuthFacade.class).auth(params);
        return auth;
    }
}
