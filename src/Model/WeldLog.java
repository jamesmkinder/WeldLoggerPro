package Model;


public class WeldLog {

    private String wms;
    private String rev;
    private String machNo;
    private String sizeAndType;
    private String heatAndLot;
    private String noOut;
    private String noIn;
    private String stubs;
    private String timeOut;
    private String timeIn;
    private String qualificationNum;
    private String qualificationExpDate;
    private Boolean qaChecked;
    private Boolean foremanChecked;
    private Boolean wpsCheck;
    private int logId;


    public WeldLog(String wms, String rev, String machNo, String sizeAndType, String heatAndLot, String noOut, String noIn, String stubs, String timeOut, String timeIn, String qualificationNum, String qualificationExpDate, Boolean qaChecked, Boolean foremanChecked, Boolean wpsCheck, int logId) {
        this.wms = wms;
        this.rev = rev;
        this.machNo = machNo;
        this.sizeAndType = sizeAndType;
        this.heatAndLot = heatAndLot;
        this.noOut = noOut;
        this.noIn = noIn;
        this.stubs = stubs;
        this.timeOut = timeOut;
        this.timeIn = timeIn;
        this.qualificationNum = qualificationNum;
        this.qualificationExpDate = qualificationExpDate;
        this.qaChecked = qaChecked;
        this.foremanChecked = foremanChecked;
        this.wpsCheck = wpsCheck;
        this.logId = logId;
    }



    public String getwms() {
        return wms;
    }

    public void setwms(String wms) {
        this.wms = wms;
    }

    public String getMachNo() {
        return machNo;
    }

    public void setMachNo(String machNo) {
        this.machNo = machNo;
    }

    public String getSizeAndType() {
        return sizeAndType;
    }

    public void setSizeAndType(String sizeAndType) {
        this.sizeAndType = sizeAndType;
    }

    public String getHeatAndLot() {
        return heatAndLot;
    }

    public void setHeatAndLot(String heatAndLot) {
        this.heatAndLot = heatAndLot;
    }

    public String getNoOut() {
        return noOut;
    }

    public void setNoOut(String noOut) {
        this.noOut = noOut;
    }

    public String getNoIn() {
        return noIn;
    }

    public void setNoIn(String noIn) {
        this.noIn = noIn;
    }

    public String getStubs() {
        return stubs;
    }

    public void setStubs(String stubs) {
        this.stubs = stubs;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getQualificationNum() {
        return qualificationNum;
    }

    public void setQualificationNum(String qualificationNum) {
        this.qualificationNum = qualificationNum;
    }

    public String getQualificationExpDate() {
        return qualificationExpDate;
    }

    public void setQualificationExpDate(String qualificationExpDate) {
        this.qualificationExpDate = qualificationExpDate;
    }

    public Boolean getQaChecked() {
        return qaChecked;
    }

    public void setQaChecked(Boolean qaChecked) {
        this.qaChecked = qaChecked;
    }

    public Boolean getForemanChecked() {
        return foremanChecked;
    }

    public void setForemanChecked(Boolean foremanChecked) {
        this.foremanChecked = foremanChecked;
    }

    public Boolean getWpsCheck() {
        return wpsCheck;
    }

    public void setWpsCheck(Boolean wpsCheck) {
        this.wpsCheck = wpsCheck;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
