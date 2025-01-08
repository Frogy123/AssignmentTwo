package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.LinkedList;
import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 *
 * This service interacts with the LiDarTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {
    private LiDarWorkerTracker lidarTracker;
    /**
     * Constructor for LiDarService.
     *
     * @param liDarTracker The LiDAR tracker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarTracker) {
        super("LiDarService");
        this.lidarTracker=liDarTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {

        subscribeEvent(DetectedObjectsEvent.class, (DetectedObjectsEvent t) -> {
            lidarTracker.trackObjects(t.getStampedObjects());
        });

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {
            List<TrackedObject> trackedObjectsToSend = new LinkedList<>();
            for (TrackedObject trackedObject : lidarTracker.getLastTrackedObjects()){
                if (trackedObject.getTime() + lidarTracker.getFrequency() <= t.getTick()){
                    trackedObjectsToSend.add(trackedObject);
                }
            }
            if(!trackedObjectsToSend.isEmpty()){
                sendEvent(new TrackedObjectsEvent(trackedObjectsToSend));
            }
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast t) -> {
            if (t.getSenderName().equals("TimeService")) {
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
        });
        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast t) -> {
            terminate();
        });



    }
}
