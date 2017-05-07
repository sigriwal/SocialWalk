package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 06-12-2015.
 */
import android.os.Handler;
import android.os.Message;

import static com.example.android.socialwalk.StepService.*;

/**
 * Calculates and displays the approximate calories.
 * @author Levente Bagi
 */
public class CaloriesNotifier implements StepListener, SpeakingTimer.Listener {

    public interface Listener {
        public void valueChanged(float value);
        public void passValue();
    }
    private Listener mListener;

    private static double METRIC_RUNNING_FACTOR = 1.02784823;
//    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    private static double METRIC_WALKING_FACTOR = 0.708;
    //  private static double IMPERIAL_WALKING_FACTOR = 0.517;

    private float mSpeedValue;
    private double mCalories = 0;

    PedometerSettings mSettings;
    Utils mUtils;

    boolean mIsMetric;
    boolean mIsRunning;
    float mStepLength;
    float mBodyWeight;

    public CaloriesNotifier(Listener listener, PedometerSettings settings, Utils utils) {
        mListener = listener;
        mUtils = utils;
        mSettings = settings;
        reloadSettings();
    }
    public void setCalories(float calories) {
        mCalories = calories;
        notifyListener();
    }
    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        // mIsRunning = mSettings.isRunning();
        mStepLength = mSettings.getStepLength();
        mBodyWeight = mSettings.getBodyWeight();
        notifyListener();
    }
    public void resetValues() {
        mCalories = 0;
    }

    public void isMetric(boolean isMetric) {
        mIsMetric = isMetric;
    }
    public void setStepLength(float stepLength) {
        mStepLength = stepLength;
    }

    private ICallback mCallback = new ICallback() {

        @Override
        public void stepsChanged(int value) {

        }

        @Override
        public void paceChanged(int value) {

        }

        @Override
        public void distanceChanged(float value) {

        }

        public void speedChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int)(value*1000), 0));
        }

        @Override
        public void caloriesChanged(float value) {

        }
    };
    private static final int SPEED_MSG = 4;
    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            mSpeedValue = ((int)msg.arg1)/1000f;
        }

    };

    public void onStep() {

        //if (mIsMetric) {
        if(mSpeedValue < 8){
            mCalories +=
                    (mBodyWeight * (METRIC_WALKING_FACTOR))
                            // Distance:
                            * mStepLength // centimeters
                            / 100000.0; // centimeters/kilometer
        }else {
            mCalories +=
                    (mBodyWeight * (METRIC_RUNNING_FACTOR))
                            // Distance:
                            * mStepLength // centimeters
                            / 100000.0; // centimeters/kilometer
        }
        // else {
        //    mCalories +=
        //        (mBodyWeight * (mIsRunning ? IMPERIAL_RUNNING_FACTOR : IMPERIAL_WALKING_FACTOR))
        // Distance:
        //        * mStepLength // inches
        //        / 63360.0; // inches/mile
        //}

        notifyListener();
    }

    private void notifyListener() {
        mListener.valueChanged((float)mCalories);
    }

    public void passValue() {

    }

    public void speak() {
        //    if (mSettings.shouldTellCalories()) {
        //        if (mCalories > 0) {
        //            mUtils.say("" + (int)mCalories + " calories burned");
        //        }
        //    }

    }


}

