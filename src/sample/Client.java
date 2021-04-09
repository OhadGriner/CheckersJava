package sample;

import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static sample.Game.*;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        InetAddress address= InetAddress.getLocalHost();
        String ip=address.getHostAddress();


        Match service = (Match) Naming.lookup("rmi://192.168.0.175:5097/matchMaking");//letting server know that you want a match
        String user_name ="Ohad";
        Player p=new Player(ip,user_name,0,0);
        service.addPlayer(p);

        GameCouple gc=service.getGC();
        while (gc == null){//waiting for a rival
            gc=service.getGC();
        }
        Pane root=new Pane();
        Scene scene=new Scene(root, CELL_SIZE*BOARD_WIDTH+200,CELL_SIZE*BOARD_HEIGHT);



        //add scene to stage
        primaryStage.setScene(scene);
        //set Title of stage
        primaryStage.setTitle("Checkers Game");
        Player p1=gc.getPlayer1();
        Player p2=gc.getPlayer2();
        Player self=(p1.getUser_name().equals(user_name)) ? p1 : p2;
        Game g=new Game(root,p1,p2,self);



        //display the content of the stage
        primaryStage.show();
        int port=(p1.getUser_name().equals(user_name)) ? 5099 : 5098;
        Registry registry1 = LocateRegistry.createRegistry(port);
        registry1.rebind("hello", new HelloServant(g));




    }
    public static void main(String args[]){
        launch(args);
    }
}