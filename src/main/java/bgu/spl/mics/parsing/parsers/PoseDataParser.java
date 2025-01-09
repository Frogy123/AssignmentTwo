package bgu.spl.mics.parsing.parsers;

import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PoseDataParser {

    public static void parse(String path) {
        Gson gson = new Gson();
        List<Pose> posesToInitialiaze = null;
        try {
            // Define the Type for the List<StampedCloudPoints> to handle generic types
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();

            // Read the Lidar data file and parse it into the list
            FileReader poseDataFileReader = new FileReader(path);
            posesToInitialiaze = gson.fromJson(poseDataFileReader, poseListType);
            poseDataFileReader.close();

            GPSIMU gpsimu = GPSIMU.getInstance();

            gpsimu.setPoses(posesToInitialiaze);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
