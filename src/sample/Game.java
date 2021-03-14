package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Game {
    public static final int CELL_SIZE =80;
    public static final int BOARD_HEIGHT=8;
    public static final int BOARD_WIDTH=8;
    public static final Color CELL1_COLOR=Color.valueOf("#f0ebc7");
    public static final Color CELL2_COLOR=Color.valueOf("#d9ad67");
    public static final Color PLAYER1_COLOR=Color.valueOf("#313131");
    public static final Color PLAYER2_COLOR=Color.valueOf("#b91b1b");


    public static Cell[][] createBoard(Pane root){
        boolean placePiece=false;
        boolean color=true;
        Cell [][]mat=new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for(int i=0;i<mat.length;i++){
            for(int j=0;j<mat[i].length;j++){
                if(color)
                    mat[i][j]=new Cell(i,j,CELL1_COLOR);
                else
                    mat[i][j]=new Cell(i,j,CELL2_COLOR);


                root.getChildren().add(mat[i][j]);
                if (placePiece==true && (j<3) ){
                    Piece piece=new Piece(i,j,PLAYER1_COLOR);
                    mat[i][j].setPiece(piece);
                    root.getChildren().add(piece);
                }
                if(placePiece==true && (j>=BOARD_HEIGHT-3) ){
                    Piece piece=new Piece(i,j,PLAYER2_COLOR);
                    mat[i][j].setPiece(piece);
                    root.getChildren().add(piece);
                }
                mat[i][j].setViewOrder(6);
                color=!color;
                placePiece=!placePiece;
            }
            color=!color;
            placePiece=!placePiece;
        }
        return mat;
    }
}
