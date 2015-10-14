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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Initialize Sensors
        private SensorManager mSensorManager;
            private Sensor mAccelerometer;
            private Sensor mGyroscope;
            private Sensor mGeomagnetic;

    // Initialize Textview
        TextView acceleration;
        TextView gyroscope;

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
                mGeomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);

        // Start Collecting Sensor Data on push of Start Button
            mStartButton = (Button) findViewById(R.id.start_button);
            mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(MainActivity.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                acceleration = (TextView)findViewById(R.id.acceleration);
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
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Gravity constants in the x,y,z direction respectively
            double linear_acceleration[] = new double[3];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            acceleration.setText("X: " + linear_acceleration[0] +
                    "\nY: " + linear_acceleration[1] +
                    "\nZ: " + linear_acceleration[2]);

            Log.d(TAG, linear_acceleration[0] + "," + linear_acceleration[1] + "," + linear_acceleration[2]);
        }
        // Gyroscope
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float X = event.values[0];
            float Y = event.values[1];
            float Z = event.values[2];

            gyroscope.setText("Gyroscope:\nX: " + X + "\nY: " + Y + "\nZ: " + Z);
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

    private float[] matrixMultiplication(float[] a, float[] b)
    {
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }
}
