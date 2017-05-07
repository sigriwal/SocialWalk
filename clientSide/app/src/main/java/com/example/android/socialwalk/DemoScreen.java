package com.example.android.socialwalk;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.socialwalk.R;

/**
 * Created by Sigriwal3 on 07-02-2016.
 */
public class DemoScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparentscreen);
        showOverLay();
    }
    private void showOverLay(){

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(R.layout.transparentscreen);

        RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.mainlayout);

        layout.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                dialog.dismiss();
                Intent open_A = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(open_A);

            }

        });



    }
}


