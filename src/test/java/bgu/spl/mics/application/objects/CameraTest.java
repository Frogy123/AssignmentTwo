package bgu.spl.mics.application.objects;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CameraTest {

    @BeforeEach
    public void setUp(){
        StampedDetectedObjects[] objs = new StampedDetectedObjects[3];
        objs[0] = new StampedDetectedObjects();
        objs[1] = new StampedDetectedObjects();
        objs[2] = new StampedDetectedObjects();
        Camera camera = new Camera(1,STATUS.UP,);
    }
    @AfterEach
    public void tearDown()
    {

    }

    @Test
    public void testDetectObjects() {

    }


}
