package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {

    private static class GPSIMUHolder{
        private static GPSIMU instance = new GPSIMU();
    }

    int currentTick;
    STATUS status;
    List<Pose> poses;

    public void tick(){
        currentTick++;
    }

    public void setStatus(STATUS status){
        this.status = status;
    }

    public static GPSIMU getInstance(){
        return GPSIMUHolder.instance;
    }

    public void addPose(Pose pose){
        poses.add(pose);
    }

    public Pose getCurrentPose(){
        return poses.get(currentTick);
    }

    public int getTick(){
        return currentTick;
    }


}
