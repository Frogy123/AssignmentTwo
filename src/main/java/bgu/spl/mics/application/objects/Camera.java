package bgu.spl.mics.application.objects;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */

public class Camera {
    private int id; // The ID of the camera.
    private int frequency; // The time interval at which the camera sends new events.
    private String key;
    private STATUS status; // Camera status: Up, Down, or Error.
    private List<StampedDetectedObjects> detectedObjectsList; // List of detected objects with timestamps.

    // Enum for status

    // Constructor
    public Camera(int id, int frequency, STATUS status, List<StampedDetectedObjects> detectedObjectsList) {
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

    public String getKey() {
        return key;
    }

    public STATUS getStatus() {
        return status;
    }

    public List<StampedDetectedObjects> getDetectedObjectsList() {
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

    public void setDetectedObjectsList(List<StampedDetectedObjects> detectedObjectsList) {
        this.detectedObjectsList = detectedObjectsList;
    }

    // other methods:

    /**
     *
     * @param time
     * @return detected objects at a particular time
     *
     * @pre: none
     * @post: If an object with time exists in detectedObjectsList, that object is returned.
     * If no such object exists, the method returns null.
     */
    public StampedDetectedObjects DetectObjects(int time){
        for(StampedDetectedObjects objs: detectedObjectsList){
            if (objs.getTime() == time){
                try {
                    Thread.sleep(frequency);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupt status
                    throw new RuntimeException("Thread sleep interrupted", e);
                }

                return objs;
            }
        }
        return null;
    }

}