package Model;

public class Part {

    private int partId;
    private String partName;
    private String heatNum;
    private String serialNum;
    private String wmsProcess;


    public Part(int partId, String partName, String heatNum, String serialNum, String wmsProcess) {
        this.partId = partId;
        this.partName = partName;
        this.heatNum = heatNum;
        this.serialNum = serialNum;
        this.wmsProcess = wmsProcess;
    }


    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getHeatNum() {
        return heatNum;
    }

    public void setHeatNum(String heatNum) {
        this.heatNum = heatNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getWmsProcess() {
        return wmsProcess;
    }

    public void setWmsProcess(String wmsProcess) {
        this.wmsProcess = wmsProcess;
    }
}
