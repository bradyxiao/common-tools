package com.tencent.qcloud.service_component.http;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class ResponeIntercept {

    public static HttpURLConnection chain(HttpRequest httpRequest, HttpURLConnection httpURLConnection) throws IOException {
        if(httpRequest != null && httpURLConnection != null){
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP){
                String location = httpURLConnection.getHeaderField("Location");
                location.replace("https", "http");
                httpRequest = new HttpRequest.Builder(httpRequest)
                        .url(location)
                        .build();
            }
            return RealRequest.startConnect(httpRequest);
        }
        return httpURLConnection;
    }
}
