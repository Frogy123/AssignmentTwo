package bgu.spl.mics.parsing.parsers;

import bgu.spl.mics.application.objects.StampedCloudPoints;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class LidarDataParser {
    public static List<StampedCloudPoints> parse(String lidarDataFilePath) {
        // Create Gson object
        Gson gson = new Gson();
        List<StampedCloudPoints> lidarDataList = null;

        try {
            // Define the Type for the List<StampedCloudPoints> to handle generic types
            Type lidarDataType = new TypeToken<List<StampedCloudPoints>>(){}.getType();

            // Read the Lidar data file and parse it into the list
            FileReader lidarDataFileReader = new FileReader(lidarDataFilePath);
            lidarDataList = gson.fromJson(lidarDataFileReader, lidarDataType);
            lidarDataFileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lidarDataList;
    }
}
