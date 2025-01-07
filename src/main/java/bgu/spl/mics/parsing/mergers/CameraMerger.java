package bgu.spl.mics.parsing.mergers;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.parsing.configurations.CameraConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CameraMerger {
    public static List<Camera> merge(List<CameraConfiguration> configs, Map<String, List<StampedDetectedObjects>> Data){
        List<Camera> cameras = new LinkedList<>();
        for(CameraConfiguration config: configs){
            cameras.add(new Camera(config.getId(),config.getFrequency(), STATUS.UP,Data.get(config.getCamera_key())));
        }
        return cameras;
    }
}
