package com.tencent.qcloud.service_component.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by bradyxiao on 2018/1/5.
 */

public class RealSocket {
    private SocketRequest socketRequest;

    public RealSocket(SocketRequest socketRequest){
        this.socketRequest = socketRequest;
    }

    public SocketResponse execute() throws IOException {

        Socket socket = startSocket(socketRequest);

        OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
        socketRequest.sendData(outputStream);
        outputStream.flush();
        outputStream.close();

        SocketResponse socketResponse = new SocketResponse();
        InputStream inputStream = new BufferedInputStream(socket.getInputStream());
        socketResponse.recevieData(inputStream);
        inputStream.close();
        return socketResponse;
    }

    public Socket startSocket(SocketRequest socketRequest) throws IOException {
        Socket socket = new Socket(socketRequest.getHost(), socketRequest.getPort());
        return socket;
    }
}
