package com.example.android.socialwalk;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Sigriwal3 on 13-02-2016.
 */
public class Prefs extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
