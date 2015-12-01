package research.activitysensingresearch;

/**
 * Model for accelerometer data table
 * Created by Ryan Smick on 11/16/2015.
 */
public class AccelerometerData{

    private double X;
    private double Y;
    private double Z;

    public AccelerometerData(){
        X = 0;
        Y = 0;
        Z = 0;
    }
    public AccelerometerData(double x, double y, double z){
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
