package bgu.spl.mics.application.objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    // Singleton instance holder
    private static class FusionSlamHolder {
        // TODO: Implement singleton instance logic.
        private static final FusionSlam instance = new FusionSlam();
    }

    /**FIELDS*/

    List<LandMark> landmarksList;
    List<Pose> posesList;


    /**CONSTRUCTORS*/
    public FusionSlam() {
        this.landmarksList = new ArrayList<LandMark>();
        this.posesList = new ArrayList<Pose>();
    }


    /**
     * Retrieves the singleton instance of FusionSlam.
     *
     * @return The FusionSlam instance.
     */
    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    //getters and setters
    public List<LandMark> getLandmarksList() {
        return landmarksList;
    }


    public List<Pose> getPosesList() {
        return posesList;
    }



    public int getPoseTime(){
        return posesList.size();
    }

    /**
     *
     *
     * @param point
     * @param time
     * @return
     */
    //static methods
    public static CloudPoint tranformToGlobalCoordinates(CloudPoint point, int time){
        Pose corespondingPose = FusionSlam.getInstance().getPosesList().get(time);
        float yawRad = (float) Math.toRadians(corespondingPose.getYaw());
        float cosYaw = (float) Math.cos(yawRad);
        float sinYaw = (float) Math.sin(yawRad);
        float xGlobal = (float) (point.getX() * cosYaw - point.getY() * sinYaw + corespondingPose.getX());
        float yGlobal = (float) (point.getX() * sinYaw + point.getY() * cosYaw + corespondingPose.getY());

        return new CloudPoint(xGlobal, yGlobal);

    }

    public static List<CloudPoint> tranformToGlobalCoordinate(TrackedObject t){
        List<CloudPoint> localPoints = t.getCoordinates();
        List<CloudPoint> globalPoints = new ArrayList<CloudPoint>();
        for (int i = 0; i < localPoints.size(); i++) {
            globalPoints.add(tranformToGlobalCoordinates(localPoints.get(i), t.getTime()));
        }
        return globalPoints;
    }
}
