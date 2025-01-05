package bgu.spl.mics.application.objects;


import java.util.ArrayList;
import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
	private int id; // The ID of the LiDar.
	private int frequency; // The time interval at which the LiDar sends new events.
	private STATUS status; // LiDar status: Up, Down, or Error.
	private List<TrackedObject> lastTrackedObjects; // The last objects the LiDar tracked.
	public static List<LiDarWorkerTracker> LiDarWorkerTrackers = new ArrayList<>();



	// Constructor
	public LiDarWorkerTracker(int id, int frequency, STATUS status, List<TrackedObject> lastTrackedObjects) {
		this.id = id;
		this.frequency = frequency;
		this.status = status;
		this.lastTrackedObjects = lastTrackedObjects;
		LiDarWorkerTrackers.add(this);
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