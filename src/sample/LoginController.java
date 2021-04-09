package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
                startGame(stage,user_name,games_won,games_played);//creating game
            }
            catch (Exception e){
                System.out.println("Couldn't play game");
            }
        }
    }

    public void registerButtonAction(ActionEvent event){
        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        if(input_user_name.getText().isBlank() || input_first_name.getText().isBlank()|| input_last_name.getText().isBlank()|| input_password.getText().isBlank()){
            registerStatus.setText("Can't leave a field empty");
        }
        else if(sqlConnect.isUserExist(input_user_name.getText())){
            registerStatus.setText("Username is taken, try another one");
        }
        else{
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
    private static void startGame(Stage stage,String user_name , int games_won,int games_played) throws Exception {
        System.out.println(user_name);
        System.out.println(games_won);
        System.out.println(games_played);

        InetAddress address= InetAddress.getLocalHost();
        String ip=address.getHostAddress();


        Match service = (Match) Naming.lookup("rmi://192.168.0.175:5097/matchMaking");//letting server know that you want a match
        Player p=new Player(ip,user_name,games_won,games_played);
        service.addPlayer(p);

        GameCouple gc=service.getGC();
        while (gc == null){//waiting for a rival
            gc=service.getGC();
        }
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



        //display the content of the stage
        stage.show();
        int port=(p1.getUser_name().equals(user_name)) ? 5099 : 5098;
        Registry registry1 = LocateRegistry.createRegistry(port);
        registry1.rebind("hello", new HelloServant(g));
    }

}