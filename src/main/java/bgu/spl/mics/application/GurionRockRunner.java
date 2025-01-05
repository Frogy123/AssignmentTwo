package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.parser.Config;
import bgu.spl.mics.parser.cameraParser;
import bgu.spl.mics.parser.lidarParser;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {


    int i;

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        String configPath = args[0];

        Gson gson = new Gson();



        // TODO: Parse configuration file.


        // TODO: Initialize system components and services.


        // TODO: Start the simulation.

        //intalianize the FusionSlamService
        FusionSlam fusionSlam = FusionSlam.getInstance();
        FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam);
        fusionSlamService.run();


        //intalianize the Config
        Config config = Config.parseConfig(configPath);



        //intalianize the CameraService
        cameraParser cameraParser = config.getCameraParser();
        List<Camera> cameras = cameraParser.getCameraList();
        String camerasDataPath = cameraParser.getCameraDataPath();
        for (Camera camera : cameras) {
            CameraService cameraService = new CameraService(camera);
            cameraService.run();
        }

        //intalianize the LidarWorker
        lidarParser lidarParser = config.getLidarParser();
        List<LiDarWorkerTracker>  lidarWorkersList = lidarParser.getLiDarWorkerTrackers();
        LiDarDataBase lidarDataBase = LiDarDataBase.getInstance(lidarParser.getLidarDataPath());

        for (LiDarWorkerTracker lidarWorker : lidarWorkersList) {
            LiDarService lidarService = new LiDarService(lidarWorker);
            lidarService.run();
        }

        //intaliazing poseService
        GPSIMU gpsimu = GPSIMU.getInstance();
        PoseService poseService = new PoseService(gpsimu);
        poseService.run();

        //intalianize the TimeService
        TimeService timeService = new TimeService(config.getTickTime(), config.getDuration());
        timeService.run();











    }


}
