package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        primaryStage.setTitle("Weld Logger Pro v1.0");
        primaryStage.setScene(new Scene(root, 700, 550));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args);}
}
