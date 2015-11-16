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


        // Start Collecting Sensor Data on push of Start Button
            mStartButton = (Button) findViewById(R.id.start_button);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSensorManager.registerListener(MainActivity.this, mAccelerometer, 50000000);
                    mSensorManager.registerListener(MainActivity.this, mGyroscope, 50000000);
                    mSensorManager.registerListener(MainActivity.this, mMagnetometer, 50000000);
                    acceleration = (TextView)findViewById(R.id.acceleration);
                    orientation = (TextView) findViewById(R.id.orientation);
                    gyroscope = (TextView) findViewById(R.id.gyroscope);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(MainActivity.this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    float[] mGeomagnetic; //geomagnetic sensor
    float[] mGravity; //gravity sensor

    @Override
    public void onSensorChanged(SensorEvent event) {

        DecimalFormat df = new DecimalFormat("#.##");
        SensorDatabaseHelper instance = SensorDatabaseHelper.getInstance(getApplicationContext());

        // Accelerometer
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Gravity constants in the x,y,z direction respectively
            double linear_acceleration[] = new double[3];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            String accelerationText = "Acceleration: \nX: " + df.format(linear_acceleration[0]) +
                    "\nY: " + df.format(linear_acceleration[1]) +
                    "\nZ: " + df.format(linear_acceleration[2]);
            acceleration.setText(accelerationText);

            Log.d(TAG, "Acceleration:" + linear_acceleration[0] + "," + linear_acceleration[1] + "," + linear_acceleration[2]);

            AccelerometerData aData = new AccelerometerData(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
            instance.addAccelerometerData( aData );
        }
        // Gyroscope
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float X = event.values[0];
            float Y = event.values[1];
            float Z = event.values[2];

            String GyroscopeText = "Gyroscope:\nX: " + df.format(X) + "\nY: " + df.format(Y) + "\nZ: " + df.format(Z);
            gyroscope.setText(GyroscopeText);

            Log.d(TAG, "Gyroscope:" + X + "," + Y + "," + Z);
            GyroscopeData gData = new GyroscopeData( X, Y, Z);
            instance.addGyroscopeData( gData );
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //Orientation sensor
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation2[] = new float[3];
                    SensorManager.getOrientation(R, orientation2);

                    String orientationText = "Orientation: \n -Z: " + df.format(orientation2[0]) +"\n-X: " + df.format(orientation2[1]) + "\nY: " + df.format(orientation2[2]);
                    orientation.setText(orientationText);

                    Log.d(TAG, "Gyroscope:" + orientation2[0] + "," + orientation2[1] + "," + orientation2[2]);

                    MagnetometerData mData = new MagnetometerData( orientation2[0], orientation2[1], orientation2[2]) ;
                    instance.addMagnetometerData( mData );
                }
            }
        }
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
