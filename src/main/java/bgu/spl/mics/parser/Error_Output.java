package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.*;

import java.util.List;

public class Error_Output implements Out {

    String Error;
    List<String> faultySensor;
    List<StampedDetectedObjects> detectedObjects;
    //Lidar
    List<Pose> poses;
    StatisticalFolder statisticalFolder;

    public Error_Output(FusionSlam fs){
        for(Camera c: Camera.getCameras()){
            if(c.getFaulty()){
                faultySensor.add(c.getSensorName());
            }
        }

    }




}
