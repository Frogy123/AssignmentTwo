package bgu.spl.mics.parsing;

import bgu.spl.mics.application.objects.*;
import java.util.ArrayList;
import java.util.List;

public class Error_Output implements Out {

    String Error;
    String faultySensor;
    List<CameraOut> LastFrame;
    List<LidarOut> LidarLastFrame;
    List<Pose> poses;
    StatisticalFolder statisticalFolder;
    List<LandMark> landMarks;

    static Error_Output instance;


    public Error_Output(){
        LastFrame = new ArrayList<>();
        LidarLastFrame = new ArrayList<>();
        landMarks = new ArrayList<>();
    }

    public void writeError(String _Error, String _faultySensor, FusionSlam fs){
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.poses = fs.getPosesList();
        this.Error = _Error;
        this.faultySensor = _faultySensor;
        landMarks = fs.getLandmarksList();
    }

    public static Error_Output getInstance(){
        if(instance == null){
            instance = new Error_Output();
        }
        return instance;
    }


    public void addCameraLastFrame(Camera c, int time){
        LastFrame.add(new CameraOut(c, time));
    }
    public void addLidarLastFrame(LiDarWorkerTracker l, List<TrackedObject> lastFrame){
        LidarLastFrame.add(new LidarOut(l, lastFrame));
    }




}
