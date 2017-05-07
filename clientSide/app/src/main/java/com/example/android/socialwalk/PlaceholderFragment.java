package com.example.android.socialwalk;

/**
 * Created by Sigriwal3 on 23-10-2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderFragment extends Fragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int MENU_REMOVE = 1;
    ListAdapter adapter;
    View rootView;
    ListView myList;
    ArrayList<HashMap<String, String>> userList;



    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        myList = (ListView) rootView.findViewById(R.id.list);
        DBController controller = new DBController(getContext());

        userList = controller.getAllUsers();
        Log.d("UserList", " " + userList);
        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView
            adapter = new SimpleAdapter(getActivity(), userList, R.layout.view_contacts, new String[] {
                    "id", "Name", "Email", "phone_number" }, new int[] { R.id.userId, R.id.userName, R.id.userEmail, R.id.userMobile });

            registerForContextMenu(myList);
            myList.setAdapter(adapter);
        }else {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", String.valueOf(0));
            map.put("msg", "Your Support List is Empty!");
            map.put("msgNull", " ");
            map.put("msgBlank", " ");
            userList.add(map);
            adapter = new SimpleAdapter(getActivity(),userList, R.layout.view_contacts, new String[]{"id", "msg", "msgNull", "msgBlank"}, new int[]{R.id.userId, R.id.userName, R.id.userEmail, R.id.userMobile });
            myList.setAdapter(adapter);
            Log.d("EmptyList", " " + userList);
        }

        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("Hello", String.valueOf(v.getId()));
        menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_REMOVE:

                String pan = String.valueOf(adapter.getItem(info.position));

                int c = pan.indexOf("id");
                int s = c+3;
                int e;
                for(e=s; e<pan.length();e++){
                    char p = pan.charAt(e);
                    if (p == ',') {
                        break;
                    }
                }

                String sub = pan.substring(s,e);

                DBController controller = new DBController(getContext());
                controller.deleteContact(sub);

                updateList();


            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateList() {
        DBController controller = new DBController(getContext());
        userList = controller.getAllUsers();
        Log.d("MyList"," "+userList);
        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView
            adapter = new SimpleAdapter(getActivity(), userList, R.layout.view_contacts, new String[] {
                    "id", "Name", "Email", "phone_number" }, new int[] { R.id.userId, R.id.userName, R.id.userEmail, R.id.userMobile });

            registerForContextMenu(myList);
            myList.setAdapter(adapter);
        }else {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", String.valueOf(0));
            map.put("msg", "Your Support List is Empty!");
            map.put("msgNull", " ");
            map.put("msgBlank", " ");
            userList.add(map);
            adapter = new SimpleAdapter(getActivity(),userList, R.layout.view_contacts, new String[]{"id", "msg", "msgNull", "msgBlank"}, new int[]{R.id.userId, R.id.userName, R.id.userEmail, R.id.userMobile });
            myList.setAdapter(adapter);
            Log.d("EmptyList", " " + userList);
        }

    }

}
