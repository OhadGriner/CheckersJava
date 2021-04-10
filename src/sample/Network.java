package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Network extends Remote {
    public void sendBoard(CellDescriptor des) throws RemoteException;
}