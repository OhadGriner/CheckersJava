package sample;

import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServant extends UnicastRemoteObject implements Hello{
    private Game g;
    public HelloServant(Game g)throws RemoteException{
        super();
        this.g=g;
    }
    public void sendBoard(CellDescriptor des)throws RemoteException{
        // Create a byte array input stream
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                g.updateWithDes(des);
            }
        });

    }


}
