package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatchMaker extends UnicastRemoteObject implements Match {
    private Player player1;
    private Player player2;
    private GameCouple gc;
    public MatchMaker() throws RemoteException {
        super();
        player1=null;
        player2=null;
        gc=null;
    }
    public void addPlayer(Player p)throws RemoteException{
        if (player1==null) {
            player1 = p;
            System.out.println(player1.toString());
        }
        else{
            player2=p;
            System.out.println(player1.toString());
            System.out.println(player2.toString());
            //send both players details of game
            gc=new GameCouple(player1,player2);
            player1=null;
            player2=null;
        }
    }
    public GameCouple getGC()throws RemoteException{
        return gc;
    }
}
