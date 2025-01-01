package bgu.spl.mics.application.objects;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */

public class Camera {
    private int id; // The ID of the camera.
    private int frequency; // The time interval at which the camera sends new events.
    private STATUS status; // Camera status: Up, Down, or Error.
    private List<DetectedObject> detectedObjectsList; // List of detected objects with timestamps.

    // Enum for status

    // Constructor
    public Camera(int id, int frequency, STATUS status, List<DetectedObject> detectedObjectsList) {
        this.id = id;
        this.frequency = frequency;
        this.status = status;
        this.detectedObjectsList = detectedObjectsList;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public STATUS getStatus() {
        return status;
    }

    public List<DetectedObject> getDetectedObjectsList() {
        return detectedObjectsList;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setDetectedObjectsList(List<DetectedObject> detectedObjectsList) {
        this.detectedObjectsList = detectedObjectsList;
    }
}
