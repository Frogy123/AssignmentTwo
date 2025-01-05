package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class createdBroadcast implements Broadcast {
    private String id;
    //an Event that is sent when a new object is created to the FusionSlam
    public createdBroadcast(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return id;
    }

}
