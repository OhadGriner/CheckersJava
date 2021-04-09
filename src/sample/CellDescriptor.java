package sample;

import java.io.Serializable;

public class CellDescriptor implements Serializable {
    private int [][] pieceColorNum;//number of color of piece 1/-1 for player 1 or 2 0for empty
    public CellDescriptor(Cell[][]board){
        pieceColorNum=new int[8][8];
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++) {
                if(board[i][j].isEmpty())
                    pieceColorNum[i][j]=0;
                else if(board[i][j].getPiece().getPlayerNum()==1){
                    if(board[i][j].getPiece().isQueen())
                        pieceColorNum[i][j]=2;
                    else
                        pieceColorNum[i][j]=1;
                }
                else if(board[i][j].getPiece().getPlayerNum()==-1)
                    if(board[i][j].getPiece().isQueen())
                        pieceColorNum[i][j]=-2;
                    else
                        pieceColorNum[i][j]=-1;

            }
        }
    }
    public int[][] getMat(){
        return pieceColorNum;
    }
}
