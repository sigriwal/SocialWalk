package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 06-12-2015.
 */
import android.content.SharedPreferences;


public class PedometerSettings {

    SharedPreferences mSettings;

    public static int M_SPEED = 3;

    public PedometerSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    public boolean isMetric() {
        // return mSettings.getString("units", "imperial").equals("metric");
        return true;
    }

    public float getStepLength() {
        try {
            float stepLength;
            float height = Float.valueOf(mSettings.getString("step_length", "120").trim());
            if(height > 183){
                stepLength = (float)(183*(0.41));
                return stepLength;
            }else {
                stepLength = (float)(height*(0.41));
                return stepLength;
            }
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }

    public float getBodyWeight() {
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim());
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }


    public boolean wakeAggressively() {
        //return mSettings.getString("operation_level", "run_in_background").equals("wake_up");
        return true;
    }
    public boolean keepScreenOn() {
        //return mSettings.getString("operation_level", "run_in_background").equals("keep_screen_on");
        return true;
    }

    //
    // Internal

    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", Utils.currentTimeInMillis());
        editor.commit();
    }

    public void saveServiceRunningWithNullTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }

    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < Utils.currentTimeInMillis() - 1000*60*10;
    }

}

