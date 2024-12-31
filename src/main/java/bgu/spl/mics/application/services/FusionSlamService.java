package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.List;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 *
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */

public class FusionSlamService extends MicroService {

    FusionSlam fusionSlam;
    int sensorsCount = 0;
    int currentTick = 0;


    /**
     * Constructor for FusionSlamService.
     *
     * @param _fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam _fusionSlam) {
        super("FusionSlamService");
        fusionSlam = _fusionSlam;
        currentTick = 0;
    }


    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        this.subscribeEvent(PoseEvent.class, (p) -> {
            while(p.getTime() != currentTick) wait();
            synchronized (this){
                Pose pose = p.getPose();
                fusionSlam.setCurrentPose(pose);
                fusionSlam.getPosesList().add(pose);
            }

        });

        this.subscribeEvent(TrackedObjectsEvent.class, (TrackedObjectsEvent t)->{
            synchronized (this){
                List<TrackedObject> trackedObjects = t.getTrackedObjects();
                for (TrackedObject tObj : trackedObjects) {
                    List<CloudPoint> coordinate = FusionSlam.tranformToGlobalCoordinate(tObj);

                    LandMark landMark = getCorrectLandMark(tObj.getId());

                    if(landMark == null){
                        LandMark newLandMark = new LandMark(tObj.getId(), tObj.getDescription(), coordinate);
                        fusionSlam.getLandmarksList().add(newLandMark);
                    }else{
                        updateLandMark(landMark, coordinate);
                    }
                }
            }

        });

        this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast t)->{
            synchronized (this){
                currentTick++;
                this.notifyAll();
            }
        });

        this.subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast t)->{
            if(t.getSenderName().equals("TimeService")){
                this.terminate();
            }
        });

        this.subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast t)->{
            this.terminate();
        });

    }

    private synchronized LandMark getCorrectLandMark(String objId){
        for (LandMark landMark : fusionSlam.getLandmarksList()) {
            if(landMark.getId().equals(objId)){
                return landMark;
            }
        }
        return null;
    }

    private synchronized void updateLandMark(LandMark landMark, List<CloudPoint> coordinate){
        for(int i = 0; i < coordinate.size(); i++){
            CloudPoint newCoordinate = coordinate.get(i);
            CloudPoint oldCoordinate = landMark.getCoordinates().get(i);

            landMark.getCoordinates().set(i, CloudPoint.average(oldCoordinate, newCoordinate));

        }
    }


}
