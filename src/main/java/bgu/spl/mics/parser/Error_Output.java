package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.*;

import java.util.List;

public class Error_Output implements Out {

    String Error;
    String faultySensor;

    List<CameraOut> LastFrame;
    //Lidar
    List<Pose> poses;
    StatisticalFolder statisticalFolder;

    public Error_Output(String _Error, String _faultySensor, FusionSlam fs){
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.LastFrame = Camera.getCameras();
        this.poses = fs.getPosesList();
        this.Error = _Error;
        this.faultySensor = _faultySensor;
    }




}
