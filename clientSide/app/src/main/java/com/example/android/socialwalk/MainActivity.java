package com.example.android.socialwalk;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
 public static final String PREFS_NAME = "MyPrefsFile";
 SharedPreferences setNoti;
 boolean showTut;
/**
 * The {@link android.support.v4.view.PagerAdapter} that will provide
 * fragments for each of the sections. We use a
 * {@link FragmentPagerAdapter} derivative, which will keep every
 * loaded fragment in memory. If this becomes too memory intensive, it
 * may be best to switch to a
 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
 */
 SectionsPagerAdapter mSectionsPagerAdapter;

/**
 * The {@link ViewPager} that will host the section contents.
 */
 ViewPager mViewPager;

@Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);

 SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
 setNoti = PreferenceManager.getDefaultSharedPreferences(this );
// SharedPref tutorial
 showTut = setNoti.getBoolean("tutorial", true);


 //boolean showTut = settings.getBoolean("tutorial", true);
 //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
 boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
 if(!(hasLoggedIn)){
    Intent open_A = new Intent(this, UserLogin.class);
    startActivity(open_A);
   //  if (showTut == true) {
    //     showActivityOverlay();
     //}
    MainActivity.this.finish();
 }
 if (showTut == true) {
  showActivityOverlay();
 }




 // Create the adapter that will return a fragment for each of the three
 // primary sections of the activity.
 mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

// Set up the ViewPager with the sections adapter.
 mViewPager = (ViewPager) findViewById(R.id.pager);
 mViewPager.setAdapter(mSectionsPagerAdapter);

//Add a tab bar navigation
 TabLayout tabLayout = (TabLayout) findViewById(R.id.tabbar);
 tabLayout.setupWithViewPager(mViewPager);
 }

private void showActivityOverlay() {
 final Dialog dialog = new Dialog(this,
         android.R.style.Theme_Translucent_NoTitleBar);

 dialog.setContentView(R.layout.transparentscreen);

 RelativeLayout layout = (RelativeLayout) dialog
         .findViewById(R.id.mainlayout);
 layout.setBackgroundColor(Color.argb(187, 0, 0, 0));
 layout.setOnClickListener(new OnClickListener() {

     public void onClick(View arg0) {
         PreferenceManager.setDefaultValues(getBaseContext(), R.xml.prefs, true);
         SharedPreferences setNoti = PreferenceManager
                 .getDefaultSharedPreferences(getBaseContext());
         setNoti.edit().putBoolean("tutorial", false).commit();
         showTut = false;
         dialog.dismiss();

     }

 });

 dialog.show();
}



 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 // Inflate the menu; this adds items to the action bar if it is present.
 getMenuInflater().inflate(R.menu.menu_main, menu);


     MenuItem item = menu.findItem(R.id.share);

     // Fetch and store ShareActionProvider
     mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);

     // Return true to display menu
     return true;
 }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


@Override
 public boolean onOptionsItemSelected(MenuItem item) {
 // Handle action bar item clicks here. The action bar will
 // automatically handle clicks on the Home/Up button, so long
 // as you specify a parent activity in AndroidManifest.xml.
 //int id = item.getItemId();
    switch (item.getItemId()){
        case R.id.action_settings:
            Intent i = new Intent(this, Prefs.class);
            startActivity(i);
            break;
        case R.id.share:
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            break;
    }

//noinspection SimplifiableIfStatement
/* if (id == R.id.action_settings) {

     mSettings = PreferenceManager.getDefaultSharedPreferences(this);
     return true;
 } */

return super.onOptionsItemSelected(item);
 }

 /**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
 public class SectionsPagerAdapter extends FragmentPagerAdapter {

public SectionsPagerAdapter(FragmentManager fm) {
 super(fm);
 }

@Override
 public Fragment getItem(int position) {
 // getItem is called to instantiate the fragment for the given page.
 // Return a PlaceholderFragment (defined as a static inner class below).
 switch (position) {
 case 0:
 return PlaceholderFragment.newInstance(position + 1);
 case 1:
 return Tab2.newInstance(position + 1);
 }
 return null;
 }

 @Override
 public int getCount() {
 // Show 2 total pages.
 return 2;
 }

@Override
 public CharSequence getPageTitle(int position) {
 Locale l = Locale.getDefault();
 switch (position) {
 case 0:
 return getString(R.string.title_section1);
 case 1:
 return getString(R.string.title_section2).toLowerCase();

 }
 return null;
 }
 }

}
