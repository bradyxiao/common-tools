package com.tencent.qcloud.service_component.http;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public abstract class ResponseBody {

    public abstract void readFrom(InputStream inputStream, long length) throws IOException;

    public static class StringResponse extends ResponseBody{

        private static final String TAG = "StringResponse";

        public StringResponse(){}

        @Override
        public void readFrom(InputStream inputStream, long length) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            if(inputStream != null){
                int len = inputStream.read(buffer);
                long recv = 0L;
                while (len >= 0){
                    byteArrayOutputStream.write(buffer, 0, len);
                    recv += len;
                    Log.d(TAG, "" + recv + "/" + length);
                    len = inputStream.read(buffer);
                }
            }
            String content = byteArrayOutputStream.toString("utf-8");
            Log.d(TAG, "body" + content);
        }
    }
}
