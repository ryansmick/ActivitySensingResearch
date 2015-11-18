package research.activitysensingresearch;

/**
 * Model for Gyroscope data table
 * Created by Ryan Smick on 11/16/2015.
 */
public class GyroscopeData {

    public double X; //azimuth around -z axis
    public double Y; //pitch around -x axis
    public double Z; //roll around y axis

    public GyroscopeData(double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
    }
}
