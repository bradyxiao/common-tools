package com.tencent.qcloud.service_component.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class HttpRequest {

    private String url;
    private String method;
    private Map<String, List<String>> header;
    private RequestBody requestBody;
    private ResponseBody responseBody;

    private HttpRequest(Builder builder){
        this.url = builder.url;
        this.method = builder.method;
        this.header = builder.header;
        this.requestBody = builder.requestBody;
        this.responseBody = builder.responseBody;
    }

    /**
     * 进行校验和检测
     */

    public String getUrl(){
        return this.url;
    }

    public String getMethod(){
        return this.method;
    }

    public Map<String, List<String>> getHeaders(){
        return header;
    }

   public void buildRequestBody(OutputStream outputStream) throws IOException {
        if(outputStream != null && requestBody != null){
            requestBody.writeTo(outputStream);
        }
   }

    public ResponseBody getResponseBody(){
        return this.responseBody;
    }


    public static class Builder{
        private String url;
        private String method;
        private Map<String, List<String>> header;
        private RequestBody requestBody;
        private ResponseBody responseBody;

        public Builder(){
            this.header = new LinkedHashMap<>();
        }

        public Builder(HttpRequest httpRequest){
            if(httpRequest != null){
                this.url = httpRequest.url;
                this.requestBody = httpRequest.requestBody;
                this.header = httpRequest.header;
                this.method = httpRequest.method;
                this.responseBody = httpRequest.responseBody;
            }
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder method(String method){
            this.method = method;
            return this;
        }

        public Builder addHeader(String key, String value){
           if(key != null && value != null){
               List<String> values;
               if(header.containsKey(key)){
                   values = header.get(key);
               }else {
                   values = new ArrayList<>();
               }
               values.add(value);
               header.put(key, values);
           }
            return this;
        }

        public Builder string(String content){
            this.requestBody = new RequestBody.StringRequestBody(content);
            return this;
        }

        public Builder bytes(byte[] content){
            this.requestBody = new RequestBody.BytesRequestBody(content);
            return this;
        }

        public Builder file(File content){
            this.requestBody = new RequestBody.FileRequestBody(content);
            return this;
        }

        public Builder parseResponseBody(ResponseBody responseBody){
            this.responseBody = responseBody;
            return this;
        }

        public HttpRequest build(){
            HttpRequest httpRequest = new HttpRequest(this);
            return httpRequest;
        }
    }
}
