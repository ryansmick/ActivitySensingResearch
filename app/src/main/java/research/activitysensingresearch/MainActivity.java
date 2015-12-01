package research.activitysensingresearch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import research.activitysensingresearch.SensorDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Initialize Sensors
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mMagnetometer;

    //Declare variable to hold current SensorDatabaseHelper instance
    SensorDatabaseHelper instance;

    //Declare timers for sensors
    SensorTimer accelTimer = new SensorTimer(false);
    SensorTimer gyroTimer = new SensorTimer(false);
    SensorTimer orientationTimer = new SensorTimer(false);

    //Sensor data
    SensorData mData = new SensorData();

    Boolean hasAccelData = false;
    Boolean hasGyroData = false;
    Boolean hasMagnetData = false;

    //Timer delay in milliseconds
    int timerDelay = 500;


    // Initialize Textview
    TextView acceleration;
    TextView gyroscope;
    TextView orientation;

    // Initialize Buttons for UI
    private Button mStartButton;
    private Button mStopButton;

    // Initialize values for Gyroscope calculation
    private static final String TAG = "MainActivity";

    // Declare rotation current
    private float[] gravity = {0, (float) 9.81, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        acceleration = (TextView)findViewById(R.id.acceleration);
        orientation = (TextView) findViewById(R.id.orientation);
        gyroscope = (TextView) findViewById(R.id.gyroscope);

        try {
            if (savedInstanceState.getBoolean("InProgress")) {
                mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(MainActivity.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(MainActivity.this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                accelTimer.start();
                gyroTimer.start();
                orientationTimer.start();
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }


        // Start Collecting Sensor Data on push of Start Button
            mStartButton = (Button) findViewById(R.id.start_button);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(MainActivity.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(MainActivity.this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                    accelTimer.start();
                    gyroTimer.start();
                    orientationTimer.start();
                }
            });

        // Stop Collecting Sensor Data on push of Stop Button
            mStopButton = (Button) findViewById(R.id.stop_button);
            mStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSensorManager.unregisterListener(MainActivity.this);
                }
            });

        //Get the current instance of the SensorDatabaseHelper
        instance = SensorDatabaseHelper.getInstance(getApplicationContext());

        //Delete all rows in all tables to correct current issue
        instance.deleteAllRowsFromAllTables();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(MainActivity.this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    float[] mGravity=new float[3]; //gravity sensor

    @Override
    public void onSensorChanged(SensorEvent event) {

        DecimalFormat df = new DecimalFormat("#.##");

        // Accelerometer
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && accelTimer.stop() >= timerDelay) {
            // Gravity constants in the x,y,z direction respectively
            double linear_acceleration[] = new double[3];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            //Set mGravity to the most recent accelerometer readings
            for(int i = 0; i < 3; i++){
                mGravity[i] = (float) linear_acceleration[i];
            }

            String accelerationText = "Acceleration: \nX: " + df.format(linear_acceleration[0]) +
                    "\nY: " + df.format(linear_acceleration[1]) +
                    "\nZ: " + df.format(linear_acceleration[2]);
            acceleration.setText(accelerationText);

            Log.d(TAG, "Acceleration:" + linear_acceleration[0] + "," + linear_acceleration[1] + "," + linear_acceleration[2]);

            mData.setAccelerometerData(new AccelerometerData(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]));
            hasAccelData = true;
            accelTimer.start();
        }
        // Gyroscope
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE && gyroTimer.stop() >= timerDelay) {

            float X = event.values[0];
            float Y = event.values[1];
            float Z = event.values[2];

            String GyroscopeText = "Gyroscope:\nX: " + df.format(X) + "\nY: " + df.format(Y) + "\nZ: " + df.format(Z);
            gyroscope.setText(GyroscopeText);

            Log.d(TAG, "Gyroscope:" + X + "," + Y + "," + Z);
            mData.setGyroscopeData(new GyroscopeData( X, Y, Z));
            hasGyroData = true;
            gyroTimer.start();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && orientationTimer.stop() >= timerDelay) {
            //Orientation sensor
            if (mGravity != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, event.values);
                if (success) {
                    float orientation2[] = new float[3];
                    SensorManager.getOrientation(R, orientation2);

                    String orientationText = "Orientation: \n -Z: " + df.format(orientation2[0]) +"\n-X: " + df.format(orientation2[1]) + "\nY: " + df.format(orientation2[2]);
                    orientation.setText(orientationText);

                    Log.d(TAG, "Orientation:" + orientation2[0] + "," + orientation2[1] + "," + orientation2[2]);

                    mData.setMagnetometerData(new MagnetometerData( orientation2[0], orientation2[1], orientation2[2]));
                    hasMagnetData = true;
                }
            }
            orientationTimer.start();
        }

        if(hasAccelData && hasGyroData && hasMagnetData){
            instance.addSensorData(mData);
            hasAccelData = false;
            hasGyroData = false;
            hasMagnetData = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("InProgress", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
