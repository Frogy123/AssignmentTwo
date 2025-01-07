package bgu.spl.mics.parsing.parsers;

import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.parsing.configurations.CameraConfiguration;
import bgu.spl.mics.parsing.configurations.SystemConfiguration;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class SystemConfigurationParser {
    public static SystemConfiguration parse(String configFilePath) {
        // Create Gson object
        Gson gson = new Gson();
        SystemConfiguration config = null;
        try {
            // Read the config file
            FileReader configFileReader = new FileReader(configFilePath);
            config = gson.fromJson(configFileReader, SystemConfiguration.class);
            configFileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
