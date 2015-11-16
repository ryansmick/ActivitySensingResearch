package research.activitysensingresearch;

/**
 * Model for magnetometer data table
 * Created by Ryan Smick on 11/16/2015.
 */
public class MagnetometerData {

    public double azimuth;
    public double pitch;
    public double roll;

    public MagnetometerData(double azimuth, double pitch, double roll){
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }
}
