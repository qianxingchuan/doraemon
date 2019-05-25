package io.github.xingchuan.doraemon.sample.auth.dao;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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
        System.out.println("bundle1 execute repository init.");
        authMap.put("xingchuan", "123456789");
        authMap.put("hanhaiyang", "741852963");
    }

    public boolean auth(String user, String pass) {
        System.out.println("bundle1.AuthRepository print " + this.getClass().getClassLoader());
        String password = authMap.get(user);
        return password.equals(pass);
    }
}
