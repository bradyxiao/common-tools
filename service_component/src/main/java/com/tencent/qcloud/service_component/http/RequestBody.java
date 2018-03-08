package com.tencent.qcloud.service_component.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public abstract class RequestBody {

    public abstract long getContentLength();

    public abstract String getMineType();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public static class StringRequestBody extends RequestBody{

        String content;

        public StringRequestBody(String content){
            this.content = content;
        }

        @Override
        public long getContentLength() {
            return content != null ? content.length() : 0L;
        }

        @Override
        public String getMineType() {
            return "text/plain";
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            if(outputStream != null && content != null){
                outputStream.write(content.getBytes("utf-8"));
            }
            outputStream.flush();
            outputStream.close();
        }
    }

    public static class BytesRequestBody extends RequestBody{

        byte[] content;

        public BytesRequestBody(byte[] content){
            this.content = content;
        }

        @Override
        public long getContentLength() {
            return content != null ? content.length : 0L;
        }

        @Override
        public String getMineType() {
            return "text/plain";
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            if(outputStream != null && content != null){
                outputStream.write(content);
            }
            outputStream.flush();
            outputStream.close();
        }
    }


    public static class FileRequestBody extends RequestBody{

        File content;

        public FileRequestBody(File content){
            this.content = content;
        }

        @Override
        public long getContentLength() {
            return content != null ? content.length() : 0L;
        }

        @Override
        public String getMineType() {
            return "text/plain";
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            if(outputStream != null && content != null){
                BufferedInputStream bufferedInputStream =
                        new BufferedInputStream(new FileInputStream(content));
                byte[] buffer = new byte[1024 * 4];
                int len = bufferedInputStream.read(buffer);
                while (len >= 0){
                    outputStream.write(buffer, 0, len);
                    len = bufferedInputStream.read(buffer);
                }
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
