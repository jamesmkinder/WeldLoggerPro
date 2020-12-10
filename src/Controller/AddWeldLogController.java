package Controller;

import Model.DBMSConnection;
import Model.Part;
import Model.WeldJob;
import Model.WeldJobHolder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddWeldLogController implements Initializable {


    @FXML public Button addLog;
    @FXML public Button back;
    @FXML public TextField wms;
    @FXML public TextField rev;
    @FXML public TextField machNum;
    @FXML public TextField sizeAndType;
    @FXML public TextField heatAndOrLot;
    @FXML public TextField noOut;
    @FXML public TextField noIn;
    @FXML public TextField stubs;
    @FXML public TextField timeOut;
    @FXML public TextField timeIn;
    @FXML public TextField qualificationNumber;
    @FXML public TextField qualificationExpDate;
    @FXML public Button endJob;
    @FXML public CheckBox rmcCheck;
    @FXML public ListView<WeldJob> openJobsList;
    @FXML public TextField weldClockNum;
    @FXML public TextField partName;
    @FXML public TextField heatNum;
    @FXML public TextField serialNum;
    @FXML public TextField wmsProcess;
    @FXML public TableColumn<Part, String> partNameCol;
    @FXML public TableColumn<Part, String> heatNumCol;
    @FXML public TableColumn<Part, String> serialNumCol;
    @FXML public TableColumn<Part, String> wmsCol;
    @FXML public Button addPart;
    @FXML public Button deletePart;
    @FXML public TableView<Part> partsTable;
    @FXML public Button moMetal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) {
                return new SimpleStringProperty(p.getValue().getPartName());
            }
        });
        heatNumCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) {
                return new SimpleStringProperty(p.getValue().getHeatNum());
            }
        });
        serialNumCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) {
                return new SimpleStringProperty(p.getValue().getSerialNum());
            }
        });
        wmsCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) {
                return new SimpleStringProperty(p.getValue().getWmsProcess());
            }
        });


        DBMSConnection connection = new DBMSConnection();

        weldClockNum.setOnKeyTyped(e ->{
            String weldJobQuery = "SELECT FWO, WELDER_CLOCK_NUM, SIZE, FIG_NUM, RMC, QAP_RT_NUM, MATERIAL, REWORK, DATE_ENTERED, ENDED, DEPARTMENT, JOB_ID FROM WELD_JOBS WHERE ENDED = 0 AND WELDER_CLOCK_NUM = '" + weldClockNum.getText() + "'";
            ObservableList<WeldJob> weldJobsList = FXCollections.observableArrayList();

            try {
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(weldJobQuery);
                while (rs.next()){
                    WeldJob newJob = new WeldJob(rs.getString("FWO"), rs.getString("WELDER_CLOCK_NUM"), rs.getString("SIZE"),
                            rs.getString("FIG_NUM"), rs.getString("RMC"), rs.getString("QAP_RT_NUM"), rs.getString("MATERIAL"), rs.getBoolean("REWORK"),
                            rs.getDate("DATE_ENTERED"), rs.getBoolean("ENDED"), rs.getString("DEPARTMENT"), rs.getInt("JOB_ID"));
                    weldJobsList.add(newJob);
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            openJobsList.setCellFactory(param -> new ListCell<WeldJob>() {
                @Override
                protected void updateItem(WeldJob item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getFwo() == null) {
                        setText(null);
                    } else {
                        setText(item.getFwo());
                    }
                }
            });
            openJobsList.setItems(weldJobsList);
        });


        openJobsList.setOnMouseClicked(e ->{
            try {
                Statement statement = connection.getConnection().createStatement();

                ObservableList<Part> partsList = FXCollections.observableArrayList();
                String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId();
                partsTable.setItems(partsList);
                ResultSet rs = statement.executeQuery(partsQuery);
                while (rs.next()){
                    Part newPart = new Part(rs.getInt("PART_ID"), rs.getString("PART_NAME"), rs.getString("HEAT_NUM"), rs.getString("SERIAL_NUM"), rs.getString("WMS_PROCESS"));
                    partsList.add(newPart);
                }
                partsTable.setItems(partsList);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        addPart.setOnAction(e ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (partName.getText().isBlank()){
                alert.setContentText("Part Name field is required");
                alert.showAndWait();
                return;
            }
            if (heatNum.getText().isBlank()){
                alert.setContentText("Heat Number field is required");
                alert.showAndWait();
                return;
            }
            if (serialNum.getText().isBlank()){
                alert.setContentText("Serial Number field is required");
                alert.showAndWait();
                return;
            }
            if(wmsProcess.getText().isBlank()){
                alert.setContentText("WMS Process field is required");
                alert.showAndWait();
                return;
            }
            String insertStatement = "";
            try {
                insertStatement = String.format("INSERT INTO PARTS (JOB_ID, PART_NAME, HEAT_NUM, SERIAL_NUM, WMS_PROCESS) VALUES (%d, '%s', '%s', '%s', '%s')", openJobsList.getSelectionModel().getSelectedItem().getJobId(), partName.getText(),
                        heatNum.getText(), serialNum.getText(), wmsProcess.getText());
            } catch (NullPointerException exception){
                alert.setContentText("Please select a job to associate this part with.");
                alert.showAndWait();
                return;
            }
            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(insertStatement);
                ObservableList<Part> partsList = FXCollections.observableArrayList();
                String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId();
                ResultSet rs = statement.executeQuery(partsQuery);
                while (rs.next()){
                    Part newPart = new Part(rs.getInt("PART_ID"), rs.getString("PART_NAME"), rs.getString("HEAT_NUM"), rs.getString("SERIAL_NUM"), rs.getString("WMS_PROCESS"));
                    partsList.add(newPart);
                }
                partsTable.setItems(partsList);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        deletePart.setOnAction(e ->{
            if (partsTable.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a part to delete");
                alert.showAndWait();
                return;
            }
            String deleteStatement = "DELETE FROM PARTS WHERE PART_ID = (SELECT PART_ID FROM PARTS WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId() + " AND PART_NAME = '" +
                    partsTable.getSelectionModel().getSelectedItem().getPartName() + "' AND HEAT_NUM = '" + partsTable.getSelectionModel().getSelectedItem().getHeatNum() + "' AND SERIAL_NUM = '" + partsTable.getSelectionModel().getSelectedItem().getSerialNum() + "')";
            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(deleteStatement);
                ObservableList<Part> partsList = FXCollections.observableArrayList();
                String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId();
                ResultSet rs = statement.executeQuery(partsQuery);
                while (rs.next()){
                    Part newPart = new Part(rs.getInt("PART_ID"), rs.getString("PART_NAME"), rs.getString("HEAT_NUM"), rs.getString("SERIAL_NUM"), rs.getString("WMS_PROCESS"));
                    partsList.add(newPart);
                }
                partsTable.setItems(partsList);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        addLog.setOnAction(e ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (!rmcCheck.isSelected()){
                alert.setContentText("RMC Check must be checked");
                alert.showAndWait();
                return;
            }
            if (wms.getText().isBlank()){
                alert.setContentText("WMS field is required");
                alert.showAndWait();
                return;
            }
            if (rev.getText().isBlank()){
                alert.setContentText("Rev field is required");
                alert.showAndWait();
                return;
            }
            if (machNum.getText().isBlank()){
                alert.setContentText("Mach No. field is required");
                alert.showAndWait();
                return;
            }
            if (sizeAndType.getText().isBlank()){
                alert.setContentText("Filler Size & Type field is required");
                alert.showAndWait();
                return;
            }
            if (heatAndOrLot.getText().isBlank()){
                alert.setContentText("Filler Heat &/or Lot field is required");
                alert.showAndWait();
                return;
            }
            String insertQuery = "";
            try {
                insertQuery = String.format("INSERT INTO WELD_LOG (WMS, REV, MACH_NO, FILLER_SIZE_AND_TYPE, FILLER_HEAT_AND_OR_LOT, NO_OUT, NO_IN, STUBS, TIME_OUT, TIME_IN, " +
                                "QUALIFICATION_NUM, QUALIFICATION_EXP_DATE, QA_CHECKED, FOREMAN_CHECKED, WPS_CHECK, JOB_ID) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 0, 0, 1, %d)", wms.getText(), rev.getText(), machNum.getText(),
                        sizeAndType.getText(), heatAndOrLot.getText(), noOut.getText(), noIn.getText(), stubs.getText(), timeOut.getText(), timeIn.getText(), qualificationNumber.getText(), qualificationExpDate.getText(), openJobsList.getSelectionModel().getSelectedItem().getJobId());
            } catch (NullPointerException exception){
                alert.setContentText("Please select a job to associate this log with");
                alert.showAndWait();
                return;
            }
            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(insertQuery);

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            Stage window = (Stage) back.getScene().getWindow();
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

        moMetal.setOnAction(e ->{
            try {
                String moMetalQuery = "SELECT * FROM WELD_LOG WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId() + " AND LOG_ID = (SELECT MAX(LOG_ID) FROM WELD_LOG WHERE JOB_ID = " + openJobsList.getSelectionModel().getSelectedItem().getJobId() +
                        ")";
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(moMetalQuery);
                if(rs.next()) {
                    wms.setText(rs.getString("WMS"));
                    rev.setText(rs.getString("REV"));
                    machNum.setText(rs.getString("MACH_NO"));
                    sizeAndType.setText(rs.getString("FILLER_SIZE_AND_TYPE"));
                    heatAndOrLot.setText(rs.getString("FILLER_HEAT_AND_OR_LOT"));
                    stubs.setText(rs.getString("STUBS"));
                    qualificationNumber.setText(rs.getString("QUALIFICATION_NUM"));
                    qualificationExpDate.setText(rs.getString("QUALIFICATION_EXP_DATE"));
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("This job does not have any previous logs");
                    alert.showAndWait();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }catch (NullPointerException exception){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a FWO");
                alert.showAndWait();
            }
        });


        endJob.setOnAction(e -> {
            if (!openJobsList.getSelectionModel().isEmpty()){
                WeldJobHolder.setWeldJob(openJobsList.getSelectionModel().getSelectedItem());
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a job to edit");
                alert.showAndWait();
                return;
            }
            Stage window = (Stage) endJob.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/FinalizeWeldJob.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        back.setOnAction(e ->{
            Stage window = (Stage) back.getScene().getWindow();
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
