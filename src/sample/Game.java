package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

import static sample.CheckersGame.board;

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

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int xClick = (int) Math.floor(e.getSceneX()/CELL_SIZE);
                int yClick = (int) Math.floor( e.getSceneY()/CELL_SIZE);
                click(xClick,yClick);

            }
        };
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

        return mat;
    }



    // method when clicking on a piece
    private static void click(int xPos,int yPos){
        System.out.print(xPos);
        System.out.print(",");
        System.out.println(yPos);
        int xMove;
        int yMove;
        Position pos = wasClicked();    // array of cells that was clicked on the last round
        ArrayList<Position> posMoves = new ArrayList<>(); //save all possible moves

        if(pos.isEmpty()){ // check that this is the first click on piece - to move it
            if  (!board[xPos][yPos].isEmpty()) // the clicked cell has Piece - this is the first click
                posMoves=calcPosMoves(xPos,yPos); // which possible moves the piece has and will be colored in greed
            if (posMoves.size()==0){return;}
            board[xPos][yPos].setStroke(Color.GREEN);
            board[xPos][yPos].setStrokeWidth(5);
            for(int i=0;i<posMoves.size();i++){
                xMove=posMoves.get(i).getX();
                yMove=posMoves.get(i).getY();
                board[xMove][yMove].setFill(Color.GREEN);
            }
            board[xPos][yPos].getPiece().setClicked(true);
        }
        else{
            posMoves=calcPosMoves(pos.getX(),pos.getY());
            for(int i=0;i<posMoves.size();i++){
                xMove=posMoves.get(i).getX();
                yMove=posMoves.get(i).getY();
                if(xPos==xMove && yPos==yMove){
                    makeMove(pos.getX(),pos.getY(),xPos,yPos);
                }
            }
            clearBoard();
        }


    }
    // method for making the move of a piece from one cell to new cell
    private static void makeMove(int xFrom,int yFrom,int xTo,int yTo){
        int delX=-1;
        int delY=-1;//indexes for piece to delete in eating movement
        Piece tempPiece = board[xFrom][yFrom].getPiece();
        board[xTo][yTo].setPiece(tempPiece);
        Circle circle = new Circle(xTo*CELL_SIZE+0.5*CELL_SIZE, yTo*CELL_SIZE+0.5*CELL_SIZE, 0.35*CELL_SIZE);
        if(tempPiece.getPlayerNum()==1)
            circle.setFill(PLAYER1_COLOR);
        else
            circle.setFill(PLAYER2_COLOR);
        board[xFrom][yFrom].getPiece().getChildren().clear();

        if(yTo==0 || yTo==BOARD_HEIGHT-1){
            board[xTo][yTo].getPiece().turnQueen();

        }
        //update if queen
        if(board[xTo][yTo].getPiece().isQueen()){
            circle.setStroke(Color.YELLOW);
            circle.setStrokeWidth(4);
        }
        //check for eating movemnt
        if(xFrom+2==xTo && yFrom+2==yTo){
            delX=xFrom+1;
            delY=yFrom+1;
        }
        if(xFrom-2==xTo && yFrom+2==yTo){
            delX=xFrom-1;
            delY=yFrom+1;
        }
        if(xFrom-2==xTo && yFrom-2==yTo){
            delX=xFrom-1;
            delY=yFrom-1;
        }
        if(xFrom+2==xTo && yFrom-2==yTo){
            delX=xFrom+1;
            delY=yFrom-1;
        }
        if (delX!=-1 && delY!=-1){
            board[delX][delY].getPiece().getChildren().clear();
            board[delX][delY].setPiece(null);
        }





        System.out.println(xFrom + "," + yFrom + " make move-> " + xTo + "," + yTo);
        board[xFrom][yFrom].setPiece(null);
        board[xTo][yTo].getPiece().getChildren().add(circle);

    }
    // method for checking the cells that the clicked piece can move to
    private static ArrayList<Position> calcPosMoves(int xPos,int yPos){
        int direction = board[xPos][yPos].getPiece().getPlayerNum();
        int xCheck;
        int yCheck;
        ArrayList<Position> moves = new ArrayList<>();

        //check for regular move
        xCheck=xPos+1;
        yCheck=yPos+direction;
        if(canCheck(xCheck,yCheck)&&board[xCheck][yCheck].isEmpty()){
            moves.add(new Position(xCheck,yCheck));
        }
        xCheck=xPos-1;
        if(canCheck(xCheck,yCheck)&& board[xCheck][yCheck].isEmpty()){
            moves.add(new Position(xCheck,yCheck));
        }

        //check for queen regular moves
        xCheck=xPos+1;
        yCheck=yPos-direction;
        if(board[xPos][yPos].getPiece().isQueen() && canCheck(xCheck,yCheck) && board[xCheck][yCheck].isEmpty()){
            moves.add(new Position(xCheck,yCheck));
        }
        xCheck=xPos-1;
        if(board[xPos][yPos].getPiece().isQueen() && canCheck(xCheck,yCheck)&& board[xCheck][yCheck].isEmpty()){
            moves.add(new Position(xCheck,yCheck));
        }

        //check for eating move
        xCheck=xPos+2;
        yCheck=yPos+2*direction;
        if(canCheck(xCheck,yCheck) && board[xCheck][yCheck].isEmpty() && !board[xPos+1][yPos+direction].isEmpty()
        && board[xPos+1][yPos+direction].getPiece().getPlayerNum()!= board[xPos][yPos].getPiece().getPlayerNum()){
            moves.add(new Position(xCheck,yCheck));
        }

        xCheck=xPos-2;
        if(canCheck(xCheck,yCheck) && board[xCheck][yCheck].isEmpty() && !board[xPos-1][yPos+direction].isEmpty()
                && board[xPos-1][yPos+direction].getPiece().getPlayerNum()!= board[xPos][yPos].getPiece().getPlayerNum()){
            moves.add(new Position(xCheck,yCheck));
        }



        //check for queen eating move
        xCheck=xPos+2;
        yCheck=yPos-2*direction;
        if(board[xPos][yPos].getPiece().isQueen() && canCheck(xCheck,yCheck) && board[xCheck][yCheck].isEmpty() && !board[xPos+1][yPos-direction].isEmpty()
                && board[xPos+1][yPos-direction].getPiece().getPlayerNum()!= board[xPos][yPos].getPiece().getPlayerNum()){
            moves.add(new Position(xCheck,yCheck));
        }

        xCheck=xPos-2;
        if(board[xPos][yPos].getPiece().isQueen() && canCheck(xCheck,yCheck) && board[xCheck][yCheck].isEmpty() && !board[xPos-1][yPos-direction].isEmpty()
                && board[xPos-1][yPos-direction].getPiece().getPlayerNum()!= board[xPos][yPos].getPiece().getPlayerNum()){
            moves.add(new Position(xCheck,yCheck));
        }


        return moves;
    }
    //method to check if X & Y are of location are a valid cell
    private static boolean canCheck(int xPos,int yPos){
        return(xPos>=0 && yPos>=0 && xPos<BOARD_WIDTH && yPos<BOARD_HEIGHT);
    }

    private static void clearBoard(){
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
    private static Position wasClicked(){
        Position temp = new Position(-1,-1);
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if (!board[i][j].isEmpty() && board[i][j].getPiece().isClicked()){
                    temp.setX(i);
                    temp.setY(j);
                    return temp;
                }
            }
        }
        return temp;
    }
}
