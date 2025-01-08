package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;

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
        super("LiDarService" + liDarTracker.getId());
        this.lidarTracker=liDarTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        System.out.println("DEBUG: initializing LiDarService" + lidarTracker.getId());

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {

        });
        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast t) -> {

        });
        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast t) -> {

        });
        subscribeEvent(DetectedObjectsEvent.class, (DetectedObjectsEvent t) -> {

        });

        sendBroadcast(new createdBroadcast(this.getName()));

        System.out.println("DEBUG: finished initializing LiDarService" + lidarTracker.getId());

    }
}