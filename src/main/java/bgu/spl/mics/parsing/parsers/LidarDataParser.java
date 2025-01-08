package bgu.spl.mics.parsing.parsers;

import bgu.spl.mics.application.objects.StampedCloudPoints;
import bgu.spl.mics.parsing.configurations.RawStampedCloudPoints;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class LidarDataParser {
    public static List<StampedCloudPoints> parse(String lidarDataFilePath) {
        // Create Gson object
        Gson gson = new Gson();
        List<RawStampedCloudPoints> RawLidarDataList = null;
        List<StampedCloudPoints> lidarDataList = null;
        try {
            // Define the Type for the List<StampedCloudPoints> to handle generic types
            Type lidarDataType = new TypeToken<List<RawStampedCloudPoints>>(){}.getType();

            // Read the Lidar data file and parse it into the list
            FileReader lidarDataFileReader = new FileReader(lidarDataFilePath);
            RawLidarDataList = gson.fromJson(lidarDataFileReader, lidarDataType);
            lidarDataFileReader.close();

            // Convert the RawStampedCloudPoints list to StampedCloudPoints list
            for(RawStampedCloudPoints rawStampedCloudPoints:RawLidarDataList){
                lidarDataList.add(new StampedCloudPoints(rawStampedCloudPoints));
            }





        } catch (IOException e) {
            e.printStackTrace();
        }

        return lidarDataList;
    }
}
