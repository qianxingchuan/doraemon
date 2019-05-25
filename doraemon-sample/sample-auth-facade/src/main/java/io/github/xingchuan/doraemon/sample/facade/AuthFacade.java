package io.github.xingchuan.doraemon.sample.facade;

import java.util.Map;

/**
 * 样例： 鉴权接口
 *
 * @author xingchuan.qxc
 * @date 2019/5/22 21:29
 */
public interface AuthFacade {

    /**
     * 鉴权接口
     *
     * @param params 鉴权所需参数
     * @return true -> 鉴权通过 false ->鉴权失败
     */
    boolean auth(Map<String, String> params);
}
