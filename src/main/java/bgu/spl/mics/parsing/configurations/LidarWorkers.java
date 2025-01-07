package bgu.spl.mics.parsing.configurations;

import bgu.spl.mics.application.objects.LiDarWorkerTracker;

import java.util.List;

public class LidarWorkers {
    private List<LiDarWorkerTracker> LidarConfigurations;
    private String lidars_data_path;

    // Getters
    public List<LiDarWorkerTracker> getLidarConfigurations() {
        return LidarConfigurations;
    }

    public String getLidars_data_path() {
        return lidars_data_path;
    }
}
