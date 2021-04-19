package sample;

import javafx.application.Platform;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NetworkServant extends UnicastRemoteObject implements Network {
    private Game g;
    public NetworkServant(Game g)throws RemoteException{
        super();
        this.g=g;
    }
    public void sendBoard(CellDescriptor des)throws RemoteException{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                g.updateWithDes(des);
                g.changeTurn();
                g.checkWin();//make sure losing side updates after loss
            }
        });


    }


}
