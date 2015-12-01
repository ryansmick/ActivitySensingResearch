package research.activitysensingresearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ACCELEROMETER = "accelerometer";
    private static final String TABLE_GYROSCOPE = "gyroscope";
    private static final String TABLE_MAGNETOMETER = "magnetometer";
    private static final String TABLE_SENSORDATA = "sensorData";

    // sensorData Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_ACCELEROMETER_X_VALUE = "accelerometerX";
    private static final String KEY_ACCELEROMETER_Y_VALUE = "accelerometerY";
    private static final String KEY_ACCELEROMETER_Z_VALUE = "accelerometerZ";

    private static final String KEY_GYROSCOPE_X_VALUE = "gyroscopeX";
    private static final String KEY_GYROSCOPE_Y_VALUE = "gyroscopeY";
    private static final String KEY_GYROSCOPE_Z_VALUE = "gyroscopeZ";

    private static final String KEY_AZIMUTH = "azimuth";
    private static final String KEY_PITCH = "pitch";
    private static final String KEY_ROLL = "roll";

    private SensorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SensorDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new SensorDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    //Called when database is created for the first time. Creates the tables to store sensor readings
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENSORDATA_TABLE = "CREATE TABLE " + TABLE_SENSORDATA +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ACCELEROMETER_X_VALUE + " REAL ," +
                KEY_ACCELEROMETER_Y_VALUE + " REAL ," +
                KEY_ACCELEROMETER_Z_VALUE + " REAL" +
                KEY_GYROSCOPE_X_VALUE + " REAL ," +
                KEY_GYROSCOPE_Y_VALUE + " REAL ," +
                KEY_GYROSCOPE_Z_VALUE + " REAL" +
                KEY_AZIMUTH + " REAL ," +
                KEY_PITCH + " REAL ," +
                KEY_ROLL + " REAL" +
                ")";

        db.execSQL(CREATE_SENSORDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //If older version of database exists, drop all old tables and recreate them
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELEROMETER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYROSCOPE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAGNETOMETER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORDATA);
            onCreate(db);
        }
    }

    public void addSensorData(SensorData data){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ACCELEROMETER_X_VALUE, data.getAccelerometerData().getX());
            values.put(KEY_ACCELEROMETER_Y_VALUE, data.getAccelerometerData().getY());
            values.put(KEY_ACCELEROMETER_Z_VALUE, data.getAccelerometerData().getZ());
            values.put(KEY_GYROSCOPE_X_VALUE, data.getGyroscopeData().getX());
            values.put(KEY_GYROSCOPE_Y_VALUE, data.getGyroscopeData().getY());
            values.put(KEY_GYROSCOPE_Z_VALUE, data.getGyroscopeData().getZ());
            values.put(KEY_AZIMUTH, data.getMagnetometerData().getAzimuth());
            values.put(KEY_PITCH, data.getMagnetometerData().getPitch());
            values.put(KEY_ROLL, data.getMagnetometerData().getRoll());

            db.insertOrThrow(TABLE_SENSORDATA, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add sensor data to database");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllRowsFromAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SENSORDATA, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all rows from all tables");
        } finally {
            db.endTransaction();
        }
    }
}


