package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.parsing.Error_Output;



/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;
    StatisticalFolder statisticalFolder;
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

    }




    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {

        this.subscribeBroadcast(TickBroadcast.class,(TickBroadcast t) -> {
            if(camera.getLastTime() + camera.getFrequency() < t.getTick()) {
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
            else if(camera.getStatus() == STATUS.UP) {
                StampedDetectedObjects stampedDetectedObjects = camera.getReadyToSendObjects(t.getTick()); // get objects that i can send immediately delay considered
                if (stampedDetectedObjects != null) {
                    String errorDescription = getError(stampedDetectedObjects);
                    if(errorDescription == null )
                    {
                        lastFrame = stampedDetectedObjects; //save in case of crash in the next tick
                        statisticalFolder.incrementNumDetectedObjects(stampedDetectedObjects.getNumOfDetectedObjects()); // statistical
                        sendEvent( new DetectedObjectsEvent(stampedDetectedObjects)); // send Event
                    }
                    else handleError(errorDescription, t.getTick());

                }
            }
        });

        this.subscribeBroadcast(TerminatedBroadcast.class, boradcast -> {
            if(boradcast.getSenderName().equals("TimeService")){
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
        });

        this.subscribeBroadcast(CrashedBroadcast.class,boradcast -> {
            writeLastFrame(boradcast.getTime());
            sendBroadcast(new CrashedBroadcast(boradcast.getSenderName(), boradcast.getErrorMassage(), boradcast.getTime()));
            terminate();
        });
        sendBroadcast(new createdBroadcast(this.getName()));
    }


    /** checks if StampedDetectedObjects contains an error, if so returns the error describtion, else returns null */
    private String getError(StampedDetectedObjects objects){
        for(DetectedObject object: objects.getDetectedObjects()){
            if(object.getId().equals("ERROR")) {
                return object.getDescription();
            }
        }
        return null;
    }

    private void handleError(String errorDescripition, int t){
        sendBroadcast(new CrashedBroadcast(camera.getKey(), errorDescripition, t));
        camera.setStatus(STATUS.ERROR);
        writeLastFrame(t);
        terminate();
    }

    public void writeLastFrame(int time){
        Error_Output.getInstance().addCameraLastFrame(camera, time);
    }


}