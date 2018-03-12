package com.tencent.qcloud.download_tool.core;

import android.util.Log;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;
import com.tencent.qcloud.download_tool.module.Range;
import com.tencent.qcloud.download_tool.util.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class MultiThreadDownloadTask extends DownloadTask {

    private Object syncObject = new Object();

    private ExecutorService executorService;
    private ListenerHandler2 listenerHandler2;
    private long hasReceiveLength;
    private int brokenDownByIndex = -1;
    private boolean brokenDown = false;
    private boolean isCanceled = false;

    private List<RunTask> runTaskList = Collections.synchronizedList(new ArrayList<RunTask>());

    private long totalContentLength;

    public MultiThreadDownloadTask(OkHttpClient okHttpClient) {
        super(okHttpClient);
        executorService = Executors.newFixedThreadPool(3);
        listenerHandler2 = new ListenerHandler2();
    }

    @Override
    public DownloadResult syncDownload() throws ClientException, ServerException {
        listenerHandler2.onWaiting();
        totalContentLength = getContentLength(downloadRequest);
        hasReceiveLength = downloadRequest.getRange().getStart();
        final List<Part> parts = new ArrayList<>();
        long sliceSize = totalContentLength / 3;
        long start = downloadRequest.getRange().getStart();
        for(int i = 0; i < 2; i ++){
            Part part = new Part();
            part.localSavePath = downloadRequest.getLocalSavePath();
            part.start = start;
            part.end = part.start + sliceSize - 1;
            parts.add(part);
            start = part.end + 1;
        }
        Part part = new Part();
        part.start = start;
        part.end = downloadRequest.getRange().getStart() + totalContentLength -1;
        part.localSavePath = downloadRequest.getLocalSavePath();
        parts.add(part);
        listenerHandler2.onRunning();
        for(int i = 0; i < 3; i ++){
            RunTask runTask = new RunTask(parts.get(i), downloadRequest, i);
            runTaskList.add(runTask);
            executorService.submit(runTask);
        }
        while (!isExit()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        listenerHandler2.onCompleted();
        if(isSuccess()){
            DownloadResult downloadResult = new DownloadResult();
            //listenerHandler2.onSuccess(downloadResult);
            return downloadResult;
        }else{
            //all task shutdown and collect error message
            executorService.shutdownNow();
            while (!executorService.isTerminated()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
            if(isCanceled){
                throw new ServerException("canceled");
            }
            int errorIndex = brokenDownByIndex == -1 ? 0 : brokenDownByIndex;
            if(runTaskList.get(errorIndex).clientException != null){
                throw runTaskList.get(errorIndex).clientException;
            }else {
                throw runTaskList.get(errorIndex).serverException;
            }
        }
    }

    private boolean isExit(){
      int size = runTaskList.size();
      boolean isExit = true;
      for(int i = 0; i < size; i++){
          if(!runTaskList.get(i).isExit()){
              isExit = false;
              break;
          }
      }
      return isExit;
    }

    private boolean isSuccess(){
        int size = runTaskList.size();
        boolean isSuccess = true;
        for(int i = 0; i < size; i++){
            if(!runTaskList.get(i).isSuccess()){
                isSuccess = false;
                break;
            }
        }
        return isSuccess;
    }

    public synchronized void error(int index){
        synchronized (syncObject){
            if(brokenDown)return;
            brokenDown = true;
            brokenDownByIndex = index;
        }

    }

    public synchronized void checkError() throws BrokenException {
        synchronized (syncObject){
            if(brokenDown) throw new BrokenException("broken exception");
        }

    }



    @Override
    public void asyncDownload() {
        listenerHandler2.onWaiting();
    }

    @Override
    public boolean cancel() {
        if(isCanceled)return false;
        synchronized (syncObject){
            isCanceled = true;
            error(0);
        }
        return true;
    }

    private long getContentLength(DownloadRequest downloadRequest) throws ServerException, ClientException {
        try{
            Request request = createRequest(downloadRequest);
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            long contentLength = response.body().contentLength();
            return contentLength;
        }catch (IOException e) {
            throw new ServerException(e);
        } catch (ClientException e) {
            throw e;
        }
    }

    private Request createRequest(Part part, DownloadRequest sourceDownloadRequest) throws ClientException {
        sourceDownloadRequest.checkParamters();
        Request request = null;
        Request.Builder requestBuilder = new Request.Builder()
                .url(downloadRequest.getDownloadUrl())
                .get();
        List<String> headers = downloadRequest.getRequestHeader();
        Range range = new Range(part.start, part.end);
        boolean isAdd = false;
        int size = headers.size();
        for(int i = 0; i <= size -2; i += 2){
            String key = headers.get(i);
            if(key.equals("Range")){
                requestBuilder.addHeader(key, range.toString());
                isAdd = true;
            }else {
                requestBuilder.addHeader(key, headers.get(i + 1));
            }
        }
        if(!isAdd){
            requestBuilder.addHeader("Range", range.toString());
        }
        request = requestBuilder.build();
        return request;
    }

    private DownloadResult handlerResponse(Part part, Response response,
                                           Call call, ListenerHandler2 listenerHandler2) throws ServerException, ClientException, BrokenException {
        checkError();
        if(response.code() != 200 && response.code() != 206){
            try {
                String errorMsg = response.body().string();
                throw new ServerException(errorMsg);
            } catch (IOException e) {
                throw new ServerException(e);
            }
        }else{
            long start = part.start;
            InputStream inputStream = response.body().byteStream();
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(part.localSavePath,
                        "rws");
                randomAccessFile.seek(start);
                byte[] buffer = new byte[2 * 1024];
                int len = inputStream.read(buffer, 0, buffer.length);
                while (len != -1){
                    checkError();
                    randomAccessFile.write(buffer, 0, len);
                    listenerHandler2.onProgress(len);
                    len = inputStream.read(buffer, 0, buffer.length);
                    Log.d("Unit", Thread.currentThread().getName() + "->" + len);
                }
                DownloadResult downloadResult = new DownloadResult();
                return downloadResult;
            } catch (FileNotFoundException e) {
                throw new ClientException(e);
            } catch (IOException e) {
                if(call.isCanceled()){
                    throw new ServerException("Canceled");
                }else {
                    throw new ServerException(e);
                }
            }catch (BrokenException e){
                throw e;
            }catch (Throwable e){
                throw new ClientException(e);
            }finally {
                Utils.close(randomAccessFile);
                Utils.close(inputStream);
            }
        }
    }

    private DownloadResult downloadOnePart(Part part, DownloadRequest sourceDownloadRequest) throws ClientException, ServerException, BrokenException {
        try {
            checkError();
            Request request = createRequest(part, sourceDownloadRequest);
            Call newCall = okHttpClient.newCall(request);
            checkError();
            Response response = newCall.execute();
            DownloadResult downloadResult = handlerResponse(part, response, newCall, listenerHandler2);
            return downloadResult;
        }catch (ClientException e) {
            throw e;
        }catch (ServerException e) {
            throw e;
        }catch (IOException e) {
            throw new ServerException(e);
        } catch (BrokenException e) {
            throw e;
        }
    }

    private class RunTask implements Runnable{

        public final static short INIT_STATE = 0;
        public final static short FAILED_STATE = 1;
        public final static short SUCCESS_STATE = 2;
       // private Object syncObject = new Object();
        public short taskState = INIT_STATE;

        public int index;
        public Part part;
        public DownloadRequest downloadRequest;
        public ClientException clientException;
        public ServerException serverException;
        public DownloadResult downloadResult;

        public RunTask(Part part, DownloadRequest downloadRequest, int index){
            this.part = part;
            this.downloadRequest = downloadRequest;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                downloadResult = downloadOnePart(part, downloadRequest);
                changeTaskState(SUCCESS_STATE);
            } catch (ClientException e) {
                error(index);
                clientException = e;
                changeTaskState(FAILED_STATE);
            } catch (ServerException e) {
                error(index);
                serverException = e;
                changeTaskState(FAILED_STATE);
            } catch (BrokenException e) {
                //e.printStackTrace();
                changeTaskState(FAILED_STATE);
            }
        }

        public void changeTaskState(short taskState){
            synchronized (syncObject){
                this.taskState = taskState;
            }
        }

        public boolean isFailed(){
            synchronized (syncObject){
                if(taskState == FAILED_STATE){
                    return true;
                }else {
                    return false;
                }
            }
        }

        public boolean isExit(){
            synchronized (syncObject){
                if(taskState != INIT_STATE){
                    return true;
                }else {
                    return false;
                }
            }
        }

        public boolean isSuccess(){
            synchronized (syncObject){
                if(taskState == SUCCESS_STATE ){
                    return true;
                }else {
                    return false;
                }
            }
        }
    }

    private static class Part{
        public long start;
        public long end;
        public String localSavePath;
    }

    private class ListenerHandler2{

       // private Object syncObject = new Object();

        public void onProgress(long receivedLength) {
            synchronized (syncObject){
                hasReceiveLength += receivedLength;
                listenerHandler.onProgress(hasReceiveLength, totalContentLength);
            }
        }


        public void onWaiting() {
            listenerHandler.onWaiting();
        }


        public void onRunning() {
            listenerHandler.onRunning();
        }


        public void onRetry(int count) {
            listenerHandler.onRetry(count);
        }


        public void onCompleted() {
            listenerHandler.onCompleted();
        }


        public void onSuccess(DownloadResult downloadResult) {
            listenerHandler.onSuccess(downloadRequest, downloadResult);
        }


        public void onFailed(ClientException clientException, ServerException serverException) {
            //中断线程池
            //onFailed
            listenerHandler.onFailed(downloadRequest, clientException, serverException);
        }
    }

    private class BrokenException extends Exception{
        public BrokenException(String msg){
            super(msg);
        }
    }

}
