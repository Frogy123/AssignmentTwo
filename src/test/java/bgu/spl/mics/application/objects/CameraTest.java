package bgu.spl.mics.application.objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CameraTest {

    private Camera camera;

    @BeforeEach
    public void setUp() {
        // Initialize the list of stamped detected objects
        List<StampedDetectedObjects> stamps = new LinkedList<>();

        // Stamp 1
        DetectedObject obj1 = new DetectedObject("0", "Table");
        StampedDetectedObjects stamp1 = new StampedDetectedObjects(1);
        stamp1.addDetectedObject(obj1);
        stamps.add(stamp1);

        // Stamp 2
        DetectedObject obj2 = new DetectedObject("1", "Chair");
        StampedDetectedObjects stamp2 = new StampedDetectedObjects(2);
        stamp2.addDetectedObject(obj2);
        stamps.add(stamp2);

        // Stamp 3
        DetectedObject obj3 = new DetectedObject("2", "Lamp");
        DetectedObject obj4 = new DetectedObject("3", "Sofa");
        StampedDetectedObjects stamp3 = new StampedDetectedObjects(3);
        stamp3.addDetectedObject(obj3);
        stamp3.addDetectedObject(obj4);
        stamps.add(stamp3);

        // Stamp 5
        DetectedObject obj5 = new DetectedObject("4", "Book");
        StampedDetectedObjects stamp5 = new StampedDetectedObjects(5);
        stamp5.addDetectedObject(obj5);
        stamps.add(stamp5);

        // Create a camera object with the prepared list
        camera = new Camera(1, 2, STATUS.UP, stamps);  // Assuming frequency = 2 for this example
    }

    @Test
    public void testOneDetectedObject() {
        // Access time 1 where we have one detected object (Table)
        StampedDetectedObjects result = camera.getReadyToSendObjects(3);  // time - frequency = 3 - 2 = 1

        assertEquals(1, result.getNumOfDetectedObjects(), "Expected one detected object");
        assertEquals("Table", result.getDetectedObjects().get(0).getDescription(), "Expected detected object to be Table");
    }

    @Test
    public void testMultipleDetectedObjects() {
        // Access time 3 where we have two detected objects (Lamp, Sofa)
        StampedDetectedObjects result = camera.getReadyToSendObjects(5);  // time - frequency = 5 - 2 = 3

        assertEquals(2, result.getNumOfDetectedObjects(), "Expected two detected objects");
        assertEquals("Lamp", result.getDetectedObjects().get(0).getDescription(), "First detected object should be Lamp");
        assertEquals("Sofa", result.getDetectedObjects().get(1).getDescription(), "Second detected object should be Sofa");
    }

    @Test
    public void testNoDetectedObjectsAtTime() {
        // Time 4 - frequency = 4 - 2 = 2, which does not match any detection time
        StampedDetectedObjects result = camera.getReadyToSendObjects(4);

        assertNull(result, "Expected null since there are no detected objects at time 2");
    }
}
