package Model;

public class CertContinuity {

    private String clockNum;
    private String wms;
    private String date;


    public CertContinuity(String clockNum, String wms, String date) {
        this.clockNum = clockNum;
        this.wms = wms;
        this.date = date;
    }

    public String getClockNum() {
        return clockNum;
    }

    public void setClockNum(String clockNum) {
        this.clockNum = clockNum;
    }

    public String getWms() {
        return wms;
    }

    public void setWms(String wms) {
        this.wms = wms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
