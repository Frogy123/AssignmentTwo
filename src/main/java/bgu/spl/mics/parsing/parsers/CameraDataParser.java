package bgu.spl.mics.parsing.parsers;

import bgu.spl.mics.application.objects.StampedDetectedObjects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CameraDataParser {
    public static Map<String, List<StampedDetectedObjects>> parse(String cameraDataFilePath) {
        // Create Gson object
        Gson gson = new Gson();
        Map<String, List<StampedDetectedObjects>> cameraDataMap = null;
        try {
            // Define the Type for the Map<String, List<StampedDetectedObjects>> to handle generic types
            Type cameraDataType = new TypeToken<Map<String, List<StampedDetectedObjects>>>(){}.getType();
            FileReader cameraDataFileReader = new FileReader(cameraDataFilePath);
            cameraDataMap = gson.fromJson(cameraDataFileReader, cameraDataType);
            cameraDataFileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cameraDataMap;
    }
}
