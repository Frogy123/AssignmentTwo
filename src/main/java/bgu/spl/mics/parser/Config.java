package bgu.spl.mics.parser;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Config {
    @SerializedName("cameras")
    cameraParser cameraParser;
    @SerializedName("LidarWorkers")
    lidarParser lidarParser;
    String poseJsonFile;
    int TickTime;
    int Duration;

    //parser
    public static Config parseConfig(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Config config = gson.fromJson(reader, Config.class);
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //getters


    public String getPoseJsonFile() {
        return poseJsonFile;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }

    public cameraParser getCameraParser() {
        return cameraParser;
    }

    public lidarParser getLidarParser() {
        return lidarParser;
    }




}
