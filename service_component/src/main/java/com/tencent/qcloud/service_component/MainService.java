package com.tencent.qcloud.service_component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tencent.qcloud.service_component.http.HttpRequest;
import com.tencent.qcloud.service_component.http.HttpResponse;
import com.tencent.qcloud.service_component.socket.SocketResponse;

import java.util.List;
import java.util.Map;

public abstract class MainService extends Service {

    private static final String TAG = "MainService";

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStart");
        //realExecute();
        //realExecute2();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service onUnBind");
        return super.onUnbind(intent);
    }

    public abstract HttpResponse executeHttpRequest();

    private void realExecute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpResponse httpResponse = executeHttpRequest();
                if(httpResponse != null && httpResponse.header() != null){
                    for(Map.Entry<String,List<String>> entry : httpResponse.header().entrySet()){
                        Log.d("MainService", entry.getKey() + ":" + entry.getValue().get(0));
                    }
                }
            }
        }).start();
    }

    public abstract SocketResponse executeSocketRequest();

    private void realExecute2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketResponse SocketResponse = executeSocketRequest();
            }
        }).start();
    }
}
