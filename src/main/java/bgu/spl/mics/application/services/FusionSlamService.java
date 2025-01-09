package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.parsing.Error_Output;
import bgu.spl.mics.parsing.Out;
import bgu.spl.mics.parsing.Output;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */

public class FusionSlamService extends MicroService {

    FusionSlam fusionSlam;
    int currentTick = 0;

    int sensorsCount = 0;
    List<String> sensors;
    List<TrackedObject> WaitingForPose ;
    StatisticalFolder statisticalFolder;
    String inputDirectoryPath;

    /**
     * Constructor for FusionSlamService.
     *
     * @param _fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam _fusionSlam, String inputDirectoryPath) {
        super("FusionSlamService");
        fusionSlam = _fusionSlam;
        currentTick = 0;
        sensors = new ArrayList<>();
        WaitingForPose = new ArrayList<>();
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.inputDirectoryPath = inputDirectoryPath;
    }



    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        this.subscribeBroadcast(createdBroadcast.class, (createdBroadcast c)->{
            sensors.add(c.getSenderId());
            sensorsCount++;
        });


        this.subscribeEvent(PoseEvent.class, (p) -> {
            Pose pose = p.getPose();
            fusionSlam.getPosesList().add(pose);
        });

        this.subscribeEvent(TrackedObjectsEvent.class, (TrackedObjectsEvent t)->{
                List<TrackedObject> trackedObjects = t.getTrackedObjects();
                for (TrackedObject tObj : trackedObjects) {
                    if(FusionSlam.getInstance().getPoseTime() >= tObj.getTime()){
                        List<CloudPoint> coordinate = FusionSlam.tranformToGlobalCoordinate(tObj);

                        LandMark landMark = getCorrectLandMark(tObj.getId());

                        if(landMark == null){
                            LandMark newLandMark = new LandMark(tObj.getId(), tObj.getDescription(), coordinate);
                            fusionSlam.getLandmarksList().add(newLandMark);
                            statisticalFolder.incrementNumLandmarks();
                        }else{
                            updateLandMark(landMark, coordinate);
                        }
                    }else{
                           WaitingForPose.add(tObj);
                    }

                }
                if(!WaitingForPose.isEmpty()){
                    this.sendEvent(new TrackedObjectsEvent(WaitingForPose));
                    WaitingForPose.clear();
                }
        });

        this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast t)->{
                currentTick++;
                statisticalFolder.incrementSystemRunTime();
        });

        this.subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast t)->{
            if(!t.getSenderName().equals("TimeService") & !t.getSenderName().equals("PoseService")){
                sensors.remove(t.getSenderName());
                if(sensors.isEmpty()){
                    Output output = new Output(fusionSlam);
                    File file = createOutputFile(output,inputDirectoryPath);
                    writeOutput(file, output);
                    this.sendBroadcast(new TerminatedBroadcast(this.getName()));
                    this.terminate();
                }
            }
        });

        this.subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast t)->{
            if(sensorsCount == 1){
                String ErrorMsg = t.getErrorMassage();
                String FaultySensor = t.getSenderName();
                Error_Output.getInstance().writeError(ErrorMsg, FaultySensor, fusionSlam); //need to fix
                File outFile = createOutputFile(Error_Output.getInstance(),inputDirectoryPath);
                writeOutput(outFile, Error_Output.getInstance());
                this.sendBroadcast(new TerminatedBroadcast(this.getName()));
                this.terminate();
            }else{
                sensorsCount--;
            }
        });


    }

    private LandMark getCorrectLandMark(String objId){
        for (LandMark landMark : fusionSlam.getLandmarksList()) {
            if(landMark.getId().equals(objId)){
                return landMark;
            }
        }
        return null;
    }

    private void updateLandMark(LandMark landMark, List<CloudPoint> coordinate){
        for(int i = 0; i < coordinate.size(); i++){
            CloudPoint newCoordinate = coordinate.get(i);
            if(landMark.getCoordinates().size() <= i){
                landMark.getCoordinates().add(newCoordinate);
            }else{
                CloudPoint oldCoordinate = landMark.getCoordinates().get(i);
                landMark.getCoordinates().set(i, CloudPoint.average(oldCoordinate, newCoordinate));
            }
        }
    }



    private File createOutputFile(Out out, String directoryPath) {
        // Determine the file name based on the type of 'out'
        String fileName = out instanceof Error_Output ? "error_output.json" : "output.json";

        try {
            // Ensure the directory exists
            File directory = new File(directoryPath);
            if (!directory.exists() && !directory.mkdirs()) {
                System.err.println("Failed to create directory: " + directoryPath);
                return null;
            }

            // Create the file in the specified directory
            File file = new File(directory, fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());
            }
            return file;
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file in directory: " + directoryPath);
            e.printStackTrace();
        }

        return null;
    }

    private void writeOutput(File file, Out out){
        try {
            FileWriter myWriter = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();  // Added pretty printing
            String json = gson.toJson(out);  // Convert the object to formatted JSON

            myWriter.write(json);  // Write the formatted JSON to the file
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
