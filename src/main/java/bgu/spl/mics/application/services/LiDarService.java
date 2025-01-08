package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
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
    private StatisticalFolder statisticalFolder;
    /**
     * Constructor for LiDarService.
     *
     * @param liDarTracker The LiDAR tracker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarTracker) {
        super("LiDarService" + liDarTracker.getId());
        this.lidarTracker=liDarTracker;
        this.statisticalFolder = StatisticalFolder.getInstance();
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {

        subscribeEvent(DetectedObjectsEvent.class, (DetectedObjectsEvent t) -> {
            System.out.println("DEBUG: recieved DetectedObjectsEvent " + t.getStampedObjects().getDetectedObjects().toString() );

            lidarTracker.trackObjects(t.getStampedObjects());
        });

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {
            List<TrackedObject> trackedObjectsToSend = new ArrayList<>();
            if(LiDarDataBase.getInstance().getLastTime() < t.getTick()){
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
            else if(lidarTracker.getLastTrackedObjects() != null){

                for (TrackedObject trackedObject : lidarTracker.getLastTrackedObjects()){
                    if (trackedObject.getTime() + lidarTracker.getFrequency() <= t.getTick()){
                        trackedObjectsToSend.add(trackedObject);
                        System.out.println("DEBUG: tracked object: " + trackedObject.getId() );
                    }
                }
            }
            if(!trackedObjectsToSend.isEmpty()){
                for(TrackedObject trackedObjectToRemove: trackedObjectsToSend){
                    lidarTracker.getLastTrackedObjects().remove(trackedObjectToRemove);
                }
                System.out.println("DEBUG: incrementing NumTrackedObjects, by: " + trackedObjectsToSend.size() );
                statisticalFolder.incrementNumTrackedObjects(trackedObjectsToSend.size()); // statistical
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
            sendBroadcast(new CrashedBroadcast(t.getSenderName(), t.getErrorMassage(), t.getTime()));
            terminate();
        });

        sendBroadcast(new createdBroadcast(this.getName()));



    }
}
