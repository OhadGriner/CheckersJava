package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;


import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static sample.Game.*;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        InetAddress address= InetAddress.getLocalHost();
        String ip=address.getHostAddress();

        Match service = (Match) Naming.lookup("rmi://192.168.0.175:5097/matchMaking");
        service.addPlayer(new Player(ip,"Ohad"));


        String ipTo= (ip.equals(player1.getIp())) ? player2.getIp() : player1.getIp();
        String portTo=(self.getUser_name().equals(player1.getUser_name())) ? "5098" : "5099";
        Hello service = (Hello) Naming.lookup("rmi://"+ipTo+":"+portTo+"/hello");
        CellDescriptor des = new CellDescriptor(getBoard());
        service.sendBoard(des);
        */


        Pane root=new Pane();
        Scene scene=new Scene(root, CELL_SIZE*BOARD_WIDTH,CELL_SIZE*BOARD_HEIGHT);
        //add scene to stage
        primaryStage.setScene(scene);
        //set Title of stage
        primaryStage.setTitle("Checkers Game");
        Player p1=new Player("192.168.0.175","Ohad");
        Player p2=new Player("192.168.0.175","Alma");
        Game g=new Game(root,p1,p2,p1);

        //display the content of the stage
        primaryStage.show();

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("hello", new HelloServant(g));
    }
    public static void main(String args[]){
        launch(args);
    }
}