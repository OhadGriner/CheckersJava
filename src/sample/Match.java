package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Match extends Remote {//create a match interface
    public void addPlayer(Player p)throws RemoteException;
    public GameCouple getGC()throws RemoteException;
    public void initGC()throws RemoteException;
}
