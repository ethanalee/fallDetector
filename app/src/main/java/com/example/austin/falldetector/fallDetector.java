package com.example.austin.falldetector;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public abstract class fallDetector implements SensorEventListener {
    private final double LOWER_THRESHOLD = 6;
    private final double HIGHER_THRESHOLD = 15;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    Queue<Double> magnitudes = new LinkedList<Double>();

    Context activity;

    public fallDetector(Context activity) {
        this.activity = activity;

        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private boolean boolLower = false;
    private boolean boolHigher = false;

    public static boolean fell = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double magnitude = Math.sqrt(x * x + y * y + z * z);

        if (magnitudes.size() < 15)
            magnitudes.add(magnitude);
        else
        {
            double magnitudeAverage = 0;
            for(Double magn : magnitudes){
                magnitudeAverage += magn;
            }
            magnitudeAverage = magnitudeAverage/magnitudes.size();

            if(magnitude/magnitudeAverage > 3){
                onFall();
            }
            else{
                magnitudes.remove();
                magnitudes.add(magnitude);
            }
        }

        Log.d("Magnitude", Double.toString(magnitude));
        Log.d("X Acceleration", Float.toString(event.values[0]));
        Log.d("Y Acceleration", Float.toString(event.values[1]));
    }

    public void onFall(){
        Log.d("Detected Fall", "A fall was detected");
        magnitudes.clear();
    };
    /*
    private boolean checkFall() {
        return boolLower && boolHigher;
    }

    public void reset() {
        boolHigher = false;
        boolLower = false;
    }

    public void resetFall() {
        fell = false;
    }

    private boolean checkLowerThreshold(double magnitude) {
        if (magnitude < LOWER_THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkHigherThreshold(double magnitude) {
        if (magnitude > HIGHER_THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }

    */
    //not necessary, just to satisfy interface for now
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}



