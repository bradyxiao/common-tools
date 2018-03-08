package com.tencent.qcloud.service_component.http;


import java.io.IOException;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class HttpClient {

    /**
     * 网络请求配置参数
     * 网络请求重试策略
     * 网络请求
     */
    public HttpClient(){

    }

    public HttpResponse excute(HttpRequest httpRequest) throws IOException {
        RealRequest realRequest = new RealRequest(httpRequest);
        return realRequest.execute();
    }


}
