package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CameraOut {
    String Name;
    List<StampedDetectedObjects> detectedObjects;

    public CameraOut(Camera c) {
        this.Name = c.getKey();
        this.detectedObjects = c.getDetectedObjectsList();
    }
}
