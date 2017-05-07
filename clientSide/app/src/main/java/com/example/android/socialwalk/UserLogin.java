package com.example.android.socialwalk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sigriwal3 on 20-12-2015.
 */
public class UserLogin extends AppCompatActivity {

    //public static final String PREFS_NAME = "MyPrefsFile";
    EditText user_contact_no, password;
    Button bSignIn, bNewUser;
    TextView forgetPass;
    private String resp;
    String status = null;
    static InputStream is = null;
    //TextView InvalidLogin= new TextView(this);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user_contact_no = (EditText) findViewById(R.id.user_mob_no);
        password = (EditText) findViewById(R.id.pass);
        bSignIn = (Button) findViewById(R.id.signIn);
        bNewUser = (Button) findViewById(R.id.buttonNewUser);
        forgetPass = (TextView) findViewById(R.id.forgetPass);

        bNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pan :", "  user REgistration");
                Intent open_A = new Intent("com.example.android.socialwalk.REGISTERUSER");
                startActivity(open_A);
                Log.d("Pan :", "  user Failed");
            }
        });
        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mob_num = user_contact_no.getText().toString();
                String pass = password.getText().toString();

                checkInDb(mob_num, pass);

            }
        });
    }

    private void checkInDb(final String mob_num, final String pass) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsermobile = params[0];
                String paramPassword = params[1];


                String mob = mob_num;
                String password = pass;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("contact_num", mob));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://www.meetdoctor.in/socialWalk/checkUser.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso_8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine())!=null){
                        sb.append(line+"\n");
                    }
                    is.close();
                    resp = sb.toString();
                    Log.d("  Hello", resp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    status = jsonObject.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equalsIgnoreCase("success")) {
                    Log.d("  MyStata :", " "+status);
                    SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    //Set "hasLoggedIn" to true
                    editor.putBoolean("hasLoggedIn", true);
                    // Commit the edits!
                    editor.commit();

                    Intent open_A = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(open_A);
                    UserLogin.this.finish();
                }
                /*else
                {
                    InvalidLogin.setText("  Incorrect Username or Password !!  ");
                }*/
                //Intent open_A = new Intent("com.example.android.socialwalk.MAIN");
                //startActivity(open_A);
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mob_num, pass);
    }

    //public void registerUser(View view){
      //  Intent open_A = new Intent(this, RegisterUser.class);
        //startActivity(open_A);
    //}
}
