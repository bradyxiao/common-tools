package com.tencent.qcloud.javarmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by bradyxiao on 2018/5/7.
 */
public interface MyRMIInterface extends Remote {
    String test() throws RemoteException;
}
