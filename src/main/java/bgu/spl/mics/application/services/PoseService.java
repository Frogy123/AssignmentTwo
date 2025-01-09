package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    GPSIMU gpsimu;
    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService");
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {
            if(gpsimu.getPosesSize() > gpsimu.getTick()){
                PoseEvent poseEvent = new PoseEvent(gpsimu.getCurrentPose(), gpsimu.getTick());
                sendEvent(poseEvent);
            }
            gpsimu.tick();
        });

        this.subscribeBroadcast(TerminatedBroadcast.class, boradcast -> {
            if(boradcast.getSenderName().equals("FusionSlamService")){
                sendBroadcast(new TerminatedBroadcast(this.getName()));
                terminate();
            }
        });

        this.subscribeBroadcast(CrashedBroadcast.class, boradcast -> {
            sendBroadcast(new TerminatedBroadcast(this.getName()));
            terminate();
        });


    }
}
