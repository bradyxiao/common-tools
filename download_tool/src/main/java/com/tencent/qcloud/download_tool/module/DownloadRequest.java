package com.tencent.qcloud.download_tool.module;

import android.text.TextUtils;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.listener.OnProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class DownloadRequest {


    private int taskId;

    private String downloadUrl;

    private String localDirPath;
    private String localFileName;

    private List<String> requestHeader;

    private OnProgressListener onProgressListener;

    private Range range;

    public DownloadRequest(String downloadUrl, String localDirPath, String localFileName){
        this.downloadUrl = downloadUrl;
        this.localDirPath = localDirPath;
        this.localFileName = localFileName;
        requestHeader = new ArrayList<>();
        taskId = UUID.randomUUID().hashCode();
    }

    public int getTaskId(){
        return taskId;
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

    public void setRange(long start, long end){
        if(end <= 0) end = -1L;
        if(start < 0) start = 0L;
        range = new Range(start, end);
        requestHeader.add("Range");
        requestHeader.add(range.toString());
    }

    public void setRange(long start){
        setRange(start, -1);
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public Range getRange() {
        return range;
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

    public String getLocalSavePath() throws ClientException{
        File file = new File(localDirPath);
        if(!file.exists()){
            boolean canMkdir = file.mkdirs();
            if(!canMkdir){
                throw new ClientException("can not create directory[" + localDirPath + "]");
            }
        }
        if(!localDirPath.endsWith(File.separator)){
            localDirPath = localDirPath + File.separator;
        }
        if(localFileName.startsWith(File.separator)){
            localFileName = localFileName.substring(1);
        }
        String localSavePath = localDirPath + localFileName;
        return localSavePath;
    }

}
