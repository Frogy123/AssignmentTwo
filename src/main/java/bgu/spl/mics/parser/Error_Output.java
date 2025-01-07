package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.*;
import java.util.ArrayList;
import java.util.List;

public class Error_Output implements Out {

    String Error;
    String faultySensor;
    List<CameraOut> LastFrame;
    //Lidar
    List<Pose> poses;
    StatisticalFolder statisticalFolder;
    static Error_Output instance;


    public Error_Output(){
        LastFrame = new ArrayList<>();
    }

    public void writeError(String _Error, String _faultySensor, FusionSlam fs){
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.poses = fs.getPosesList();
        this.Error = _Error;
        this.faultySensor = _faultySensor;
    }

    public static Error_Output getInstance(){
        if(instance == null){
            instance = new Error_Output();
        }
        return instance;
    }


    public void addCameraLastFrame(Camera c){
        LastFrame.add(new CameraOut(c));
    }




}
