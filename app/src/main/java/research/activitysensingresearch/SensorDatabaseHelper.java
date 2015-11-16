package research.activitysensingresearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.util.Log;

/**
 * Facilitates setting up and adding data to our Sensor database
 * Created by Ryan Smick on 11/16/2015.
 */
public class SensorDatabaseHelper extends SQLiteOpenHelper{
    private static SensorDatabaseHelper sInstance;

    public static final String TAG = "SensorDatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "sensorDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ACCELEROMETER = "accelerometer";
    private static final String TABLE_GYROSCOPE = "gyroscope";
    private static final String TABLE_MAGNETOMETER = "magnetometer";

    // Accelerometer Table Columns
    private static final String KEY_ACCELEROMETER_ID = "id";
    private static final String KEY_ACCELEROMETER_X_VALUE = "X";
    private static final String KEY_ACCELEROMETER_Y_VALUE = "Y";
    private static final String KEY_ACCELEROMETER_Z_VALUE = "Z";

    // Gyroscope Table Columns
    private static final String KEY_GYROSCOPE_ID = "id";
    private static final String KEY_GYROSCOPE_X_VALUE = "X";
    private static final String KEY_GYROSCOPE_Y_VALUE = "Y";
    private static final String KEY_GYROSCOPE_Z_VALUE = "Z";

    // Magnetometer Table Columns
    private static final String KEY_MAGNETOMETER_ID = "id";
    private static final String KEY_AZIMUTH = "azimuth";
    private static final String KEY_PITCH = "pitch";
    private static final String KEY_ROLL = "roll";

    private SensorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SensorDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SensorDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    //Called when database is created for the first time. Creates the tables to store sensor readings
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCEL_TABLE = "CREATE TABLE " + TABLE_ACCELEROMETER +
                "(" +
                KEY_ACCELEROMETER_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ACCELEROMETER_X_VALUE + " REAL ," +
                KEY_ACCELEROMETER_Y_VALUE + " REAL ," +
                KEY_ACCELEROMETER_Z_VALUE + " REAL" +
                ")";

        String CREATE_GYRO_TABLE = "CREATE TABLE " + TABLE_GYROSCOPE +
                "(" +
                KEY_GYROSCOPE_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_GYROSCOPE_X_VALUE + " REAL ," +
                KEY_GYROSCOPE_Y_VALUE + " REAL ," +
                KEY_GYROSCOPE_Z_VALUE + " REAL" +
                ")";

        String CREATE_MAGNETOMETER_TABLE = "CREATE TABLE " + TABLE_MAGNETOMETER +
                "(" +
                KEY_MAGNETOMETER_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_AZIMUTH + " REAL ," +
                KEY_PITCH + " REAL ," +
                KEY_ROLL + " REAL" +
                ")";

        db.execSQL(CREATE_ACCEL_TABLE);
        db.execSQL(CREATE_GYRO_TABLE);
        db.execSQL(CREATE_MAGNETOMETER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //If older version of database exists, drop all old tables and recreate them
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELEROMETER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYROSCOPE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAGNETOMETER);
            onCreate(db);
        }
    }

    public void addAccelerometerData(AccelerometerData data) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ACCELEROMETER_X_VALUE, data.X);
            values.put(KEY_ACCELEROMETER_Y_VALUE, data.Y);
            values.put(KEY_ACCELEROMETER_Z_VALUE, data.Z);

            db.insertOrThrow(TABLE_ACCELEROMETER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add accelerometer to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addGyroscopeData(GyroscopeData data) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_GYROSCOPE_X_VALUE, data.X);
            values.put(KEY_GYROSCOPE_Y_VALUE, data.Y);
            values.put(KEY_GYROSCOPE_Z_VALUE, data.Z);

            db.insertOrThrow(TABLE_GYROSCOPE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add gyroscope data to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addMagnetometerData(MagnetometerData data) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_AZIMUTH, data.azimuth);
            values.put(KEY_PITCH, data.pitch);
            values.put(KEY_ROLL, data.roll);

            db.insertOrThrow(TABLE_MAGNETOMETER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add magnetometer data to database");
        } finally {
            db.endTransaction();
        }
    }
}


