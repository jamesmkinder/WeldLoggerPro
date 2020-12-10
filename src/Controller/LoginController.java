package Controller;

import Model.DBMSConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML public Button cancelButton;
    @FXML public Button loginButton;
    @FXML public TextField username;
    @FXML public PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cancelButton.setOnAction(e ->{
            Stage window = (Stage) cancelButton.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        loginButton.setOnAction(e ->{

            try {
                Connection connection = new DBMSConnection().getConnection();
                String query = "SELECT USERNAME, USER_TYPE FROM USERS WHERE USERNAME = '" + username.getText() + "' AND PASSWORD = '" + password.getText() + "';";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    String userType = rs.getString("USER_TYPE");
                    if (userType.equals("MANAGER")) {
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.close();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("../View/Dashboard.fxml"));
                            Scene scene = new Scene(root);
                            window.setScene(scene);
                            window.show();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    else if (userType.equals("LEAD")){
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.close();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("../View/LeadDashboard.fxml"));
                            Scene scene = new Scene(root);
                            window.setScene(scene);
                            window.show();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    else if (userType.equals("QA")){
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.close();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("../View/QaDashboard.fxml"));
                            Scene scene = new Scene(root);
                            window.setScene(scene);
                            window.show();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    else {
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.close();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("../View/Dashboard.fxml"));
                            Scene scene = new Scene(root);
                            window.setScene(scene);
                            window.show();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Invalid Username/Password combination");
                    alert.showAndWait();
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });


    }
}
