package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.ArrayList;
import java.util.List;

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
    StatisticalFolder statisticalFolder;
    List<StampedDetectedObjects> detectedObjectsToSend;
    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */


    public CameraService(Camera camera) {
        super(camera.getKey());
        this.camera = camera;
        statisticalFolder = StatisticalFolder.getInstance();
        detectedObjectsToSend = new ArrayList<StampedDetectedObjects>();
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
                detectedObjectsToSend.add(objs);
                statisticalFolder.incrementNumDetectedObjects(objs.getNumOfDetectedObjects());
            }

            if(t.getTick() % camera.getFrequency() == 0){
               for(StampedDetectedObjects stampedDetectedObjects : detectedObjectsToSend){
                   sendEvent(new DetectedObjectsEvent(stampedDetectedObjects));
               }
               detectedObjectsToSend.clear();
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