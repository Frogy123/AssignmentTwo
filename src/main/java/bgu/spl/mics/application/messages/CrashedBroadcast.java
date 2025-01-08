package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class CrashedBroadcast implements Broadcast {
    private String senderName;
    private String errorMassage;
    private int errorTime;

    public CrashedBroadcast(String senderName, String _errorMassage, int time) {
        this.errorMassage = _errorMassage;
        this.senderName = senderName;
        this.errorTime = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getErrorMassage() {
        return errorMassage;
    }

    public int getErrorTime() {
        return errorTime;
    }

}