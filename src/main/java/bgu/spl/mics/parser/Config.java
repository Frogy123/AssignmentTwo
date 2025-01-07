package bgu.spl.mics.parser;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.IOException;

public class Config {
    @SerializedName("Cameras")
    CameraParser cameraParser;

    @SerializedName("Lidars")
    LidarParser lidarParser;

    @SerializedName("poseJsonFile")
    String poseJsonFile;

    @SerializedName("TickTime")
    int TickTime;

    @SerializedName("Duration")
    int Duration;

    //parser
    // Parse configuration file
    public static Config parseConfig(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Config config = gson.fromJson(reader, Config.class);
            return config;
        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + filePath);
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

    public CameraParser getCameraParser() {
        return cameraParser;
    }

    public LidarParser getLidarParser() {
        return lidarParser;
    }




}
