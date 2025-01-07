package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;
    StatisticalFolder statisticalFolder;
    List<StampedDetectedObjects> detectedObjectsToSend;
    StampedDetectedObjects lastFrame;
    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */


    public CameraService(Camera camera) {
        super(camera.getKey());
        this.camera = camera;
        statisticalFolder = StatisticalFolder.getInstance();
        detectedObjectsToSend = new ArrayList<>();
    }

    public void writeLastFrame(){
        //TODO: write down the last frame to the error file
    }


    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        System.out.println("DEBUG: initializing CameraService" + camera.getId());
        this.subscribeBroadcast(TickBroadcast.class,(TickBroadcast t) -> {
            if(camera.getStatus() == STATUS.UP) {

                StampedDetectedObjects objs = camera.detectObjects(t.getTick());
                if (objs != null) {
                    //check if there is an error:
                    for(DetectedObject object: objs.getDetectedObjects()){
                        if(object.getId().equals("ERROR")) {
                            camera.setStatus(STATUS.ERROR);
                            sendBroadcast(new CrashedBroadcast(camera.getKey(), object.getDescription()));
                            writeLastFrame();
                            terminate();
                            return;
                        }
                    }

                    //if there is no error:
                    detectedObjectsToSend.add(objs);
                    lastFrame = objs;
                    statisticalFolder.incrementNumDetectedObjects(objs.getNumOfDetectedObjects());
                }

                if (t.getTick() % camera.getFrequency() == 0) {
                    for (StampedDetectedObjects stampedDetectedObjects : detectedObjectsToSend) {
                        sendEvent( new DetectedObjectsEvent(stampedDetectedObjects));
                    }
                    detectedObjectsToSend.clear();
                }
            }
        });
        this.subscribeBroadcast(TerminatedBroadcast.class, boradcast -> {
            if(boradcast.getSenderId().equals("TimeService")){
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
        });
        this.subscribeBroadcast(CrashedBroadcast.class,boradcast -> {
            writeLastFrame();
            terminate();
        });
        sendBroadcast(new createdBroadcast(this.getName()));

        System.out.println("DEBUG: Finished initializing CameraService" + camera.getId());

    }

}