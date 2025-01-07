package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LidarParser {
    @SerializedName("LidarConfigurations")
    List<LiDarWorkerTracker> LiDarWorkerTrackers;
    @SerializedName("lidars_data_path")
    String lidarDataPath;

    public List<LiDarWorkerTracker> getLiDarWorkerTrackers() {
        return LiDarWorkerTrackers;
    }

    public String getLidarDataPath() {
        return lidarDataPath;
    }
}
