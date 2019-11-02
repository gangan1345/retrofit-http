package com.develop.http.callback;

import java.util.Map;

/**
 * app 初始化HttpManager 时 实现此接口来 配置 参数
 * @author Angus
 */

public interface HttpParamsInterface {

    /**
     * 项目默认服务器地址
     * @return
     */
    String getBaseUrl();

    /**
     * 公共请求头
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * 请求公共参数
     * @return
     */
    Map<String, Object> getParams();
}
