package server;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{


    @Override
    public void start(Stage stage) throws Exception {
        Registry registry = LocateRegistry.createRegistry(5097);
        registry.rebind("matchMaking", new MatchMaker());

    }
    public static void main(String[] args){
        launch(args);
    }
}
