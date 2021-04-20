package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.util.ArrayList;

public class Game{
    public static final int CELL_SIZE =80;
    public static final int BOARD_HEIGHT=8;
    public static final int BOARD_WIDTH=8;
    public static final Color CELL1_COLOR=Color.valueOf("#f0ebc7");
    public static final Color CELL2_COLOR=Color.valueOf("#d9ad67");
    public static final Color PLAYER1_COLOR=Color.valueOf("#313131");
    public static final Color PLAYER2_COLOR=Color.valueOf("#b91b1b");

    private Cell[][]board;
    private int turn;
    private Pane root;
    private Player player1;
    private Player player2;
    private Player self;



    public Game(Pane root,Player p1,Player p2,Player _self){
        player1=p1;
        player2=p2;
        self=_self;
        boolean placePiece=false;
        boolean color=true;
        turn=1;
        this.root=root;

        board=new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if(color)
                    board[i][j]=new Cell(i,j,CELL1_COLOR);
                else
                    board[i][j]=new Cell(i,j,CELL2_COLOR);


                root.getChildren().add(board[i][j]);
                if (placePiece==true && (j<3) ){
                    Piece piece=new Piece(i,j,PLAYER1_COLOR,false);
                    board[i][j].setPiece(piece);
                    this.root.getChildren().add(piece);
                }
                if(placePiece==true && (j>=BOARD_HEIGHT-3) ){
                    Piece piece=new Piece(i,j,PLAYER2_COLOR,false);
                    board[i][j].setPiece(piece);
                    this.root.getChildren().add(piece);
                }
                board[i][j].setViewOrder(6);
                color=!color;
                placePiece=!placePiece;
            }
            color=!color;
            placePiece=!placePiece;
        }
        createNames();
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int xClick = (int) Math.floor(e.getSceneX()/CELL_SIZE);
                int yClick = (int) Math.floor( e.getSceneY()/CELL_SIZE);
                click(xClick,yClick);

            }
        };
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        Stage stage=(Stage)root.getScene().getWindow();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("Im out");
                int pNum=self.getUser_name().equals(player1.getUser_name())? 1:-1;
                try {
                    InetAddress address= InetAddress.getLocalHost();
                    String ip=address.getHostAddress();
                    String ipTo= (ip.equals(player1.getIp())) ? player2.getIp() : player1.getIp();//check which player am I
                    String portTo=(self.getUser_name().equals(player1.getUser_name())) ? "5098" : "5099";
                    Network service = (Network) Naming.lookup("rmi://"+ipTo+":"+portTo+"/hello");
                    service.quitMatch(pNum);
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
                Platform.exit();
                System.exit(0);
            }
        });

    }

    private void createNames() {//function to showcase on screen usernames and game record
        Label l1=new Label(player1.getUser_name());
        Label l2=new Label(player2.getUser_name());
        String gameRecord1=String.valueOf(player1.getGames_won())+"/"+String.valueOf(player1.getGames_played());
        Label l3=new Label(gameRecord1);
        String gameRecord2=String.valueOf(player2.getGames_won())+"/"+String.valueOf(player2.getGames_played());
        Label l4=new Label(gameRecord2);
        l1.relocate(BOARD_WIDTH*CELL_SIZE+5,0);
        l2.relocate(BOARD_WIDTH*CELL_SIZE+5,(BOARD_HEIGHT-1)*CELL_SIZE);
        l3.relocate(BOARD_WIDTH*CELL_SIZE+5,CELL_SIZE*0.5);
        l4.relocate(BOARD_WIDTH*CELL_SIZE+5,(BOARD_HEIGHT-0.5)*CELL_SIZE);
        l1.setStyle("-fx-text-fill: white; -fx-font-size: 20pt; -fx-font-weight: bold;");
        l2.setStyle("-fx-text-fill: white; -fx-font-size: 20pt; -fx-font-weight: bold;");
        l3.setStyle("-fx-text-fill: white; -fx-font-size: 20pt; -fx-font-weight: bold;");
        l4.setStyle("-fx-text-fill: white; -fx-font-size: 20pt; -fx-font-weight: bold;");
        Rectangle r=new Rectangle();
        r.setFill(Color.web("733404"));
        r.setWidth(200);
        r.setHeight(CELL_SIZE*BOARD_HEIGHT);
        r.relocate(BOARD_WIDTH*CELL_SIZE+1,0);
        root.getChildren().add(r);
        root.getChildren().add(l1);
        root.getChildren().add(l2);
        root.getChildren().add(l3);
        root.getChildren().add(l4);
    }


    // method when clicking on a piece
    private void click(int xPos,int yPos) {
        System.out.print(xPos);
        System.out.print(",");
        System.out.println(yPos);
        int xMove;
        int yMove;
        Position pos = wasClicked();    // array of cells that was clicked on the last round
        ArrayList<Position> posMoves = new ArrayList<>(); //save all possible moves
        if(!canCheck(xPos,yPos))
            return;
        if(pos.isEmpty()) { // check that this is the first click on piece - to move it


            if (!board[xPos][yPos].isEmpty() && turn != board[xPos][yPos].getPiece().getPlayerNum()){//not his turn
                return;
            }
            if(self.getUser_name().equals(player1.getUser_name())&&turn==-1) {//check for turn
                return;
            }
            if(self.getUser_name().equals(player2.getUser_name())&&turn==1) {//check for turn
                return;
            }



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
                    turn*=-1;
                    checkWin(0);
                }
            }
            clearBoard();
        }


    }
    // method for making the move of a piece from one cell to new cell
    public void makeMove(int xFrom,int yFrom,int xTo,int yTo)  {
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
        board[xFrom][yFrom].setPiece(null);
        board[xTo][yTo].getPiece().getChildren().add(circle);

        try {
            System.out.println("Before connection");//send board update to other player
            InetAddress address= InetAddress.getLocalHost();
            String ip=address.getHostAddress();
            String ipTo= (ip.equals(player1.getIp())) ? player2.getIp() : player1.getIp();//check which player am I
            String portTo=(self.getUser_name().equals(player1.getUser_name())) ? "5098" : "5099";
            Network service = (Network) Naming.lookup("rmi://"+ipTo+":"+portTo+"/hello");
            CellDescriptor des = new CellDescriptor(getBoard());
            service.sendBoard(des);
        }
        catch(Exception e){
            System.out.println(e.getCause());
        }

    }
    // method for checking the cells that the clicked piece can move to
    private ArrayList<Position> calcPosMoves(int xPos,int yPos){
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
    private boolean canCheck(int xPos,int yPos){
        return(xPos>=0 && yPos>=0 && xPos<BOARD_WIDTH && yPos<BOARD_HEIGHT);
    }

    private void clearBoard(){//clear board colors
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
    private Position wasClicked(){
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
    //check if any player won and update db
    public void checkWin(int pNum){//pNum determines if certain player lost
        //1: player 1 lost
        //-1:player 2 lost
        //0: there is no certain loser
        boolean p1=false;
        boolean p2=false;
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getPlayerNum()==1){
                    p1=true;
                }
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getPlayerNum()==-1){
                    p2=true;
                }
            }
        }
        if(p1==false||pNum==1){
            Label l=new Label(player2.getUser_name()+" won the game");
            l.setStyle("-fx-text-fill: black; -fx-font-size: 45pt; -fx-font-weight: bold; -fx-background-color:#dddddd");
            l.setTranslateY(280);
            root.getChildren().add(l);
            newGameButton();
            if(self.getUser_name().equals(player2.getUser_name())) {
                JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
                sqlConnect.connect();
                sqlConnect.updateGamesWon(player2.getUser_name(), player2.getGames_won() + 1);
                self.incGames_won();
            }
        }
        if(p2==false||pNum==-1){
            Label l=new Label(player1.getUser_name()+" won the game");
            l.setStyle("-fx-text-fill: black; -fx-font-size: 45pt; -fx-font-weight: bold; -fx-background-color:#dddddd");
            l.setTranslateY(280);
            root.getChildren().add(l);
            newGameButton();
            if(self.getUser_name().equals(player1.getUser_name())) {
                JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
                sqlConnect.connect();
                sqlConnect.updateGamesWon(player1.getUser_name(), player1.getGames_won() + 1);
                self.incGames_won();
            }
        }
    }

    public void newGameButton(){
        self.incGames_played();
        Button b=new Button("Start a new game");
        b.setStyle("-fx-text-fill: white; -fx-font-size: 30pt;-fx-background-color: #964b00;");
        b.setLayoutX(200);
        b.setLayoutY(380);
        b.setMinSize(120, 100);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    LoginController.startGame((Stage)root.getScene().getWindow(),self.getUser_name(),self.getGames_won(),self.getGames_played(),true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        root.getChildren().add(b);
    }

    public Cell[][] getBoard() {
        return board;
    }

    private void updateBoard(){//update display after board update
        root.getChildren().clear();
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                root.getChildren().add(board[i][j]);
                if(!board[i][j].isEmpty())
                    root.getChildren().add(board[i][j].getPiece());
            }

        }
        createNames();
    }
    public void updateWithDes(CellDescriptor des){//update board using Cell descriptor object
        int [][]mat=des.getMat();
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if(mat[i][j]==0)//cell is empty
                    board[i][j].setPiece(null);
                else if(mat[i][j]==1)
                    board[i][j].setPiece(new Piece(i,j,PLAYER1_COLOR,false));
                else if(mat[i][j]==-1)
                    board[i][j].setPiece(new Piece(i,j,PLAYER2_COLOR,false));
                else if(mat[i][j]==2){
                    Piece p=new Piece(i,j,PLAYER1_COLOR,true);
                    board[i][j].setPiece(p);
                }
                else if(mat[i][j]==-2){
                    Piece p=new Piece(i,j,PLAYER2_COLOR,true);
                    board[i][j].setPiece(p);
                }

            }
        }
        updateBoard();

    }
    public void changeTurn(){
        turn*=-1;
    }
}
