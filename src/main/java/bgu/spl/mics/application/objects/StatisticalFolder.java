package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int systemRunTime;
    private int numDetectedObjects;
    private int numTrackedObjects;
    private int numLandmarks;

    static StatisticalFolder instance = null;

    public StatisticalFolder() {
        systemRunTime = 0;
        numDetectedObjects = 0;
        numTrackedObjects = 0;
        numLandmarks = 0;
    }
    public int getSystemRunTime() {
        return systemRunTime;
    }
    public int getNumDetectedObjects() {
        return numDetectedObjects;
    }
    public int getNumTrackedObjects() {
        return numTrackedObjects;
    }
    public int getNumLandmarks() {
        return numLandmarks;
    }
    public void incrementSystemRunTime() {
        systemRunTime++;
    }
    public void incrementNumDetectedObjects(int num) {
        numDetectedObjects+=num;
    }   
    public void incrementNumTrackedObjects() {
        numTrackedObjects++;
    }
    public void incrementNumLandmarks() {
        numLandmarks++;
    }

    public static StatisticalFolder getInstance() {
        if (instance == null) {
            instance = new StatisticalFolder();
        }
        return instance;
    }

}