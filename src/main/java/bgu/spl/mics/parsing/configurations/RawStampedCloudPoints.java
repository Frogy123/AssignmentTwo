package bgu.spl.mics.parsing.configurations;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RawStampedCloudPoints {
    int time;

    String id;

    @SerializedName("cloudPoints")
    List<List<Double>> rawCloudPoints = new ArrayList<>();

    public int getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public List<List<Double>> getRawCloudPoints() {
        return rawCloudPoints;
    }

}
