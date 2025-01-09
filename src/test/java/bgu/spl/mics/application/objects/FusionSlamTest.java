package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FusionSlamTest {




    @Test
    public void testTranformToGlobalCoordinate1() {
        // Create a TrackedObject with local coordinates and a specific time
        List<CloudPoint> localPoints = Arrays.asList(new CloudPoint(5, 6), new CloudPoint(7, 8));
        TrackedObject trackedObject = new TrackedObject("1","table",3,localPoints);

        // Add corresponding poses to the FusionSlam instance
        Pose pose1 = new Pose(0, 0, 0, 0); // Pose at time 0
        Pose pose2 = new Pose(1, 1, 45, 1); // Pose at time 1 with 45 degrees yaw
        FusionSlam.getInstance().getPosesList().add(pose1);
        FusionSlam.getInstance().getPosesList().add(pose2);

        // Call the function to transform to global coordinates
        List<CloudPoint> globalPoints = FusionSlam.tranformToGlobalCoordinate(trackedObject);

        // Verify the results
        assertEquals(2, globalPoints.size());
        assertEquals(5.707, globalPoints.get(0).getX(), 0.001);
        assertEquals(6.707, globalPoints.get(0).getY(), 0.001);
        assertEquals(7.707, globalPoints.get(1).getX(), 0.001);
        assertEquals(8.707, globalPoints.get(1).getY(), 0.001);
    }

    @Test
    public void testTranformToGlobalCoordinate2() {
        // Create a TrackedObject with local coordinates and a specific time
        List<CloudPoint> localPoints = Arrays.asList(new CloudPoint(2, 3), new CloudPoint(4, 5));
        TrackedObject trackedObject = new TrackedObject("3","chair",1,localPoints);

        // Add corresponding poses to the FusionSlam instance
        Pose pose1 = new Pose(0, 0, 0, 0); // Pose at time 0
        Pose pose2 = new Pose(1, 1, 30, 1); // Pose at time 1 with 30 degrees yaw
        Pose pose3 = new Pose(2, 2, 60, 2); // Pose at time 2 with 60 degrees yaw
        FusionSlam.getInstance().getPosesList().add(pose1);
        FusionSlam.getInstance().getPosesList().add(pose2);
        FusionSlam.getInstance().getPosesList().add(pose3);

        // Call the function to transform to global coordinates
        List<CloudPoint> globalPoints = FusionSlam.tranformToGlobalCoordinate(trackedObject);

        // Verify the results
        assertEquals(2, globalPoints.size());
        assertEquals(2.366, globalPoints.get(0).getX(), 0.001);
        assertEquals(4.366, globalPoints.get(0).getY(), 0.001);
        assertEquals(3.732, globalPoints.get(1).getX(), 0.001);
        assertEquals(6.732, globalPoints.get(1).getY(), 0.001);
    }
}

