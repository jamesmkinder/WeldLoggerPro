package Controller;

import Model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
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

public class EditWeldJobController implements Initializable {


    @FXML public TextField clockNum;
    @FXML public CheckBox rework;
    @FXML public TextField size;
    @FXML public TextField figNum;
    @FXML public TextField fwo;
    @FXML public TextField rmc;
    @FXML public TextField qaprtNum;
    @FXML public ComboBox<String> department;
    @FXML public TableView<WeldLog> weldLogTable;
    @FXML public TableColumn<WeldLog, String> wms;
    @FXML public TableColumn<WeldLog, String> machNum;
    @FXML public TableColumn<WeldLog, String> sizeAndType;
    @FXML public TableColumn<WeldLog, String> heatAndLot;
    @FXML public TableColumn<WeldLog, String> noOut;
    @FXML public TableColumn<WeldLog, String> noIn;
    @FXML public TableColumn<WeldLog, String> stubs;
    @FXML public TableColumn<WeldLog, String> timeOut;
    @FXML public TableColumn<WeldLog, String> timeIn;
    @FXML public TableColumn<WeldLog, String> qualificationNum;
    @FXML public TableColumn<WeldLog, String> qualificationExpDate;
    @FXML public Button edit;
    @FXML public Button submit;
    @FXML public TextField partName;
    @FXML public TextField heatNum;
    @FXML public TextField serialNum;
    @FXML public TableColumn<Part, String> partNameCol;
    @FXML public TableColumn<Part, String> heatNumCol;
    @FXML public TableColumn<Part, String> serialNumCol;
    @FXML public TableColumn<Part, String> wmsCol;
    @FXML public Button addPart;
    @FXML public Button deletePart;
    @FXML public TableView<Part> partsTable;
    @FXML public Button deleteLog;
    @FXML public Button back;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBMSConnection connection = new DBMSConnection();
        department.getItems().setAll("Forged Steel", "Small Cast", "Large Cast");

        department.getSelectionModel().select(WeldJobHolder.getWeldJob().getDepartment());

        clockNum.setText(WeldJobHolder.getWeldJob().getWelderClockNum());
        if (WeldJobHolder.getWeldJob().getRework()){
            rework.setSelected(true);
        }

        size.setText(WeldJobHolder.getWeldJob().getSize());
        figNum.setText(WeldJobHolder.getWeldJob().getFigNum());
        fwo.setText(WeldJobHolder.getWeldJob().getFwo());
        rmc.setText(WeldJobHolder.getWeldJob().getRmc());
        qaprtNum.setText(WeldJobHolder.getWeldJob().getQapRtNum());

        ObservableList<WeldLog> associatedWeldLogs = FXCollections.observableArrayList();
        String logsQuery = String.format("SELECT * FROM WELD_LOG WHERE JOB_ID = %d", WeldJobHolder.getWeldJob().getJobId());
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(logsQuery);

