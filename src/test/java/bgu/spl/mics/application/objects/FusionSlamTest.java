package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FusionSlamTest {



    @Test
    public void testTransformToGlobalCoordinateSimple() {
        List<CloudPoint> localPoints = Arrays.asList(new CloudPoint(1, 0), new CloudPoint(0, 1));
        TrackedObject trackedObject = new TrackedObject("1", "object", 1, localPoints);

        Pose pose = new Pose(2, 3, 90, 0);
        FusionSlam.getInstance().getPosesList().add(pose);

        List<CloudPoint> globalPoints = FusionSlam.tranformToGlobalCoordinate(trackedObject);

        assertEquals(1.0, globalPoints.get(0).getX(), 0.1);
        assertEquals(0.0, globalPoints.get(0).getY(), 0.1);
        assertEquals(0.0, globalPoints.get(1).getX(), 0.1);
        assertEquals(1.0, globalPoints.get(1).getY(), 0.1);
    }


    @Test
    public void testTransformToGlobalCoordinateComplex() {
        List<CloudPoint> localPoints = Arrays.asList(new CloudPoint(3, 4), new CloudPoint(-2, -3));
        TrackedObject trackedObject = new TrackedObject("2", "object", 2, localPoints);

        Pose pose1 = new Pose(0, 0, 0, 0);
        Pose pose2 = new Pose(1, 2, 45, 1);
        FusionSlam.getInstance().getPosesList().add(pose1);
        FusionSlam.getInstance().getPosesList().add(pose2);

        List<CloudPoint> globalPoints = FusionSlam.tranformToGlobalCoordinate(trackedObject);

        assertEquals(2, globalPoints.size());
        assertEquals(0.292, globalPoints.get(0).getX(), 0.1); // Corrected
        assertEquals(6.949, globalPoints.get(0).getY(), 0.1); // Corrected
        assertEquals(1.707, globalPoints.get(1).getX(), 0.1); // Corrected
        assertEquals(-1.535, globalPoints.get(1).getY(), 0.1); // Corrected
    }



}

