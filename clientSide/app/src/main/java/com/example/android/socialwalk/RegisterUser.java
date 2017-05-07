package com.example.android.socialwalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
 * Created by Sigriwal3 on 27-12-2015.
 */
public class RegisterUser extends AppCompatActivity {

    EditText contact_no, password, email;
    Button bRegister;
    private String resp;
    String status = null;
    static InputStream is = null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Intent in = getIntent();
        contact_no = (EditText) findViewById(R.id.mobileNumber);
        password = (EditText) findViewById(R.id.userPass);
        email = (EditText) findViewById(R.id.email_of_user);
        bRegister = (Button) findViewById(R.id.userReg);

//        bRegister.setOnClickListener(this);
    }

    //@Override
    //public void onClick(View v) {
     //   String mob_num = contact_no.getText().toString();
     //   String userEmail = email.getText().toString();
     //   String pass = password.getText().toString();

//        insertInDb(mob_num, userEmail, pass);
  //  }

    public void registration_of_user(View view){
        String mob_num = contact_no.getText().toString();
        String userEmail = email.getText().toString();
        String pass = password.getText().toString();

        insertInDb(mob_num, userEmail, pass);
    }

    private void insertInDb(final String mob_num, final String userEmail, final String pass) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String paramUsermobile = params[0];
                String paramEmail = params[1];
                String paramPassword = params[2];


                String mob = mob_num;
                String mail = userEmail;
                String password = pass;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("contact_num", mob));
                nameValuePairs.add(new BasicNameValuePair("mail", mail));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://www.meetdoctor.in/socialWalk/registerUser.php");
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

                    SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    //Set "hasLoggedIn" to true
                    editor.putBoolean("hasLoggedIn", true);
                    // Commit the edits!
                    editor.commit();

                    Intent open_A = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(open_A);
                    RegisterUser.this.finish();
                }

                //Intent open_A = new Intent("com.example.android.socialwalk.MAIN");
                //startActivity(open_A);
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mob_num, userEmail, pass);
    }
}
