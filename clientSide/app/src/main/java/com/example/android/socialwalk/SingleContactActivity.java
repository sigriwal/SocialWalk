package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 03-11-2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.graphics.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SingleContactActivity  extends ListActivity {

    // URL to get contacts JSON
    private static String url = "http://www.meetdoctor.in/socialWalk/getSponsors.php";

    //Shared Pref
    public static final String NGO_INFO = "MyNGOInfo";
    // JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_SPONSORS_NAME = "sponsor_name";
    private static final String TAG_DESC = "sponsor_description";
    private static final String TAG_ID = "sponsor_id";
    private static final String TAG_SPONSORS = "sponsors";

    String name = "";
    String email = "";
    String mobile = "";
    String address = "";

    JSONArray sponsors = null;
    //Button support = (Button)findViewById(R.id.supportButton);

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> sponsorList;
    ListView lv;

    HashMap<String, String> queryValues;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);

        //NGO Info
        SharedPreferences ngo_log = getSharedPreferences(SingleContactActivity.NGO_INFO, 0);
       // String ngo_name  = ngo_log.getString("ngo_name", "n");
       // String ngo_id = ngo_log.getString("ngo_id", "0");

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#512DA8")));
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        name = in.getStringExtra(TAG_NAME);
        email = in.getStringExtra(TAG_EMAIL);
        mobile = in.getStringExtra(TAG_PHONE_MOBILE);
        address = in.getStringExtra(TAG_ADDRESS);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblEmail = (TextView) findViewById(R.id.email_label);
        TextView lblMobile = (TextView) findViewById(R.id.mobile_label);
        TextView lblAddress = (TextView)findViewById(R.id.addr1);

        lblName.setText(name);
        lblEmail.setText(email);
        lblMobile.setText(mobile);
        lblAddress.setText(address);

        sponsorList = new ArrayList<HashMap<String, String>>();

        lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // Calling async task to get json
        new GetSponsors().execute();
    }

    public void supportList(View view){

        SharedPreferences ngo_log = getSharedPreferences(SingleContactActivity.NGO_INFO, 0);
        SharedPreferences.Editor editor = ngo_log.edit();

        editor.putString("ngo_name", name);
        editor.putString("ngo_id", mobile);
        editor.commit();

        // DB QueryValues Object to insert into SQLite
        //queryValues = new HashMap<String, String>();
        //queryValues.put("userName", name);
        //queryValues.put("userEmail", email);
        //queryValues.put("userMobile", mobile);

        //DBController controller = new DBController(this);
        //controller.insertUser(queryValues);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent open_A = new Intent("com.example.android.socialwalk.PEDOMETER");
                    startActivity(open_A);
                }
            }
        };
        timer.start();
    }


    private class GetSponsors extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            FetchSponsors fh = new FetchSponsors();


            // Making a request to url and getting response
            String jsonStr = fh.makeServiceCall(url, FetchSponsors.POST);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    sponsors = jsonObj.getJSONArray(TAG_SPONSORS);

                    // looping through All Contacts
                    for (int i = 0; i < sponsors.length(); i++) {
                        JSONObject c = sponsors.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_SPONSORS_NAME);
                        String desc = c.getString(TAG_DESC);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_SPONSORS_NAME, name);
                        contact.put(TAG_DESC, desc);

                        // adding contact to contact list
                        sponsorList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter;
            adapter = new SimpleAdapter(SingleContactActivity.this, sponsorList,
                    R.layout.sponsor_list, new String[] { TAG_SPONSORS_NAME}, new int[] { R.id.buttonSponsor});
            lv.setAdapter(adapter);
        }

    }

}
