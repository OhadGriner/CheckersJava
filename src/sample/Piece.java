package sample;

import static sample.CheckersGame.*;
import static sample.Game.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends Pane {
    private boolean isClicked;
    private int xPos;
    private int yPos;
    public Piece(int x,int y,Color color){
        isClicked=false;
        this.xPos=x;
        this.yPos=y;
        Circle circle = new Circle(x*CELL_SIZE+0.5*CELL_SIZE, y*CELL_SIZE+0.5*CELL_SIZE, 0.35*CELL_SIZE);
        circle.setFill(color);
        getChildren().add(circle);


        setOnMousePressed(e -> {
            int xClick = (int) Math.floor(e.getSceneX()/CELL_SIZE);
            int yClick = (int) Math.floor( e.getSceneY()/CELL_SIZE);
            click(xClick,yClick);


        });
    }
    private void click(int xPos,int yPos){
        if(board[xPos][yPos].isEmpty()){return;}
        board[xPos][yPos].setStroke(Color.GREEN);
        board[xPos][yPos].setStrokeWidth(5);


    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
