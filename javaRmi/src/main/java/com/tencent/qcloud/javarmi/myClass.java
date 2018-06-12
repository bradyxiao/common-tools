package com.tencent.qcloud.javarmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class myClass {

    public static void main(String[] args){
        System.out.println("RMI");
        MyRmiImpl myRmi = new MyRmiImpl();
        try {
            MyRMIInterface stub = (MyRMIInterface) UnicastRemoteObject.exportObject(myRmi, 8888);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("RMI-EchoMessage", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

class MyRmiImpl implements MyRMIInterface{

    @Override
    public String test() throws RemoteException{
        return "welcome to rmi";
    }
}
