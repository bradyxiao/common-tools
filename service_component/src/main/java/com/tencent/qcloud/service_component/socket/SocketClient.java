package com.tencent.qcloud.service_component.socket;

import java.io.IOException;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class SocketClient {

    public SocketClient(){}

    public SocketResponse execute(SocketRequest socketRequest) throws IOException {
        RealSocket realSocket = new RealSocket(socketRequest);
        return realSocket.execute();
    }
}
