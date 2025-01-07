package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.parser.Config;
import bgu.spl.mics.parser.CameraParser;
import bgu.spl.mics.parser.LidarParser;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        // Initialize the Config
        Config config = Config.parseConfig(configPath);



        // TODO: Parse configuration file.


        // TODO: Initialize system components and services.


        // TODO: Start the simulation.

        // Initialize a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        try {
            // Initialize the FusionSlamService
            FusionSlam fusionSlam = FusionSlam.getInstance();
            FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam);
            executorService.submit(fusionSlamService);

            // Initialize the CameraService
            CameraParser cameraParser = config.getCameraParser();
            List<Camera> cameras = cameraParser.getCameraList();
            String camerasDataPath = cameraParser.getCameraDataPath();

            for (Camera camera : cameras) {
                CameraService cameraService = new CameraService(camera);
                executorService.submit(cameraService);
            }

            // Initialize the LidarWorker
            LidarParser lidarParser = config.getLidarParser();
            List<LiDarWorkerTracker> lidarWorkersList = lidarParser.getLiDarWorkerTrackers();
            LiDarDataBase lidarDataBase = LiDarDataBase.getInstance(lidarParser.getLidarDataPath());

            for (LiDarWorkerTracker lidarWorker : lidarWorkersList) {
                LiDarService lidarService = new LiDarService(lidarWorker);
                executorService.submit(lidarService);
            }

            // Initialize the PoseService
            GPSIMU gpsimu = GPSIMU.getInstance();
            PoseService poseService = new PoseService(gpsimu);
            executorService.submit(poseService);

            // Initialize the TimeService
            TimeService timeService = new TimeService(config.getTickTime(), config.getDuration());
            executorService.submit(timeService);
        } finally {
            // Shutdown the executor service
            executorService.shutdown();
        }



    }


}
