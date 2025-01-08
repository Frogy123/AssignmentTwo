package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an object tracked by the LiDAR.
 * This object includes information about the tracked object's ID, description,
 * time of tracking, and coordinates in the environment.
 */
public class TrackedObject {
    private String id;
    private int time;
    private String description;
    private List<CloudPoint> coordinates;
    public TrackedObject(String id, String description, int time, List<CloudPoint> coordinates) {
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
        this.time = time;
    }
    public String getId() {
        return id;
    }
    public int tick(){
        time--;
        return time;
    }
    public int getTime() {
        return time;
    }
    public String getDescription() {
        return description;
    }
    public List<CloudPoint> getCoordinates() {
        return coordinates;
    }
    public void addCoordinate(CloudPoint coordinate) {
        coordinates.add(coordinate);
    }

}
