package Controller;

import Model.*;
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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {

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
    @FXML public Button logOut;
    @FXML public Button editWeldJob;
    @FXML public Button deleteWeldJob;
    @FXML public CategoryAxis xAxis;
    @FXML public NumberAxis yAxis;
    @FXML public LineChart<String, Double> fpyChart;
    @FXML public ComboBox<String> welderClockNum;
    @FXML public ComboBox<String> material;
    @FXML public ComboBox<String> department;
    @FXML public LineChart<String, Number> logisticChart;
    @FXML public CategoryAxis reworkAxis;
    @FXML public NumberAxis passFailAxis;
    @FXML public PieChart pieChart;
    @FXML public ComboBox<String> category;
    @FXML public TableView<CertContinuity> certContinuityTable;
    @FXML public TableColumn<CertContinuity, String> clockNumWelderContinuity;
    @FXML public TableColumn<CertContinuity, String> wmsWelderContinuity;
    @FXML public TableColumn<CertContinuity, String> mostRecentDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBMSConnection connection = new DBMSConnection();
        String welderClockNumQuery = "SELECT DISTINCT WELDER_CLOCK_NUM FROM WELD_JOBS";
        ObservableList<String> welders = FXCollections.observableArrayList();
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(welderClockNumQuery);
            while (rs.next()){
                welders.add(rs.getString(1));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        welderClockNum.getItems().setAll(welders);
        material.getItems().setAll("Base metal", "Stellite 21", "Stellite 6", "Stoody", "Norem", "Deloro 50", "Other");
        department.getItems().setAll("Forge Steel", "Small Cast", "Large Cast");
        category.getItems().setAll("Material", "Department", "Welder");


        XYChart.Series<String, Number> logisticChartPoints = new XYChart.Series<>();
        XYChart.Series<String, Number> logisticChartRegression = new XYChart.Series<>();
        logisticChartPoints.setName("Pass/Fail Points");
        logisticChartRegression.setName("Logistic Regression");
        reworkAxis.setLabel("Number of Corresponding Records in the Rework Table");
        passFailAxis.setLabel("Passed or Rejected");
        String logisticChartPointsQuery = "SELECT IIF(REJECT_TICKET_ID IS NULL, 1, 0), (SELECT COUNT(*) FROM REJECT_TICKETS R WHERE R.ORIGINAL_WO_NUM = J.FWO), MATERIAL FROM WELD_JOBS J INNER JOIN PARTS P ON J.JOB_ID = P.JOB_ID ORDER BY 2";


        XYChart.Series<String, Double> totalFpy = new XYChart.Series<>();
        XYChart.Series<String, Double> reworkFpy = new XYChart.Series<>();
        XYChart.Series<String, Double> fpyByWelder = new XYChart.Series<>();
        XYChart.Series<String, Double> fpyByMaterial = new XYChart.Series<>();
        XYChart.Series<String, Double> fpyByDepartment = new XYChart.Series<>();
        xAxis.setLabel("Month");
        totalFpy.setName("All Parts");
        reworkFpy.setName("Rework Parts");
        fpyByWelder.setName("FPY for selected welder");
        fpyByMaterial.setName("FPY for selected material");
        fpyByDepartment.setName("FPY for selected department");

        String totalFpyQuery = "SELECT CDbl(CDbl((CDbl((Count(*)-Count(REJECT_TICKET_ID)))/Count(*)))*100), Month(DATE_ENTERED) FROM PARTS P INNER JOIN WELD_JOBS J ON P.JOB_ID = J.JOB_ID WHERE YEAR(DATE_ENTERED) = YEAR(Date()) " +
                "GROUP BY Month(DATE_ENTERED) ORDER BY MONTH(DATE_ENTERED)";
        String reworkFpyQuery = "SELECT CDbl(CDbl((CDbl((Count(*)-Count(REJECT_TICKET_ID)))/Count(*)))*100), Month(DATE_ENTERED) FROM PARTS P INNER JOIN WELD_JOBS J ON P.JOB_ID = J.JOB_ID WHERE J.REWORK = 1 " +
                "AND YEAR(DATE_ENTERED) = YEAR(Date()) GROUP BY Month(DATE_ENTERED) ORDER BY MONTH(DATE_ENTERED)";

        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(totalFpyQuery);
            while (rs.next()){
                totalFpy.getData().add(new XYChart.Data<>(rs.getString(2), rs.getDouble(1)));
            }
            rs = statement.executeQuery(reworkFpyQuery);
            while (rs.next()){
                reworkFpy.getData().add(new XYChart.Data<>(rs.getString(2), rs.getDouble(1)));
            }
            while (rs.next()){
                fpyByDepartment.getData().add(new XYChart.Data<>(rs.getString(2), rs.getDouble(1)));
            }

            rs = statement.executeQuery(logisticChartPointsQuery);
            while (rs.next()){
                logisticChartPoints.getData().add(new XYChart.Data<String, Number>(Integer.toString(rs.getInt(2)), rs.getInt(1)));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        int i;
        double b0 = -2.0, b1 = -0.01;

        ObservableList<Double> probability = FXCollections.observableArrayList();
        for (i=0; i<logisticChartPoints.getData().size(); i++){
            probability.add(1/(1+Math.exp(b0 + b1 * Integer.parseInt(logisticChartPoints.getData().get(i).getXValue()))));
        }
        ObservableList<Double> logLikelihood = FXCollections.observableArrayList();
        for (i=0; i<probability.size(); i++){
            logLikelihood.add(Math.log(probability.get(i)));
        }

        for (i=0; i<logLikelihood.size(); i++){
            XYChart.Data<String, Number> newDataPoint = new XYChart.Data<String, Number>(logisticChartPoints.getData().get(i).getXValue(), probability.get(i));
            logisticChartRegression.getData().add(newDataPoint);
        }

        logisticChart.getData().addAll(logisticChartPoints, logisticChartRegression);
        logisticChart.lookup(".default-color0.chart-series-line").setStyle("-fx-stroke: transparent");

        fpyChart.getData().addAll(totalFpy, reworkFpy, fpyByMaterial, fpyByWelder, fpyByDepartment);
        fpyChart.setAnimated(false);

        welderClockNum.setOnAction(e ->{
            try {
                fpyByWelder.getData().clear();
                String fpyQuery = "SELECT CDbl(CDbl((CDbl((Count(*)-Count(REJECT_TICKET_ID)))/Count(*)))*100), Month(DATE_ENTERED) FROM PARTS P INNER JOIN WELD_JOBS J ON P.JOB_ID = J.JOB_ID WHERE J.WELDER_CLOCK_NUM = '" +
                        welderClockNum.getSelectionModel().getSelectedItem() + "' AND YEAR(DATE_ENTERED) = YEAR(Date()) GROUP BY Month(DATE_ENTERED) ORDER BY MONTH(DATE_ENTERED)";
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(fpyQuery);
                while (rs.next()){
                    fpyByWelder.getData().add(new XYChart.Data<String, Double>(rs.getString(2), rs.getDouble(1)));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        material.setOnAction(e ->{
            try {
                fpyByMaterial.getData().clear();
                String fpyQuery = "SELECT CDbl(CDbl((CDbl((Count(*)-Count(REJECT_TICKET_ID)))/Count(*)))*100), Month(DATE_ENTERED) FROM PARTS P INNER JOIN WELD_JOBS J ON P.JOB_ID = J.JOB_ID WHERE J.MATERIAL = '" +
                        material.getSelectionModel().getSelectedItem() + "' AND YEAR(DATE_ENTERED) = YEAR(Date()) GROUP BY  Month(DATE_ENTERED) ORDER BY MONTH(DATE_ENTERED)";
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(fpyQuery);
                while (rs.next()){
                    fpyByMaterial.getData().add(new XYChart.Data<String, Double>(rs.getString(2), rs.getDouble(1)));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        department.setOnAction(e ->{
            try{
            fpyByDepartment.getData().clear();
            String fpyQuery = "SELECT CDbl(CDbl((CDbl((Count(*)-Count(REJECT_TICKET_ID)))/Count(*)))*100), Month(DATE_ENTERED) FROM PARTS P INNER JOIN WELD_JOBS J ON P.JOB_ID = J.JOB_ID WHERE J.DEPARTMENT = '" +
                    department.getSelectionModel().getSelectedItem() + "' AND YEAR(DATE_ENTERED) = YEAR(Date()) GROUP BY  Month(DATE_ENTERED) ORDER BY MONTH(DATE_ENTERED)";
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(fpyQuery);
            while (rs.next()){
                fpyByDepartment.getData().add(new XYChart.Data<String, Double>(rs.getString(2), rs.getDouble(1)));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        });

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

        category.setOnAction(e->{
            if (category.getSelectionModel().getSelectedItem() == "Material"){
                String pieChartQuery = "SELECT MATERIAL, COUNT(REJECT_TICKET_ID) FROM PARTS INNER JOIN WELD_JOBS ON PARTS.JOB_ID = WELD_JOBS.JOB_ID GROUP BY MATERIAL";
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                try {
                    Statement statement = connection.getConnection().createStatement();
                    ResultSet rs = statement.executeQuery(pieChartQuery);
                    while (rs.next()){
                        PieChart.Data newPieSlice = new PieChart.Data(rs.getString(1), rs.getInt(2));
                        pieChartData.add(newPieSlice);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                pieChart.setData(pieChartData);
            }
            else if (category.getSelectionModel().getSelectedItem() == "Department"){
                String pieChartQuery = "SELECT DEPARTMENT, COUNT(REJECT_TICKET_ID) FROM PARTS INNER JOIN WELD_JOBS ON PARTS.JOB_ID = WELD_JOBS.JOB_ID GROUP BY DEPARTMENT";
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                try {
                    Statement statement = connection.getConnection().createStatement();
                    ResultSet rs = statement.executeQuery(pieChartQuery);
                    while (rs.next()){
                        PieChart.Data newPieSlice = new PieChart.Data(rs.getString(1), rs.getInt(2));
                        pieChartData.add(newPieSlice);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                pieChart.setData(pieChartData);
            }
            else if (category.getSelectionModel().getSelectedItem() == "Welder"){
                String pieChartQuery = "SELECT WELDER_CLOCK_NUM, COUNT(REJECT_TICKET_ID) FROM PARTS INNER JOIN WELD_JOBS ON PARTS.JOB_ID = WELD_JOBS.JOB_ID GROUP BY WELDER_CLOCK_NUM";
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                try {
                    Statement statement = connection.getConnection().createStatement();
                    ResultSet rs = statement.executeQuery(pieChartQuery);
                    while (rs.next()){
                        PieChart.Data newPieSlice = new PieChart.Data(rs.getString(1), rs.getInt(2));
                        pieChartData.add(newPieSlice);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                pieChart.setData(pieChartData);
            }
        });

        String certContinuityQuery = "SELECT WELDER_CLOCK_NUM, WMS, MOST_RECENT_DATE FROM WELDER_CONTINUITY";
        ObservableList<CertContinuity> certs = FXCollections.observableArrayList();
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(certContinuityQuery);
            while (rs.next()){
                CertContinuity newCert = new CertContinuity(rs.getString("WELDER_CLOCK_NUM"), rs.getString("WMS"), rs.getString("MOST_RECENT_DATE"));
                certs.add(newCert);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        certContinuityTable.getItems().addAll(certs);

        clockNumWelderContinuity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CertContinuity, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CertContinuity, String> p) {
                return new SimpleStringProperty(p.getValue().getClockNum());
            }
        });

        wmsWelderContinuity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CertContinuity, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CertContinuity, String> p) {
                return new SimpleStringProperty(p.getValue().getWms());
            }
        });

        mostRecentDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CertContinuity, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CertContinuity, String> p) {
                return new SimpleStringProperty(p.getValue().getDate());
            }
        });

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

        FilteredList<CertContinuity> searchedWelderContinuity = new FilteredList<>(certs, p -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->{
            searchedWelderContinuity.setPredicate(searchedForWelder ->{
                if (newValue.isBlank()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return searchedForWelder.getClockNum().contains(lowerCaseFilter);
            });
        });
        SortedList<CertContinuity> sortedCertContinuityList = new SortedList<>(searchedWelderContinuity);
        sortedCertContinuityList.comparatorProperty().bind(certContinuityTable.comparatorProperty());
        certContinuityTable.setItems(sortedCertContinuityList);



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

        deleteWeldJob.setOnAction(e ->{
            try {
                String deleteStatement = "DELETE FROM WELD_JOBS WHERE JOB_ID = " + weldJobsTable.getSelectionModel().getSelectedItem().getJobId();
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(deleteStatement);

                weldJobsList.clear();
                certs.clear();
                ResultSet rs = statement.executeQuery(weldJobQuery);
                while (rs.next()){
                    WeldJob newJob = new WeldJob(rs.getString("FWO"), rs.getString("WELDER_CLOCK_NUM"), rs.getString("SIZE"),
                            rs.getString("FIG_NUM"), rs.getString("RMC"), rs.getString("QAP_RT_NUM"), rs.getString("MATERIAL"), rs.getBoolean("REWORK"),
                            rs.getDate("DATE_ENTERED"), rs.getBoolean("ENDED"), rs.getString("DEPARTMENT"), rs.getInt("JOB_ID"));
                    weldJobsList.add(newJob);
                }
                rs = statement.executeQuery(certContinuityQuery);
                while (rs.next()){
                    CertContinuity newCert = new CertContinuity(rs.getString("WELDER_CLOCK_NUM"), rs.getString("WMS"), rs.getString("MOST_RECENT_DATE"));
                    certs.add(newCert);
                }
                certContinuityTable.setItems(sortedCertContinuityList);
                weldJobsTable.setItems(sortedWeldJobs);
                weldLogTable.getItems().clear();
                partsTable.getItems().clear();

            } catch (SQLException exception){
                exception.printStackTrace();
            }
        });

        editWeldJob.setOnAction(e ->{
            if (!weldJobsTable.getSelectionModel().isEmpty()){
                WeldJobHolder.setWeldJob(weldJobsTable.getSelectionModel().getSelectedItem());
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a job to edit");
                alert.showAndWait();
                return;
            }
            Stage window = (Stage) editWeldJob.getScene().getWindow();
            window.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/EditWeldJob.fxml"));
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        logOut.setOnAction(e ->{
            Stage window = (Stage) logOut.getScene().getWindow();
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