package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.Camera;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CameraParser {
    @SerializedName("CamerasConfigurations")
    List<Camera> cameraList;
    @SerializedName("camera_datas_path")
    String cameraDataPath;

    public List<Camera> getCameraList() {
        return cameraList;
    }

    public String getCameraDataPath() {
        return cameraDataPath;
    }



}
