package bgu.spl.mics.application.objects;

import bgu.spl.mics.parsing.configurations.RawStampedCloudPoints;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {


    @SerializedName("time")
    private final int time;

    @SerializedName("id")
    private final String id;

    private final List<CloudPoint> cloudPoints;



    public StampedCloudPoints(RawStampedCloudPoints rawStampedCloudPoints){
        this.id = rawStampedCloudPoints.getId();
        this.time = rawStampedCloudPoints.getTime();
        cloudPoints = new ArrayList<>();
        for(List<Double> point:rawStampedCloudPoints.getRawCloudPoints()){
            cloudPoints.add(new CloudPoint(point.get(0),point.get(1)));
        }
    }

    public String getId() {
        return id;
    }
    public int getTime() {
        return time;
    }
    public List<CloudPoint> getCloudPoints() {
        return cloudPoints;
    }


}
