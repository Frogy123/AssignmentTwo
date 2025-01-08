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

    List<Pose> poses;
    int currentTick;
    STATUS status;


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

    public void setPoses(List<Pose> posesToInitialiaze) {
        this.poses = posesToInitialiaze;
    }

    public int getPosesSize(){
        return poses.size();
    }

    public int getTick(){
        return currentTick;
    }


}
