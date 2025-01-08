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
    private int time;

    @SerializedName("id")
    private String id;

    private List<CloudPoint> cloudPoints;


    public StampedCloudPoints(String id, int time) {
        this.id = id;
        this.time = time;
        cloudPoints = new ArrayList<>();
    }

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
    public void addCloudPoint(CloudPoint cloudPoint) {
        this.cloudPoints.add(cloudPoint);
    }

    public void intializeCloudPoints() {
        for(CloudPoint point:this.cloudPoints){
            point.initialize();
        }
    }
}
