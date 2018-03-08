package com.bradyxiao.network_tool.request;

import com.bradyxiao.network_tool.common.ErrorCode;
import com.bradyxiao.network_tool.common.HttpMethod;
import com.bradyxiao.network_tool.common.HttpProtocol;
import com.bradyxiao.network_tool.exception.QClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by bradyxiao on 2017/12/22.
 * <H1> 请求报文封装类 </H1>
 * <P>
 *      <ul>
 *          <li>method </li>
 *          <li>host:port + path + query ,etc.</li>
 *          <li>header </li>
 *          <li>body</li>
 *      </ul>
 *
 * </P>
 */

public final class RequestSerialize {

    private static final String TAG = "RequestSerialize";


    private String scheme;
    private String httpMethod;
    private String host;
    private String path;
    private List<String> queryParament;
    private Map<String, List<String>> requestHeader;
    private RequestBodySerialize requestBodySerialize;

    public RequestSerialize(){
        this.scheme = HttpProtocol.HTTP;
        this.httpMethod = HttpMethod.GET;
        this.path = "/";
        queryParament = new ArrayList<>(); //有序的
        requestHeader = new LinkedHashMap<>();
    }


    private String getUrl(){
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(scheme)
                .append("://")
                .append(host)
                .append(path);
        if(queryParament.size() > 0){
            urlBuilder.append("?");
            int size = queryParament.size();
            int i;
            for( i = 0; i < size -2; i += 2){
                urlBuilder.append(queryParament.get(i));
                String value = queryParament.get(i + 1);
                if(value != null && value.length() != 0){
                    urlBuilder.append("=").append(value);
                }
                urlBuilder.append("&");
            }
            urlBuilder.append(queryParament.get(i));
            String value = queryParament.get(i + 1);
            if(value != null && value.length() != 0){
                urlBuilder.append("=").append(value);
            }
        }
        return urlBuilder.toString();
    }

    private Headers getHeaders(){
        int size = requestHeader.size();
        if(size <= 0)return null;
        Headers.Builder builder = new Headers.Builder();
        List<String> values;
        String key;
        for(Map.Entry<String, List<String>> entry : requestHeader.entrySet()){
            values = entry.getValue();
            key = entry.getKey();
            for (String value : values){
                builder.add(key, value);
            }
        }
        return builder.build();
    }


    public Request createRequest(){
        Request.Builder builder = new Request.Builder();
        builder.url(getUrl())
                .headers(getHeaders())
                .method(httpMethod, requestBodySerialize);
        return builder.build();
    }

    public void setHost(String host) throws QClientException {
        //host必须参数，需校验
        if(host == null)throw new QClientException(ErrorCode.HOST_IS_NULL.getCode(),
                ErrorCode.HOST_IS_NULL.getMessage());
        this.host = host;
    }

    public void setHttp(boolean isHttps){
        if(isHttps) this.scheme = HttpProtocol.HTTPS;
        else scheme = HttpProtocol.HTTP;
    }

    public void setHttpMethod(String httpMethod) throws QClientException {
        //是否支持的method
        if(!HttpMethod.isSupportMethod(httpMethod)) throw new QClientException(ErrorCode.HTTP_METHOD_INVALID,
                ErrorCode.HTTP_METHOD_INVALID.getMessage());
        this.httpMethod = httpMethod;
    }

    public void setQueryParament(String key, String value){
        if(key != null){
            queryParament.add(key);
            queryParament.add(value);
        }
    }

    public void setQueryParamet(Map<String, String> queryParameters){
        if(queryParameters != null){
            for(Map.Entry<String,String> entry : queryParameters.entrySet()){
                this.queryParament.add(entry.getKey());
                this.queryParament.add(entry.getValue());
            }
        }
    }

    public void setHeader(String key, String value){
        if(key != null){
            List<String> headerValues;
            if (requestHeader.containsKey(key)){
                headerValues = requestHeader.get(key);
                headerValues.add(value);
            }else {
                headerValues = new ArrayList<>();
                requestHeader.put(key, headerValues);
            }
        }
    }

    public void setHeader(Map<String, List<String>> headers){
        if(headers != null){
            this.requestHeader.putAll(headers);
        }
    }

    public void setRequestBodySerialize(RequestBodySerialize requestBodySerialize){
        if(requestBodySerialize != null){
            this.requestBodySerialize = requestBodySerialize;
        }
    }

}
