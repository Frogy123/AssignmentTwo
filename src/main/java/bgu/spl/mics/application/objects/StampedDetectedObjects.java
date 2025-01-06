package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {
    private int stampTime;
    private List<DetectedObject> detectedObjects;
    public StampedDetectedObjects(int stampTime) {
        this.stampTime=stampTime;
        detectedObjects = new ArrayList<DetectedObject>();
    }
    public void addDetectedObject(DetectedObject detectedObject) {
        detectedObjects.add(detectedObject);
    }
    public int getTime() {
        return stampTime;
    }
    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }

    public int getNumOfDetectedObjects() {
        return detectedObjects.size();
    }
}