package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.parsing.PathResolver;
import bgu.spl.mics.parsing.configurations.SystemConfiguration;
import bgu.spl.mics.parsing.mergers.CameraMerger;
import bgu.spl.mics.parsing.parsers.CameraDataParser;
import bgu.spl.mics.parsing.parsers.PoseDataParser;
import bgu.spl.mics.parsing.parsers.SystemConfigurationParser;

import java.util.List;
import java.util.Map;
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



    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */

    public static void main(String[] args) {
        //parse system config
        String configPath = args[0];
        String inputDirectoryPath = PathResolver.getDirectoryPath(configPath);
        SystemConfiguration systemConfiguration = SystemConfigurationParser.parse(configPath);

        //parse camera data
        String cameraDataRelPath = systemConfiguration.getCameras().getCamera_datas_path();
        String cameraDataAbsPath = PathResolver.resolveRelativePath(cameraDataRelPath,configPath);
        Map<String, List<StampedDetectedObjects>> cameraData = CameraDataParser.parse(cameraDataAbsPath);

        //merge cameras config with camera data
        List<Camera> cameras = CameraMerger.merge(systemConfiguration.getCameras().getCamerasConfigurations(), cameraData);

        //parse lidar data
        String lidarDataRelPath = systemConfiguration.getLidarWorkers().getLidars_data_path();
        String lidarDataAbsPath = PathResolver.resolveRelativePath(lidarDataRelPath,configPath);
        LiDarDataBase lidarDataBase = LiDarDataBase.getInstance(lidarDataAbsPath);

        //parse pose data
        String poseDataRelPath = systemConfiguration.getPoseJsonFile();
        String poseDataAbsPath = PathResolver.resolveRelativePath(poseDataRelPath,configPath);
        PoseDataParser.parse(poseDataAbsPath);




        // TODO: Initialize system components and services.


        // TODO: Start the simulation.

        // Initialize a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        try {
            // Initialize the FusionSlamService
            FusionSlam fusionSlam = FusionSlam.getInstance();
            FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam,inputDirectoryPath);
            executorService.submit(fusionSlamService);

            // Initialize the CameraService

            for (Camera camera : cameras) {
                CameraService cameraService = new CameraService(camera);
                executorService.submit(cameraService);
            }

            // Initialize the LidarWorker

            List<LiDarWorkerTracker> lidarWorkersList =systemConfiguration.getLidarWorkers().getLidarConfigurations();
            for (LiDarWorkerTracker lidarWorker : lidarWorkersList) {
                lidarWorker.setData(lidarDataBase);
                LiDarService lidarService = new LiDarService(lidarWorker);
                executorService.submit(lidarService);
            }

            // Initialize the PoseService
            GPSIMU gpsimu = GPSIMU.getInstance();
            PoseService poseService = new PoseService(gpsimu);
            executorService.submit(poseService);

            // Initialize the TimeService
            TimeService timeService = new TimeService(systemConfiguration.getTickTime(), systemConfiguration.getDuration());
            executorService.submit(timeService);


        } finally {
            // Shutdown the executor service
            executorService.shutdown();
        }



    }

}
