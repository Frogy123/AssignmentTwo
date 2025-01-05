package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 *
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private static int cameraServiceCount = 0;

    private Camera camera;
    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super(camera.getKey());
        this.camera = camera;
    }


    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        this.subscribeBroadcast(TickBroadcast.class,(TickBroadcast t) -> {
            StampedDetectedObjects objs = camera.DetectObjects(t.getTick());
            if(objs != null){
                sendEvent(new DetectedObjectsEvent(objs));
            }
        });
        this.subscribeBroadcast(TerminatedBroadcast.class, boradcast -> {
            if(boradcast.getSenderId().equals("TimeService")){
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
        });
        this.subscribeBroadcast(CrashedBroadcast.class,boradcast -> terminate());
        sendBroadcast(new createdBroadcast(this.getName()));
    }

}