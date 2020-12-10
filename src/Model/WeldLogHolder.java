package Model;

public class WeldLogHolder {

    private static WeldLog weldLog;
    public static void setWeldLog(WeldLog pass){
        weldLog = pass;
    }
    public static WeldLog getWeldLog(){
        return weldLog;
    }

}