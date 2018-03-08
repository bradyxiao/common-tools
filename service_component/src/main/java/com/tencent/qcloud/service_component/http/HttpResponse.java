package com.tencent.qcloud.service_component.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class HttpResponse {

    int httpCode;
    String httpMessage;
    String protocol;

    Map<String, List<String>> header;

    ResponseBody responseBody;

    public HttpResponse(ResponseBody responseBody){
        this.responseBody = responseBody;
    }



    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void parseBody(InputStream inputStream) throws IOException {
        if(responseBody != null && inputStream != null){
            responseBody.readFrom(inputStream, getContentLength());
        }
    }

    public Map<String, List<String>> header(){
        return header;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public long getContentLength(){
        long contentLength = -1L;
        if(header.containsKey("Content-Length")){
            contentLength = Long.parseLong(header.get("Content-Length").get(0));
        }
        return contentLength;
    }
}
