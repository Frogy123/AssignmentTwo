package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.*;

import java.util.List;

public class Error_Output implements Out {

    String Error;
    List<String> faultySensor;

    List<CameraOut> LastFrame;
    //Lidar
    List<Pose> poses;
    StatisticalFolder statisticalFolder;

    public Error_Output(FusionSlam fs){
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.LastFrame = Camera.getCameras();
        this.poses = fs.getPosesList();
    }




}
