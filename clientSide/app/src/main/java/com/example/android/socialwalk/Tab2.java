package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 23-10-2015.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tab2 extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    // URL to get contacts JSON
	private static String url = "http://www.meetdoctor.in/socialWalk/index.php";

	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";

	// contacts JSONArray
	JSONArray contacts = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;
    ListView lv;

    public static Tab2 newInstance(int sectionNumber) {
        Tab2 fragment = new Tab2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2, container, false);

        contactList = new ArrayList<HashMap<String, String>>();

        lv = (ListView)v.findViewById(R.id.listView);

        // Listview on item click listener
        lv.setOnItemClickListener(this);
        // Calling async task to get json
        new GetContacts().execute();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // getting values from selected ListItem
        String name = ((TextView) view.findViewById(R.id.name))
                .getText().toString();
        String cost = ((TextView) view.findViewById(R.id.email))
                .getText().toString();
        String description = ((TextView) view.findViewById(R.id.mobile))
                .getText().toString();
        String adres = ((TextView) view.findViewById(R.id.addr)).getText().toString();
        // Starting single contact activity
        Intent in = new Intent(getContext(),SingleContactActivity.class);
        in.putExtra(TAG_NAME, name);
        in.putExtra(TAG_EMAIL, cost);
        in.putExtra(TAG_PHONE_MOBILE, description);
        in.putExtra(TAG_ADDRESS, adres);
        startActivity(in);

    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.d(" HIIIIIIIIII", " ");


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String email = c.getString(TAG_EMAIL);
                        String address = c.getString(TAG_ADDRESS);
                        String gender = c.getString(TAG_GENDER);
                        String mobile = c.getString(TAG_PHONE);

                        // Phone node is JSON Object
                       // JSONObject phone = c.getJSONObject(TAG_PHONE);
                       // String mobile = phone.getString(TAG_PHONE_MOBILE);
                        //String home = phone.getString(TAG_PHONE_HOME);
                        //String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);
                        contact.put(TAG_ADDRESS, address);

                        // adding contact to contact list
                        contactList.add(contact);
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
            adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE, TAG_ADDRESS }, new int[] { R.id.name,
                    R.id.email, R.id.mobile, R.id.addr });

            lv.setAdapter(adapter);
        }

    }

}
