package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
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
        tickTime = TickTime;
        duration = Duration;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {

        currentTick = 1;
        while(currentTick < duration & MicroService.counter > 1){

            try {
                Thread.sleep(tickTime*1000L);
            } catch (InterruptedException e) {
                sendBroadcast(new CrashedBroadcast("TimeService", "InterruptedException", currentTick));
            }
            System.out.println("DEBUG: Tick:" + currentTick);
            sendBroadcast(new TickBroadcast(currentTick));
            currentTick++;
        }
        sendBroadcast(new TerminatedBroadcast("TimeService"));
        terminate();

    }






}
