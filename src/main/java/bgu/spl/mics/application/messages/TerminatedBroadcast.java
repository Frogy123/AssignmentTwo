package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TerminatedBroadcast implements Broadcast
{
    private String id;

    public TerminatedBroadcast(String id)
    {
        this.id = id;
    }

    public String getSenderId()
    {
        return id;
    }


}
