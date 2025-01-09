package bgu.spl.mics.parsing;

import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

public class LidarOut   {

        String Name;
        List<TrackedObject> lastTrackedObjects;

        public LidarOut(LiDarWorkerTracker liDarWorkerTracker, List<TrackedObject> lastTrackedObjects) {
            this.Name = "LidarWorker" + liDarWorkerTracker.getId();
            this.lastTrackedObjects = lastTrackedObjects;
        }


}
