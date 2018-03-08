package com.tencent.qcloud.download_tool.module;

import android.text.TextUtils;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.listener.OnProgressListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class DownloadRequest {

    private String downloadUrl;

    private String localDirPath;
    private String localFileName;

    private List<String> requestHeader;

    private OnProgressListener onProgressListener;

    public DownloadRequest(String downloadUrl, String localDirPath, String localFileName){
        this.downloadUrl = downloadUrl;
        this.localDirPath = localDirPath;
        this.localFileName = localFileName;
        requestHeader = new ArrayList<>();
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getLocalDirPath() {
        return localDirPath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setRequestHeader(String key, String value){
        if(key != null && value != null){
            requestHeader.add(key);
            requestHeader.add(value);
        }
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    public OnProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public void checkParamters() throws ClientException{
        if(TextUtils.isEmpty(downloadUrl)){
            throw new ClientException("downloadUrl[" + downloadUrl + "] invalid" );
        }
        if(localFileName == null || localDirPath == null){
            throw new ClientException("localDirPath[" + localDirPath + "]  or localFileName[" +
                    localFileName + "] invalid" );
        }
    }

}
