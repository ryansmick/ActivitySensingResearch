package research.activitysensingresearch;

import android.view.accessibility.AccessibilityRecord;

/**
 * Created by Ryan on 12/1/2015.
 */
public class SensorData {
    private AccelerometerData mAccelerometerData;
    private GyroscopeData mGyroscopeData;
    private MagnetometerData mMagnetometerData;

    public SensorData(){
        mAccelerometerData = new AccelerometerData();
        mGyroscopeData = new GyroscopeData();
        mMagnetometerData = new MagnetometerData();
    }

    public AccelerometerData getAccelerometerData(){
        return mAccelerometerData;
    }

    public GyroscopeData getGyroscopeData(){
        return mGyroscopeData;
    }

    public MagnetometerData getMagnetometerData(){
        return mMagnetometerData;
    }

    public void setAccelerometerData(AccelerometerData data){
        mAccelerometerData = data;
    }

    public void setGyroscopeData(GyroscopeData data){
        mGyroscopeData = data;
    }

    public void setMagnetometerData(MagnetometerData data){
        mMagnetometerData = data;
    }
}
