package Controller;

import Model.DBMSConnection;
import Model.WeldLog;
import Model.WeldLogHolder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class EditLogManagementController implements Initializable {


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
    @FXML public Button commit;
    @FXML public Button cancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        wms.setText(WeldLogHolder.getWeldLog().getwms());
        rev.setText(WeldLogHolder.getWeldLog().getRev());
        machNum.setText(WeldLogHolder.getWeldLog().getMachNo());
        sizeAndType.setText(WeldLogHolder.getWeldLog().getSizeAndType());
        heatAndOrLot.setText(WeldLogHolder.getWeldLog().getHeatAndLot());
        noOut.setText(WeldLogHolder.getWeldLog().getNoOut());
        noIn.setText(WeldLogHolder.getWeldLog().getNoIn());
        stubs.setText(WeldLogHolder.getWeldLog().getStubs());
        timeOut.setText(WeldLogHolder.getWeldLog().getTimeOut());
        timeIn.setText(WeldLogHolder.getWeldLog().getTimeIn());
        qualificationNumber.setText(WeldLogHolder.getWeldLog().getQualificationNum());
        qualificationExpDate.setText(WeldLogHolder.getWeldLog().getQualificationExpDate());

        commit.setOnAction(e ->{
            DBMSConnection connection = new DBMSConnection();
            String updateStatement = String.format("UPDATE WELD_LOG SET WMS = '%s', MACH_NO = '%s', FILLER_SIZE_AND_TYPE = '%s', FILLER_HEAT_AND_OR_LOT = '%s', NO_OUT = '%s', NO_IN = '%s'" +
                            ", STUBS = '%s', TIME_OUT = '%s', TIME_IN = '%s', QUALIFICATION_NUM = '%s', QUALIFICATION_EXP_DATE = '%s' WHERE LOG_ID = %d", wms.getText(), machNum.getText(),
                    sizeAndType.getText(), heatAndOrLot.getText(), noOut.getText(), noIn.getText(), stubs.getText(), timeOut.getText(), timeIn.getText(), qualificationNumber.getText(), qualificationExpDate.getText(), WeldLogHolder.getWeldLog().getLogId());
            WeldLogHolder.setWeldLog(new WeldLog(wms.getText(), rev.getText(), machNum.getText(),
                    sizeAndType.getText(), heatAndOrLot.getText(), noOut.getText(), noIn.getText(), stubs.getText(), timeOut.getText(), timeIn.getText(), qualificationNumber.getText(), qualificationExpDate.getText(), false, false, true, WeldLogHolder.getWeldLog().getLogId()));
            try {
                Statement statement = connection.getConnection().createStatement();
                statement.executeUpdate(updateStatement);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            Stage window = (Stage) commit.getScene().getWindow();
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

        cancel.setOnAction(e ->{
            Stage window = (Stage) commit.getScene().getWindow();
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

    }
}
