package bgu.spl.mics.parsing.configurations;

import java.util.List;

public class LidarWorkers {
    private List<LidarConfiguration> LidarConfigurations;
    private String lidars_data_path;

    // Getters
    public List<LidarConfiguration> getLidarConfigurations() {
        return LidarConfigurations;
    }

    public String getLidars_data_path() {
        return lidars_data_path;
    }
}
