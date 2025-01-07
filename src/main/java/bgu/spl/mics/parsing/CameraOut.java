package bgu.spl.mics.parsing;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

public class CameraOut {
    String Name;
        StampedDetectedObjects detectedObjects;

    public CameraOut(Camera c) {
        this.Name = c.getKey();
        this.detectedObjects = c.getDetectedObjectsList().get(c.getDetectedObjectsList().size()-1);
    }
}
