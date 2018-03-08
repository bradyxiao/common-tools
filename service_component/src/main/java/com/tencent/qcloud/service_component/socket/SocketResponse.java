package com.tencent.qcloud.service_component.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class SocketResponse {

    public void recevieData(InputStream inputStream) throws IOException {
        if(inputStream != null){
            byte[] buffer = new byte[1024 * 4];
            int len = inputStream.read(buffer);
            while (len >= 0){
                Log.d("SocketResponse", new String(buffer, 0, len, "utf-8"));
                len = inputStream.read(buffer);
            }
        }
    }
}
