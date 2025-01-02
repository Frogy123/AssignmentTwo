package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

public class DetectedObjectsEvent implements Event {
    private StampedDetectedObjects stampedObjects;

    // Constructor
    public DetectedObjectsEvent(StampedDetectedObjects stampedObjects) {
        this.stampedObjects = stampedObjects;
    }

    // Getter
    public StampedDetectedObjects getStampedObjects() {
        return stampedObjects;
    }

    // Setter
    public void setStampedObjects(StampedDetectedObjects stampedObjects) {
        this.stampedObjects = stampedObjects;
    }
}