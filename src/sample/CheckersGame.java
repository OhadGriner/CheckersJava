package sample;

import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static sample.Game.*;

public class CheckersGame extends Application {
    public static Cell[][]board;



    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root=new Pane();
        Scene scene=new Scene(root, CELL_SIZE*BOARD_WIDTH+200,CELL_SIZE*BOARD_HEIGHT);



        //add scene to stage
        primaryStage.setScene(scene);
        //set Title of stage
        primaryStage.setTitle("Checkers Game");
        Player p1=new Player("192.168.0.175","Ohad",5,6);
        Player p2=new Player("192.168.0.188","Alma",2,3);
        Game g=new Game(root,p1,p2,p1);

        //display the content of the stage
        primaryStage.show();

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("hello", new NetworkServant(g));


    }
    public static void main(String []args){
        launch(args);
    }
}