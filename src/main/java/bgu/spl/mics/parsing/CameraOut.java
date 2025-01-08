package bgu.spl.mics.parsing;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedCloudPoints;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

public class CameraOut {
    String Name;
        StampedDetectedObjects detectedObjects;

    public CameraOut(Camera c, int time) {
        this.Name = c.getKey();
        this.detectedObjects = this.getDetectedObjectsByTime(c.getDetectedObjectsList(), time);
    }

    public StampedDetectedObjects getDetectedObjectsByTime(List<StampedDetectedObjects> list, int time) {
        StampedDetectedObjects val = list.get(0);
        for(StampedDetectedObjects s : list) {
            if(s.getTime() < time && s.getTime() > val.getTime()) {
                val = s;
            }
        }
        return val;
    }
}
