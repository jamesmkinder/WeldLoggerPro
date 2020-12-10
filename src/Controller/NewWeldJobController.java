package Controller;

import Model.DBMSConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class NewWeldJobController implements Initializable {

    @FXML public Button createJob;
    @FXML public Button cancel;
    @FXML public TextField clockNum;
    @FXML public TextField size;
    @FXML public TextField figNum;
    @FXML public TextField fwo;
    @FXML public TextField rmc;
    @FXML public TextField qaprtNum;
    @FXML public CheckBox rework;
    @FXML public ComboBox<String> department;
    @FXML public ComboBox<String> material;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date now = new Date(System.currentTimeMillis());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        department.getItems().setAll("Forge Steel", "Small Cast", "Large Cast");
        material.getItems().setAll("Base metal", "Stellite 21", "Stellite 6", "Stoody", "Norem", "Deloro 50", "Other");
        createJob.setOnAction(e ->{
            DBMSConnection connection = new DBMSConnection();
            if (fwo.getText().isBlank()){
                alert.setContentText("FWO field is required");
                alert.showAndWait();
                return;
            }
            if (clockNum.getText().isBlank()){
                alert.setContentText("Clock Number field is required");
                alert.showAndWait();
                return;
            }
            if (rmc.getText().isBlank()){
                alert.setContentText("RMC field is required");
                alert.showAndWait();
                return;
            }

            int reworkYesNo = 0;
            if (rework.isSelected()) reworkYesNo = 1;
            String insertQuery = String.format("INSERT INTO WELD_JOBS (FWO, WELDER_CLOCK_NUM, SIZE, FIG_NUM, RMC, QAP_RT_NUM, REWORK, DATE_ENTERED, DEPARTMENT, MATERIAL)" +
                    " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d, #%s#, '%s', '%s')", fwo.getText(), clockNum.getText(), size.getText(), figNum.getText(),
                    rmc.getText(), qaprtNum.getText(), reworkYesNo, now, department.getSelectionModel().getSelectedItem(), material.getSelectionModel().getSelectedItem());

            System.out.println(insertQuery);

            try {
                Statement addWeldLog = connection.getConnection().createStatement();
                addWeldLog.executeUpdate(insertQuery);
                connection.getConnection().close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            Stage window = (Stage) cancel.getScene().getWindow();
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

        cancel.setOnAction(e ->{
            Stage window = (Stage) cancel.getScene().getWindow();
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

    }
}
