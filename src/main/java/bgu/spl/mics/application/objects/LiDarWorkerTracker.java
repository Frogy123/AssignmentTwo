package bgu.spl.mics.application.objects;


import java.util.LinkedList;
import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
	private LiDarDataBase data;
	private int id; // The ID of the LiDar.
	private int frequency; // The time interval at which the LiDar sends new events.
	private STATUS status; // LiDar status: Up, Down, or Error.
	private List<TrackedObject> lastTrackedObjects; // The last objects the LiDar tracked.





	// Constructor
	public LiDarWorkerTracker(int id, int frequency, STATUS status, List<TrackedObject> lastTrackedObjects,LiDarDataBase data) {
		this.id = id;
		this.frequency = frequency;
		this.status = status;
		this.lastTrackedObjects = lastTrackedObjects;
		this.data = data;
	}

	// Getters
	public int getId() {
		return id;
	}

	public void trackObjects(StampedDetectedObjects stampedDetectedObjects) {
		int detectionTime = stampedDetectedObjects.getTime();
		for(DetectedObject detectedObject:stampedDetectedObjects.getDetectedObjects()) {
			List<CloudPoint> coordinates = data.findCloudPoints(detectedObject.getId(), detectionTime);
			TrackedObject trackedObject = new TrackedObject(detectedObject.getId(),detectedObject.getDescription(),detectionTime,coordinates);
			lastTrackedObjects.add(trackedObject);
		}
	}

	public int getFrequency() {
		return frequency;
	}

	public STATUS getStatus() {
		return status;
	}

	public List<TrackedObject> getLastTrackedObjects() {
		return lastTrackedObjects;
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

	public void setLastTrackedObjects(List<TrackedObject> lastTrackedObjects) {
		this.lastTrackedObjects = lastTrackedObjects;
	}
}