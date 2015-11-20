package research.activitysensingresearch;

/**
 * Class to handle sensor timings
 * Created by Ryan Smick on 11/20/2015.
 */
public class SensorTimer {

    private long startTime;

    //Create a new timer object
    public SensorTimer(Boolean startNow){
        if(startNow){
            startTime = System.currentTimeMillis();
        }
    }

    //Start a new timer
    public void start(){
        startTime = System.currentTimeMillis();
    }

    //Stops the timer, returns the difference in time, but leaves the start time unchanged
    public long stop() throws RuntimeException{
        if(startTime < 1){
            throw new RuntimeException("Timer not started");
        }

        long difference = System.currentTimeMillis() - startTime;
        return difference;
    }
}
