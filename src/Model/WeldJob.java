package Model;

import java.util.Date;

public class WeldJob {

    private String fwo;
    private String welderClockNum;
    private String size;
    private String figNum;
    private String rmc;
    private String qapRtNum;
    private String material;
    private Boolean rework;
    private Date dateEntered;
    private Boolean ended;
    private String department;
    private int jobId;

    public WeldJob(String fwo, String welderClockNum, String size, String figNum, String rmc, String qapRtNum, String material, Boolean rework, Date dateEntered, Boolean ended, String department, int jobId){
        this.fwo = fwo;
        this.welderClockNum = welderClockNum;
        this.size = size;
        this.figNum = figNum;
        this.rmc = rmc;
        this.qapRtNum = qapRtNum;
        this.rework = rework;
        this.dateEntered = dateEntered;
        this.ended = ended;
        this.department = department;
        this.jobId = jobId;
        this.material = material;
    }

    public String getFwo() {
        return fwo;
    }

    public void setFwo(String fwo) {
        this.fwo = fwo;
    }

    public String getWelderClockNum() {
        return welderClockNum;
    }

    public void setWelderClockNum(String welderClockNum) {
        this.welderClockNum = welderClockNum;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFigNum() {
        return figNum;
    }

    public void setFigNum(String figNum) {
        this.figNum = figNum;
    }

    public String getRmc() {
        return rmc;
    }

    public void setRmc(String rmc) {
        this.rmc = rmc;
    }

    public String getQapRtNum() {
        return qapRtNum;
    }

    public void setQapRtNum(String qapRtNum) {
        this.qapRtNum = qapRtNum;
    }

    public Boolean getRework() {
        return rework;
    }

    public void setRework(Boolean rework) {
        this.rework = rework;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
