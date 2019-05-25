package io.github.qianxingchuan.doraemon.sample.auth.impl;

import io.github.qianxingchuan.doraemon.sample.auth.dao.AuthRepository;
import io.github.qianxingchuan.doraemon.sample.facade.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xingchuan.qxc
 * @date 2019/5/22 21:37
 */
@Component
public class SampleAuthBundleImpl implements AuthFacade {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public boolean auth(Map<String, String> params) {
        String user = params.get("user");
        String pass = params.get("pass");
        return authRepository.auth(user, pass);
    }
}
