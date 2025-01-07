package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.parser.Error_Output;
import bgu.spl.mics.parser.Out;
import bgu.spl.mics.parser.Output;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 *
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */

public class FusionSlamService extends MicroService {

    FusionSlam fusionSlam;
    int currentTick = 0;

    List<String> sensors;
    List<TrackedObject> WaitingForPose ;

    StatisticalFolder statisticalFolder;

    /**
     * Constructor for FusionSlamService.
     *
     * @param _fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam _fusionSlam) {
        super("FusionSlamService");
        fusionSlam = _fusionSlam;
        currentTick = 0;
        sensors = new ArrayList<>();
        WaitingForPose = new ArrayList<>();
        this.statisticalFolder = StatisticalFolder.getInstance();

    }



    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        System.out.println("DEBUG: initializing FusionSlamService");
        this.subscribeBroadcast(createdBroadcast.class, (createdBroadcast c)->{
            sensors.add(c.getSenderId());
        });


        this.subscribeEvent(PoseEvent.class, (p) -> {
            while(p.getTime() != currentTick) wait();
                Pose pose = p.getPose();
                fusionSlam.getPosesList().add(pose);
        });

        this.subscribeEvent(TrackedObjectsEvent.class, (TrackedObjectsEvent t)->{
                List<TrackedObject> trackedObjects = t.getTrackedObjects();
                for (TrackedObject tObj : trackedObjects) {
                    if(FusionSlam.getInstance().getPoseTime() != tObj.getTime()){
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
            if(!t.getSenderId().equals("TimeService")){
                sensors.remove(t.getSenderId());
                if(sensors.isEmpty()){
                    Output output = new Output(fusionSlam);
                    createOutputFile(output);
                    this.terminate();
                }
            }
        });

        this.subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast t)->{
            String ErrorMsg = t.getErrorMassage();
            String FaultySensor = t.getSenderName();
            Error_Output error_output = new Error_Output(ErrorMsg, FaultySensor, fusionSlam); //need to fix
            createOutputFile(error_output);
            this.terminate();


        });

        System.out.println("DEBUG: finished initializing FusionSlamService");

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
            CloudPoint oldCoordinate = landMark.getCoordinates().get(i);

            landMark.getCoordinates().set(i, CloudPoint.average(oldCoordinate, newCoordinate));

        }
    }



    private void createOutputFile(Out out){

        String fileName = out instanceof Error_Output ? "error_output.txt" : "output.txt";
        out = out instanceof Error_Output ? (Error_Output) out : (Output) out;

        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void writeOutput(File file, Out out){
        try {
            FileWriter myWriter = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(out);

            myWriter.write(json);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
