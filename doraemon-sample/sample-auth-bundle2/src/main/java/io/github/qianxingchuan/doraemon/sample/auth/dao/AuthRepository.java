package io.github.qianxingchuan.doraemon.sample.auth.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * @author xingchuan.qxc
 * @date 2019/5/22 21:38
 */
@Component
public class AuthRepository {

    /**
     * key : username
     * value : password
     */
    private Map<String, String> authMap = new HashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("bundle2 execute repository init.");
        authMap.put("zhuxintian", "777777");
        authMap.put("xiaying", "888888");
    }

    public boolean auth(String user, String pass) {
        System.out.println("bundle2.AuthRepository print " + this.getClass().getClassLoader());
        String password = authMap.get(user);
        return password != null && password.equals(pass);
    }
}
