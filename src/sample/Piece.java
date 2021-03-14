package sample;

import static sample.CheckersGame.*;
import static sample.Game.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Piece extends Pane {

    private int playerNum;
    private boolean isClicked;
    private int xPosition;
    private int yPosition;

    // Piece constructor
    public Piece(int x,int y,Color color){
        isClicked=false;
        this.xPosition=x;
        this.yPosition=y;
        Circle circle = new Circle(x*CELL_SIZE+0.5*CELL_SIZE, y*CELL_SIZE+0.5*CELL_SIZE, 0.35*CELL_SIZE);
        circle.setFill(color);
        getChildren().add(circle);

        if(color==PLAYER1_COLOR){
            playerNum=1;
        }
        else{
            playerNum=-1;
        }

        setOnMousePressed(e -> {
            int xClick = (int) Math.floor(e.getSceneX()/CELL_SIZE);
            int yClick = (int) Math.floor( e.getSceneY()/CELL_SIZE);
            click(xClick,yClick);


        });
    }
    // method when clicking on a piece
    private void click(int xPos,int yPos){
        System.out.print(xPos);
        System.out.print(",");
        System.out.println(yPos);
        int xMove;
        int yMove;
        ArrayList<Integer> pos = wasClicked();    // array of cells that was clicked on the last round
        ArrayList<ArrayList<Integer>> posMoves = new ArrayList<>(); //save all possible moves

        if(pos.size()==0){ // check that this is the first click on piece - to move it
            if  (!board[xPos][yPos].isEmpty()) // the clicked cell has Piece - this is the first click
                posMoves=calcPosMoves(xPos,yPos); // which possible moves the piece has and will be colored in greed
            if (posMoves.size()==0){return;}
            board[xPos][yPos].setStroke(Color.GREEN);
            board[xPos][yPos].setStrokeWidth(5);
            for(int i=0;i<posMoves.size();i++){
                xMove=posMoves.get(i).get(0);
                yMove=posMoves.get(i).get(1);
                board[xMove][yMove].setFill(Color.GREEN);
            }
            board[xPos][yPos].getPiece().isClicked=true;
        }
        else{
            posMoves=calcPosMoves(pos.get(0),pos.get(1));
            for(int i=0;i<posMoves.size();i++){
                xMove=posMoves.get(i).get(0);
                yMove=posMoves.get(i).get(1);
                if(xPos==xMove && yPos==yMove){
                    makeMove(pos.get(0),pos.get(1),xPos,yPos);
                }
            }
            clearBoard();
        }


    }
    // method for making the move of a piece from one cell to new cell
    private void makeMove(int xFrom,int yFrom,int xTo,int yTo){
        Piece tempPiece = board[xFrom][yFrom].getPiece();
        board[xTo][yTo].setPiece(tempPiece);
        Circle circle = new Circle(xTo*CELL_SIZE+0.5*CELL_SIZE, yTo*CELL_SIZE+0.5*CELL_SIZE, 0.35*CELL_SIZE);
        if(tempPiece.playerNum==1)
            circle.setFill(PLAYER1_COLOR);
        else
            circle.setFill(PLAYER2_COLOR);
        board[xFrom][yFrom].getPiece().getChildren().clear();


        System.out.println(xFrom + "," + yFrom + " make move-> " + xTo + "," + yTo);
        board[xFrom][yFrom].setPiece(null);
        board[xTo][yTo].getPiece().getChildren().add(circle);

    }
    // method for checking the cells that the clicked piece can move to
    private ArrayList<ArrayList<Integer>> calcPosMoves(int xPos,int yPos){
        int direction = board[xPos][yPos].getPiece().getPlayerNum();
        int xCheck;
        int yCheck;
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        xCheck=xPos+1;
        yCheck=yPos+direction;
        if(canCheck(xCheck,yCheck)&&board[xCheck][yCheck].isEmpty()){
            moves.add(new ArrayList());
            moves.get(moves.size()-1).add(xCheck);
            moves.get(moves.size()-1).add(yCheck);
        }
        xCheck=xPos-1;
        if(canCheck(xCheck,yCheck)&& board[xCheck][yCheck].isEmpty()){
            moves.add(new ArrayList());
            moves.get(moves.size()-1).add(xCheck);
            moves.get(moves.size()-1).add(yCheck);
        }
        return moves;
    }
    //method to check if X & Y are of location are a valid cell
    private boolean canCheck(int xPos,int yPos){
        return(xPos>=0 && yPos>=0 && xPos<BOARD_WIDTH && yPos<BOARD_HEIGHT);
    }

    private void clearBoard(){
        boolean color=true;
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if(color)
                    board[i][j].setFill(CELL1_COLOR);
                else
                    board[i][j].setFill(CELL2_COLOR);
                color=!color;
                if(!board[i][j].isEmpty()){
                    board[i][j].getPiece().setClicked(false);
                }
                board[i][j].setStroke(Color.BLACK);
                board[i][j].setStrokeWidth(1);
                if(!board[i][j].isEmpty())
                    board[i][j].getPiece().getChildren().get(0).setViewOrder(5.0);
            }
            color=!color;
        }
    }
    // method to check the cells that can be clicked to move the piece
    private ArrayList<Integer> wasClicked(){
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if (!board[i][j].isEmpty() && board[i][j].getPiece().isClicked){
                    temp.add(i);
                    temp.add(j);
                    return temp;
                }
            }
        }
        return temp;
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
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
