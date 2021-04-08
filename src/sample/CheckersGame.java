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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static sample.Game.*;

public class CheckersGame extends Application {
    public static Cell[][]board;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root=new Pane();
        Scene scene=new Scene(root, CELL_SIZE*BOARD_WIDTH,CELL_SIZE*BOARD_HEIGHT);



        //add scene to stage
        primaryStage.setScene(scene);
        //set Title of stage
        primaryStage.setTitle("Checkers Game");
        Player p1=new Player("192.168.0.175");
        Player p2=new Player("192.168.0.188");
        Game g=new Game(root,p1,p2);

        //display the content of the stage
        primaryStage.show();

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("hello", new HelloServant(g));
    }
    public static void main(String args[]){
        launch(args);
    }
}