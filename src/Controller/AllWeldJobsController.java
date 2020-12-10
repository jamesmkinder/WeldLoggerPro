package Controller;

import Model.DBMSConnection;
import Model.Part;
import Model.WeldJob;
import Model.WeldLog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AllWeldJobsController implements Initializable {

    @FXML public TextField search;
    @FXML public TableColumn<WeldJob, String> clockNum;
    @FXML public TableColumn<WeldJob, String> size;
    @FXML public TableColumn<WeldJob, String> figNum;
    @FXML public TableColumn<WeldJob, String> fwo;
    @FXML public TableColumn<WeldJob, String> rmc;
    @FXML public TableColumn<WeldJob, String> qaprtNum;
    @FXML public TableColumn<WeldJob, String> ended;
    @FXML public TableView<WeldJob> weldJobsTable;
    @FXML public TableColumn<WeldLog, String> wms;
    @FXML public TableColumn<WeldLog, String> rev;
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
    @FXML public TableView<WeldLog> weldLogTable;
    @FXML public TableView<Part> partsTable;
    @FXML public TableColumn<Part, String> partNameCol;
    @FXML public TableColumn<Part, String> heatNumCol;
    @FXML public TableColumn<Part, String> serialNumCol;
    @FXML public TableColumn<Part, String> wmsCol;
    @FXML public Button back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DBMSConnection connection = new DBMSConnection();
        String weldJobQuery = "SELECT FWO, WELDER_CLOCK_NUM, SIZE, FIG_NUM, RMC, QAP_RT_NUM, MATERIAL, REWORK, DATE_ENTERED, ENDED, DEPARTMENT, JOB_ID FROM WELD_JOBS";
        ObservableList<WeldJob> weldJobsList = FXCollections.observableArrayList();
        try {
            Statement weldJobs = connection.getConnection().createStatement();
            ResultSet rs = weldJobs.executeQuery(weldJobQuery);
            while (rs.next()){
                WeldJob newJob = new WeldJob(rs.getString("FWO"), rs.getString("WELDER_CLOCK_NUM"), rs.getString("SIZE"),
                        rs.getString("FIG_NUM"), rs.getString("RMC"), rs.getString("QAP_RT_NUM"), rs.getString("MATERIAL"), rs.getBoolean("REWORK"),
                        rs.getDate("DATE_ENTERED"), rs.getBoolean("ENDED"), rs.getString("DEPARTMENT"), rs.getInt("JOB_ID"));
                weldJobsList.add(newJob);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        weldJobsTable.getItems().setAll(weldJobsList);

        clockNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getWelderClockNum());
            }
        });

        size.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getSize());
            }
        });

        figNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getFigNum());
            }
        });

        fwo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getFwo());
            }
        });

        rmc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getRmc());
            }
        });

        qaprtNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                return new SimpleStringProperty(p.getValue().getQapRtNum());
            }
        });

        ended.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldJob, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldJob, String> p) {
                if (p.getValue().getEnded()){
                    return new SimpleStringProperty("Yes");
                }
                else return new SimpleStringProperty("No");
            }
        });

        FilteredList<WeldJob> searchedJobs = new FilteredList<>(weldJobsList, p -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->{
            searchedJobs.setPredicate(searchedForJob ->{
                if (newValue.isBlank()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return searchedForJob.getFwo().contains(lowerCaseFilter) || searchedForJob.getWelderClockNum().contains(lowerCaseFilter);
            });
        });
        SortedList<WeldJob> sortedWeldJobs = new SortedList<>(searchedJobs);
        sortedWeldJobs.comparatorProperty().bind(weldJobsTable.comparatorProperty());
        weldJobsTable.setItems(sortedWeldJobs);


        wms.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getwms());
            }
        });

        rev.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WeldLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WeldLog, String> p) {
                return new SimpleStringProperty(p.getValue().getRev());
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



        weldJobsTable.setOnMouseClicked(e ->{
            ObservableList<WeldLog> associatedWeldLogs = FXCollections.observableArrayList();
            ObservableList<Part> partsList = FXCollections.observableArrayList();
            String logsQuery = "SELECT * FROM WELD_LOG WHERE JOB_ID = " + weldJobsTable.getSelectionModel().getSelectedItem().getJobId();
            String partsQuery = "SELECT * FROM PARTS WHERE JOB_ID = " + weldJobsTable.getSelectionModel().getSelectedItem().getJobId();
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
                rs = statement.executeQuery(partsQuery);
                while (rs.next()) {
                    Part newPart = new Part(rs.getInt("PART_ID"), rs.getString("PART_NAME"), rs.getString("HEAT_NUM"), rs.getString("SERIAL_NUM"), rs.getString("WMS_PROCESS"));
                    partsList.add(newPart);
                }
                partsTable.setItems(partsList);
                statement.close();
            } catch (SQLException exception) {
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
