package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML public Button newWeldJob;
    @FXML public Button login;
    @FXML public Button addWeldLog;
    @FXML public Button allWeldJobs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        login.setOnAction(e ->{
            Stage window = (Stage) login.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        newWeldJob.setOnAction(e ->{
            Stage window = (Stage) newWeldJob.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/NewWeldJob.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        addWeldLog.setOnAction(e ->{
            Stage window = (Stage) addWeldLog.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/AddWeldLog.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        allWeldJobs.setOnAction(e ->{
            Stage window = (Stage) addWeldLog.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/AllWeldJobs.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

    }
}