            while (rs.next()){
                WeldLog newLog = new WeldLog(rs.getString("WMS"), rs.getString("REV"), rs.getString("MACH_NO"), rs.getString("FILLER_SIZE_AND_TYPE"), rs.getString("FILLER_HEAT_AND_OR_LOT"),
                        rs.getString("NO_OUT"), rs.getString("NO_IN"), rs.getString("STUBS"), rs.getString("TIME_OUT"), rs.getString("TIME_IN"),
                        rs.getString("QUALIFICATION_NUM"), rs.getString("QUALIFICATION_EXP_DATE"), rs.getBoolean("QA_CHECKED"), rs.getBoolean("FOREMAN_CHECKED"), rs.getBoolean("WPS_CHECK"), rs.getInt("LOG_ID"));
                associatedWeldLogs.add(newLog);
            }
            weldLogTable.getItems().setAll(associatedWeldLogs);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        wms.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getwms());
            }
        });

        machNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getMachNo());
            }
        });

        sizeAndType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getSizeAndType());
            }
        });

        heatAndLot.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getHeatAndLot());
            }
        });

        noOut.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getNoOut());
            }
        });

        noIn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getNoIn());
            }
        });

        stubs.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getStubs());
            }
        });

        timeOut.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getTimeOut());
            }
        });

        timeIn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getTimeIn());
            }
        });

        qualificationNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getQualificationNum());
            }
        });

        qualificationExpDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getQualificationExpDate());
            }
        });

        edit.setOnAction(e -> {
            WeldLogHolder.setWeldLog(weldLogTable.getSelectionModel().getSelectedItem());
            Stage window = (Stage) edit.getScene().getWindow();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/EditLogManagement.fxml"));
                window.close();
                Scene scene = new Scene(root);
                window.setScene(scene);
            } catch (LoadException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must select a weld log to edit.");
                alert.showAndWait();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            window.show();
        });


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

        try {
            Statement statement = connection.getConnection().createStatement();
            ObservableList<Part> partsList = FXCollections.observableArrayList();
            String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + WeldJobHolder.getWeldJob().getJobId();
            ResultSet rs = statement.executeQuery(partsQuery);
            while (rs.next()){
                Part newPart = new Part(rs.getInt("PART_ID"), rs.getString("PART_NAME"), rs.getString("HEAT_NUM"), rs.getString("SERIAL_NUM"), rs.getString("WMS_PROCESS"));
                partsList.add(newPart);
            }
            partsTable.setItems(partsList);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

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

            String insertStatement = String.format("INSERT INTO PARTS (JOB_ID, PART_NAME, HEAT_NUM, SERIAL_NUM) VALUES (%d, '%s', '%s', '%s')", WeldJobHolder.getWeldJob().getJobId(), partName.getText(),
                    heatNum.getText(), serialNum.getText());

            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(insertStatement);
                ObservableList<Part> partsList = FXCollections.observableArrayList();
                String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + WeldJobHolder.getWeldJob().getJobId();
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
            String deleteStatement = "DELETE FROM PARTS WHERE PART_ID = (SELECT PART_ID FROM PARTS WHERE JOB_ID = " + WeldJobHolder.getWeldJob().getJobId() + " AND PART_NAME = '" +
                    partsTable.getSelectionModel().getSelectedItem().getPartName() + "' AND HEAT_NUM = '" + partsTable.getSelectionModel().getSelectedItem().getHeatNum() + "' AND SERIAL_NUM = '" + partsTable.getSelectionModel().getSelectedItem().getSerialNum() + "')";
            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(deleteStatement);
                ObservableList<Part> partsList = FXCollections.observableArrayList();
                String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + WeldJobHolder.getWeldJob().getJobId();
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

        submit.setOnAction(e ->{
            int reworkYesNo = 0;
            if (rework.isSelected()){
                reworkYesNo = 1;
            }
            String endJobUpdate = String.format("UPDATE WELD_JOBS SET ENDED = 1, FWO = '%s', WELDER_CLOCK_NUM = '%s', SIZE = '%s', " +
                            "FIG_NUM = '%s', RMC = '%s', QAP_RT_NUM = '%s', REWORK = %d, DEPARTMENT = '%s' WHERE FWO = '%s' AND WELDER_CLOCK_NUM = '%s'", fwo.getText(), clockNum.getText(),
                    size.getText(), figNum.getText(), rmc.getText(), qaprtNum.getText(), reworkYesNo, department.getSelectionModel().getSelectedItem(),
                    WeldJobHolder.getWeldJob().getFwo(), WeldJobHolder.getWeldJob().getWelderClockNum());
            System.out.println(endJobUpdate);

            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(endJobUpdate);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            Stage window = (Stage) submit.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/Dashboard.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        deleteLog.setOnAction(e ->{
            associatedWeldLogs.clear();
            try {
                String deleteStatement = "DELETE FROM WELD_LOG WHERE LOG_ID = " + weldLogTable.getSelectionModel().getSelectedItem().getLogId();
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(deleteStatement);

                ResultSet rs = statement.executeQuery(logsQuery);
                while (rs.next()){
                    WeldLog newLog = new WeldLog(rs.getString("WMS"), rs.getString("REV"), rs.getString("MACH_NO"), rs.getString("FILLER_SIZE_AND_TYPE"), rs.getString("FILLER_HEAT_AND_OR_LOT"),
                            rs.getString("NO_OUT"), rs.getString("NO_IN"), rs.getString("STUBS"), rs.getString("TIME_OUT"), rs.getString("TIME_IN"),
                            rs.getString("QUALIFICATION_NUM"), rs.getString("QUALIFICATION_EXP_DATE"), rs.getBoolean("QA_CHECKED"), rs.getBoolean("FOREMAN_CHECKED"), rs.getBoolean("WPS_CHECK"), rs.getInt("LOG_ID"));
                    associatedWeldLogs.add(newLog);
                }
            }catch (NullPointerException exception){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a log to delete.");
                alert.showAndWait();
            }catch (SQLException exception){
                exception.printStackTrace();
            }
            weldLogTable.getItems().setAll(associatedWeldLogs);
        });

        back.setOnAction(e ->{
            Stage window = (Stage) back.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/Dashboard.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });



    }
}
