package research.activitysensingresearch;

/**
 * Model for magnetometer data table
 * Created by Ryan Smick on 11/16/2015.
 */
public class MagnetometerData {

    private double azimuth;
    private double pitch;
    private double roll;

    public MagnetometerData(){
        azimuth = 0;
        pitch = 0;
        roll = 0;
    }

    public MagnetometerData(double azimuth, double pitch, double roll){
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getAzimuth(){
        return azimuth;
    }

    public double getPitch(){
        return pitch;
    }

    public double getRoll(){
        return roll;
    }

    public void setAzimuth(double azimuth){
        this.azimuth = azimuth;
    }

    public void setPitch(double pitch){
        this.pitch = pitch;
    }

    public void setRoll(double roll){
        this.roll = roll;
    }


}
