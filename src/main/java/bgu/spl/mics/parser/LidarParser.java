package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LidarParser {
    @SerializedName("LidarConfigurations")
    List<LiDarWorkerTracker> LidarConfigurations;
    @SerializedName("lidars_data_path")
    String lidars_data_path;

    public List<LiDarWorkerTracker> getLiDarWorkerTrackers() {
        return LidarConfigurations;
    }

    public String getLidarDataPath() {
        return lidars_data_path;
    }
}
