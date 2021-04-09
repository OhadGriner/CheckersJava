package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Match extends Remote {
    public void addPlayer(Player p)throws RemoteException;
    public GameCouple getGC();
}
