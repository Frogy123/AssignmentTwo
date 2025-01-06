package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class CrashedBroadcast implements Broadcast {

    private String senderName;
    private String errorMassage;

    public CrashedBroadcast(String senderName, String _errorMassage) {
        this.errorMassage = _errorMassage;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getErrorMassage() {
        return errorMassage;
    }

}