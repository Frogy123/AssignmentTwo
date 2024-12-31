package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

public class PoseEvent implements Event<Pose> {
    Pose currentPose;
    int time;

    public PoseEvent(Pose currentPose, int time){
        this.currentPose = currentPose;
        this.time = time;
    }

    public Pose getPose(){
        return currentPose;
    }

    public int getTime(){
        return time;
    }

}