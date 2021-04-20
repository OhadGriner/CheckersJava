package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static sample.Game.*;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label invalidLabel;
    @FXML
    private Label registerStatus;
    @FXML
    private Button moveToRegisterButton;
    @FXML
    private Button moveToLoginButton;
    @FXML
    private TextField input_user_name;
    @FXML
    private TextField input_first_name;
    @FXML
    private TextField input_last_name;
    @FXML
    private TextField input_password;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Button moveToBoxScoreButton;
    private static String serverIp="192.168.0.175";


    public void loginButtonAction(ActionEvent event){
        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        if(tf_username.getText().isBlank() || tf_password.getText().isBlank()){
            invalidLabel.setText("Can't leave a field empty");
        }
        else if(sqlConnect.searchLogin(tf_username.getText(),tf_password.getText()) ==false)//change later to wrong password
            invalidLabel.setText("Invalid Login - Try again");
        else{
            Stage stage=(Stage) loginButton.getScene().getWindow();
            try {
                String user_name = tf_username.getText();
                int games_won=sqlConnect.getNumOfWins(user_name);
                int games_played=sqlConnect.getNumOfGames(user_name);
                startGame(stage,user_name,games_won,games_played,false);//creating game
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void registerButtonAction(ActionEvent event){
        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        if(input_user_name.getText().isBlank() || input_first_name.getText().isBlank()|| input_last_name.getText().isBlank()|| input_password.getText().isBlank()){
            registerStatus.setTextFill(Color.RED);
            registerStatus.setText("Can't leave a field empty");
        }
        else if(sqlConnect.isUserExist(input_user_name.getText())){
            registerStatus.setTextFill(Color.RED);
            registerStatus.setText("Username is taken, try another one");
        }
        else{
            registerStatus.setTextFill(Color.BLACK);
            registerStatus.setText("Registration is completed ");
            sqlConnect.insertByValue(input_first_name.getText(),input_last_name.getText(),input_user_name.getText(),input_password.getText(),0,0);
        }
    }

    public void cancelButtonAction(ActionEvent event){
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void moveToRegister(ActionEvent event) throws IOException {
        Stage stage=(Stage) moveToRegisterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage.setTitle("Checkers Game");
        stage.setScene(new Scene(root, 600, 319));

    }

    public void moveToLogin(ActionEvent event)throws IOException {
        Stage stage=(Stage) moveToLoginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Checkers Game");
        stage.setScene(new Scene(root, 600, 319));

    }

    public void moveToBoxScore(ActionEvent event){
        Stage stage=(Stage) moveToBoxScoreButton.getScene().getWindow();

        //Username column
        TableColumn<TableRow,String> userNameColumn=new TableColumn<>("Username");
        userNameColumn.setMinWidth(100);
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("uName"));
        //First name column
        TableColumn<TableRow,String>firstNameColumn=new TableColumn<>("First name");
        firstNameColumn.setMinWidth(50);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("fName"));
        //Last name column
        TableColumn<TableRow,String>lastNameColumn=new TableColumn<>("Last name");
        lastNameColumn.setMinWidth(50);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lName"));
        //Games wom column
        TableColumn<TableRow,Integer>gamesWonColumn=new TableColumn<>("Games won");
        gamesWonColumn.setMinWidth(20);
        gamesWonColumn.setCellValueFactory(new PropertyValueFactory<>("gWon"));
        //Games played column
        TableColumn<TableRow,Integer>gamesPlayedColumn=new TableColumn<>("Games played");
        gamesPlayedColumn.setMinWidth(20);
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gPlayed"));
        //Winning rate column
        TableColumn<TableRow,Double>winRateColumn=new TableColumn<>("Winning rate");
        winRateColumn.setMinWidth(100);
        winRateColumn.setCellValueFactory(new PropertyValueFactory<>("winRate"));

        TableView<TableRow>table=new TableView<>();
        table.setItems(getPlayers());
        table.getColumns().addAll(userNameColumn,firstNameColumn,lastNameColumn,gamesWonColumn,gamesPlayedColumn,winRateColumn);

        Button b=new Button("Back to Log in");
        b.setStyle("-fx-text-fill: white; -fx-background-color: #964b00;");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("login.fxml"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stage.setTitle("Checkers Game");
                stage.setScene(new Scene(root, 600, 319));
            }
        });
        VBox vbox=new VBox();
        vbox.getChildren().addAll(table,b);
        Scene scene=new Scene(vbox);
        stage.setScene(scene);
    }

    public ObservableList<TableRow> getPlayers(){
        ObservableList<TableRow>players= FXCollections.observableArrayList();
        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        sqlConnect.updateList(players);

        return players;
    }

    public static void startGame(Stage stage,String user_name , int games_won,int games_played,boolean secondGame) throws Exception {
        System.out.println(user_name);
        System.out.println(games_won);
        System.out.println(games_played);

        InetAddress address= InetAddress.getLocalHost();
        String ip=address.getHostAddress();


        Match service = (Match) Naming.lookup("rmi://"+serverIp+":5097/matchMaking");//letting server know that you want a match
        Player p=new Player(ip,user_name,games_won,games_played);
        service.addPlayer(p);

        GameCouple gc=service.getGC();


        BorderPane borderPane=new BorderPane();
        Label l=new Label("Waiting for another player to join");
        l.setStyle("-fx-text-fill: white; -fx-font-size: 45pt; -fx-font-weight: bold; -fx-background-color:#000000");
        borderPane.setStyle("-fx-background-color:#000000");
        borderPane.setCenter(l);

        Scene scene1= new Scene(borderPane);
        stage.setScene(scene1);
        stage.show();;

        while (gc == null){//waiting for a rival
            gc=service.getGC();
        }
        service.initGC();

        Pane root=new Pane();
        Scene scene=new Scene(root, CELL_SIZE*BOARD_WIDTH+200,CELL_SIZE*BOARD_HEIGHT);



        //add scene to stage
        stage.setScene(scene);
        //set Title of stage
        stage.setTitle("Checkers Game");
        Player p1=gc.getPlayer1();
        Player p2=gc.getPlayer2();
        Player self=(p1.getUser_name().equals(user_name)) ? p1 : p2;
        Game g=new Game(root,p1,p2,self);

        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        sqlConnect.updateGamesPlayed(user_name,games_played+1);


        //display the content of the stage
        stage.show();
        if (!secondGame) {
            int port = (p1.getUser_name().equals(user_name)) ? 5099 : 5098;
            Registry registry1 = LocateRegistry.createRegistry(port);
            registry1.rebind("hello", new NetworkServant(g));
        }
    }

}