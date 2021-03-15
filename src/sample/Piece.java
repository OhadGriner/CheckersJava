package sample;

import static sample.CheckersGame.*;
import static sample.Game.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Piece extends Pane {

    private boolean queen;
    private int playerNum;
    private boolean clicked;
    private int xPosition;
    private int yPosition;

    // Piece constructor
    public Piece(int x,int y,Color color){
        clicked=false;
        this.xPosition=x;
        this.yPosition=y;
        queen=false;
        Circle circle = new Circle(x*CELL_SIZE+0.5*CELL_SIZE, y*CELL_SIZE+0.5*CELL_SIZE, 0.35*CELL_SIZE);
        circle.setFill(color);
        getChildren().add(circle);

        if(color==PLAYER1_COLOR){
            playerNum=1;
        }
        else{
            playerNum=-1;
        }


    }


    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isQueen(){return queen;}

    public void turnQueen(){queen=true;}

}
