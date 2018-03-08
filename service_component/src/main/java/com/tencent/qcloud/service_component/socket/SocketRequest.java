package com.tencent.qcloud.service_component.socket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class SocketRequest {

    private String host;
    private int port;

    public SocketRequest(){}

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void sendData(OutputStream outputStream) throws IOException{}
}
