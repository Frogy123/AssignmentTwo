package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {


    int tickTime;
    int duration;
    int currentTick;
    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("TickService");
        int tickTime = TickTime;
        int duration = Duration;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {
        int currentTick = 0;
        while(currentTick < duration){
            try {
                Thread.sleep(tickTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                sendBroadcast(new CrashedBroadcast("TimeService"));
            }
            sendBroadcast(new TickBroadcast(currentTick));
            currentTick++;
        };
    }
}
