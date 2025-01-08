package bgu.spl.mics.application.objects;

import bgu.spl.mics.parsing.parsers.LidarDataParser;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private List<StampedCloudPoints> data;
    private static AtomicReference<String> path = new AtomicReference<>(null);;

    private static class InstanceHolder {
        private static LiDarDataBase instance = new LiDarDataBase();
    }

    private LiDarDataBase() {
        System.out.println(path);
        data = LidarDataParser.parse(path.get());
    }



    /**
     * Returns the singleton instance of LiDarDataBase.
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance(String filePath) {
        path.compareAndSet(null,filePath);
        return InstanceHolder.instance;
    }

    public static LiDarDataBase getInstance() {
        return InstanceHolder.instance;
    }


    /**
     * searches for specific points
     * @param id - the id of the object
     * @param time - time of detection of the object
     * @returns the matching cloud point if exists, otherwise, null
     */
    public List<CloudPoint> findCloudPoints(String id, int time){
        for (StampedCloudPoints stampedCloudPoints: data){
            if (stampedCloudPoints.getId().equals(id) && stampedCloudPoints.getTime() == time)
                return stampedCloudPoints.getCloudPoints();
        }
        return null;
    }




}
