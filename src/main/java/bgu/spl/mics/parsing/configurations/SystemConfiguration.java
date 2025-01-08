package bgu.spl.mics.parsing.configurations;

import bgu.spl.mics.application.objects.Camera;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SystemConfiguration {
    private Cameras Cameras;
    @SerializedName("LiDarWorkers")
    private LidarWorkers LidarWorkers;
    private String poseJsonFile;
    private int TickTime;
    private int Duration;

    // Getters
    public Cameras getCameras() {
        return Cameras;
    }

    public LidarWorkers getLidarWorkers() {
        return LidarWorkers;
    }

    public String getPoseJsonFile() {
        return poseJsonFile;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }
}
