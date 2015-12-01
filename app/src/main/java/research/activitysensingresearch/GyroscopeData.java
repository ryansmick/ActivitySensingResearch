package research.activitysensingresearch;

/**
 * Model for Gyroscope data table
 * Created by Ryan Smick on 11/16/2015.
 */
public class GyroscopeData {

    private double X; //azimuth around -z axis
    private double Y; //pitch around -x axis
    private double Z; //roll around y axis

    public GyroscopeData(){
        X = 0;
        Y = 0;
        Z = 0;
    }

    public GyroscopeData(double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
    }

    public double getX(){
        return X;
    }

    public double getY(){
        return Y;
    }

    public double getZ(){
        return Z;
    }

    public void setX(double x){
        X = x;
    }

    public void setY(double y){
        Y = y;
    }

    public void setZ(double z){
        Z = z;
    }
}
