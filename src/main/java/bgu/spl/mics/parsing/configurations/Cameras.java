package bgu.spl.mics.parsing.configurations;

import java.util.List;

public class Cameras {
    private List<CameraConfiguration> CamerasConfigurations;
    private String camera_datas_path;

    // Getters
    public List<CameraConfiguration> getCamerasConfigurations() {
        return CamerasConfigurations;
    }

    public String getCamera_datas_path() {
        return camera_datas_path;
    }
}
