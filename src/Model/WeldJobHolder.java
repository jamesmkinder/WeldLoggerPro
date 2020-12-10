package Model;

public class WeldJobHolder {
        private static WeldJob weldJob;
    public static void setWeldJob(WeldJob pass) {
        weldJob = pass;
    }
    public static WeldJob getWeldJob(){
            return weldJob;
        }

    }

