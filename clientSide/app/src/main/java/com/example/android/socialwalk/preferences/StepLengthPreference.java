package com.example.android.socialwalk.preferences;

/**
 * Created by Sigriwal3 on 06-12-2015.
 */
import com.example.android.socialwalk.R;
import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class StepLengthPreference extends EditMeasurementPreference {

    public StepLengthPreference(Context context) {
        super(context);
    }
    public StepLengthPreference(Context context, AttributeSet attr) {
        super(context, attr);
    }
    public StepLengthPreference(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    protected void initPreferenceDetails() {
        mTitleResource = R.string.height_setting_title;
        mMetricUnitsResource = R.string.centimeters;
        mImperialUnitsResource = R.string.inches;
    }
}

