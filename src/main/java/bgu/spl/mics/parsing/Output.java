package bgu.spl.mics.parsing;

import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.LandMark;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.List;

public class Output implements Out {

    StatisticalFolder statisticalFolder;
    List<LandMark> landmarks;

    public Output(FusionSlam fs) {
        this.statisticalFolder = StatisticalFolder.getInstance();
        this.landmarks = fs.getLandmarksList();
    }
}
