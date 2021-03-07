package sample;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
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

        board=createBoard(root);
        //display the content of the stage
        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}