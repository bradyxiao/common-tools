package com.tencent.qcloud.service_component.http;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class RealRequest {

    private HttpRequest httpRequest;


    public RealRequest(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public HttpResponse execute() throws IOException {

        HttpURLConnection httpURLConnection = startConnect(httpRequest);

        /** before send data */


        OutputStream outputStream =  new BufferedOutputStream(httpURLConnection.getOutputStream());
        httpRequest.buildRequestBody(outputStream);
        outputStream.flush();
        outputStream.close();

        /** before receive data */
        httpURLConnection = ResponeIntercept.chain(httpRequest, httpURLConnection);

        HttpResponse httpResponse = new HttpResponse(httpRequest.getResponseBody());
        httpResponse.setHttpCode(httpURLConnection.getResponseCode());
        httpResponse.setHttpMessage(httpURLConnection.getResponseMessage());
        httpResponse.setProtocol("HTTP/1.1");
        httpResponse.setHeader(httpURLConnection.getHeaderFields());

        InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
        httpResponse.parseBody(inputStream);
        inputStream.close();

        return httpResponse;
    }

    private static HttpURLConnection initConnect(HttpRequest httpRequest) throws IOException {
        if(httpRequest.getUrl() == null){
            throw new IOException("url is null");
        }
        URL url = new URL(httpRequest.getUrl());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(Configure.socketTimeOut);
        httpURLConnection.setReadTimeout(Configure.socketTimeOut);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
    }

    public static HttpURLConnection startConnect(HttpRequest httpRequest) throws IOException {
        HttpURLConnection httpURLConnection = initConnect(httpRequest);
        checkMethod(httpRequest.getMethod());
        httpURLConnection.setRequestMethod(httpRequest.getMethod());
        for(Map.Entry<String, List<String>> entry : httpRequest.getHeaders().entrySet()){
            for(String string : entry.getValue()){
                httpURLConnection.setRequestProperty(entry.getKey(), string);
            }
        }
        httpURLConnection.connect();
        return httpURLConnection;
    }

    private static void checkMethod(String method) throws IOException {
        boolean isValied = "GET".equalsIgnoreCase(method) ||
                "POST".equalsIgnoreCase(method) ||
                "PUT".equalsIgnoreCase(method);
        if(!isValied){
            throw new IOException("method is valied :" + method);
        }
    }


}
