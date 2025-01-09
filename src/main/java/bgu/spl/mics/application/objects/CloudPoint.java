package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * CloudPoint represents a specific point in a 3D space as detected by the LiDAR.
 * These points are used to generate a point cloud representing objects in the environment.
 */
public class CloudPoint {
    List<Double> coordinates = new ArrayList<>();
    private double x;
    private double y;
    private double z; //all cloud points have the same z value added for parsing


    // Constructor
    public CloudPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }



    public static CloudPoint average(CloudPoint oldCoordinate, CloudPoint newCoordinate) {
        double x = (oldCoordinate.getX() + newCoordinate.getX()) / 2;
        double y = (oldCoordinate.getY() + newCoordinate.getY()) / 2;
        return new CloudPoint(x, y);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public void initialize() {
        coordinates.add(x);
        coordinates.add(y);
        coordinates.add(z);
    }
}