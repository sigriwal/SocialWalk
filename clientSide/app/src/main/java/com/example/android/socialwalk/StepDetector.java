package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 06-12-2015.
 */
import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class StepDetector implements SensorEventListener
{
    private final static String TAG = "StepDetector";
    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;


    private float acceleration;

    private float previousY;
    private float currentY;
    private int numSteps;
    private long lastUpdate;
    private long lastUpdateA = 0;
    private float u = 0.00f;

    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;

    private int threshold;

    private static final boolean ADAPTIVE_ACCEL_FILTER = true;
    float lastAccel[] = new float[3];
    float accelFilter[] = new float[3];

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public StepDetector() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    public void setSensitivity(float sensitivity) {
        //mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
        mLimit = 8.66f;
    }

    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

    //public void onSensorChanged(int sensor, float[] values) {
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            float updateFreq = 5; // match this to your update speed
            float cutOffFreq = 0.9f;
            float RC = 1.0f / cutOffFreq;
            float dt = 1.0f / updateFreq;
            float filterConstant = RC / (dt + RC);
            float alpha = filterConstant;
            float kAccelerometerMinStep = 0.033f;
            float kAccelerometerNoiseAttenuation = 3.5f;

            float accelX = event.values[0];
            float accelY = event.values[1];
            float accelZ = event.values[2];

            if (ADAPTIVE_ACCEL_FILTER) {
                float d = clamp(Math.abs(norm(accelFilter[0], accelFilter[1], accelFilter[2]) - norm(accelX, accelY, accelZ)) / kAccelerometerMinStep - 1.0f, 0.0f, 1.0f);
                alpha = d * filterConstant / kAccelerometerNoiseAttenuation + (1.0f - d) * filterConstant;
            }

            accelFilter[0] = (float) (alpha * (accelFilter[0] + accelX - lastAccel[0]));
            accelFilter[1] = (float) (alpha * (accelFilter[1] + accelY - lastAccel[1]));
            accelFilter[2] = (float) (alpha * (accelFilter[2] + accelZ - lastAccel[2]));

            lastAccel[0] = accelX;
            lastAccel[1] = accelY;
            lastAccel[2] = accelZ;

            onFilteredAccelerometerChanged(accelFilter[0], accelFilter[1], accelFilter[2]);
        }
    }

    private float clamp(double v, float min, float max) {
        if(v > max)
            return max;
        else if(v < min)
            return min;
        else
            return (float)v;
    }

    private float norm(float v, float v1, float v2) {
        return (float)Math.sqrt(v*v + v1*v1 + v2*v2);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    private void onFilteredAccelerometerChanged(float v, float v1, float v2) {

        float aX = (float)Math.sqrt(v*v + v1*v1);
        float aY = (float)Math.sqrt(v1*v1 + v2*v2);
        float aZ = (float)Math.sqrt(v * v + v2 * v2);


        float a = (float)Math.sqrt(aX*aX + aY*aY + aZ*aZ);

        if (a > mLimit){
            for (StepListener stepListener : mStepListeners) {
                stepListener.onStep();
            }
        }

    }

}
