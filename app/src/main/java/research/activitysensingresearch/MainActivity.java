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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Initialize Sensors
        private SensorManager mSensorManager;
            private Sensor mAccelerometer;
            private Sensor mGyroscope;
            private Sensor mGeomagnetic;

    // Initialize Buttons for UI
        private Button mStartButton;
        private Button mStopButton;

    // Initialize values for Gyroscope calculation
        private static final String TAG = "MainActivity";
        private static final float NS2S = 1.0f / 1000000000.0f;
        private final float[] deltaRotationVector = new float[4];
        private float timestamp;

    // Declare rotation current
        private float[] rotationCurrent = new float[9];
        private float[] inclinationMatrix = new float[9];
        private float[] gravity = {0, (float) 9.81, 0};
        private float[] geomagnetic =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Sensors
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                mGeomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);

        // Start Collecting Sensor Data on push of Start Button
            mStartButton = (Button) findViewById(R.id.start_button);
            mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(MainActivity.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.getRotationMatrix(rotationCurrent, inclinationMatrix, gravity,mGeomagnetic );
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // we probably won't use this
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Accelerometer
            // Gravity constants in the x,y,z direction respectively
            double linear_acceleration[] = new double[3];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            Log.d(TAG, linear_acceleration[0] + "," + linear_acceleration[1] + "," + linear_acceleration[2]);

        // Gyroscope
            if (timestamp !=0 ) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of rotation sample (not normalized yet)
                    float axisX = event.values[0];
                    float axisY = event.values[1];
                    float axisZ = event.values[2];

                // Calculate angular speed of the sample
                    float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // Integrate around this axis with the angular speed by the timestep in order to get a delta
                // rotation from this sample over the timestep. We will convert this axis-angle representation
                // of the delta rotation into a quaternion before turning it into the rotation matrix.

                    float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                    deltaRotationVector[0] = sinThetaOverTwo * axisX;
                    deltaRotationVector[1] = sinThetaOverTwo * axisY;
                    deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                    deltaRotationVector[3] = cosThetaOverTwo;
            }

            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);

            rotationCurrent = rotationCurrent*deltaRotationMatrix;

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
