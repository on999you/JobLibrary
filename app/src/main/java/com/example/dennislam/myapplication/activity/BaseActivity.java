package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.GlobalClass;
import com.example.dennislam.myapplication.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog loadingInternetDialog;
    protected DrawerLayout mDrawer;
    GlobalClass globalVariable;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        if("zh".equals(prefs.getString("Language",""))){
            Locale myLocale = new Locale("zh");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Locale.setDefault(myLocale);
        }else{
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Locale.setDefault(myLocale);
        }

        globalVariable = (GlobalClass) getApplicationContext();

        res = getResources();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Detect contains network or not
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();

        if(info == null){
            globalVariable.setNetwork(false);

            //Dialog
            new MaterialDialog.Builder(this)
                    .title(res.getString(R.string.baseAct_reminder1))
                    .content(res.getString(R.string.baseAct_reminder2))
                    .positiveText(res.getString(R.string.baseAct_reminder3))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .show();
        }
        else {
            globalVariable.setNetwork(true);
            Log.v("vbn", "contain network");
        }
        try {
            // Get the font size value from SharedPreferences.
            SharedPreferences settings =
                    getSharedPreferences("fontSizeSetting", Context.MODE_PRIVATE);

            // Get the font size option.  We use "FONT_SIZE" as the key.
            // Make sure to use this key when you set the value in SharedPreferences.
            // We specify "Medium" as the default value, if it does not exist.
            String fontSizePref = settings.getString("FONT_SIZE", "Medium");

            // Select the proper theme ID.
            // These will correspond to your theme names as defined in themes.xml.
            int themeID = R.style.FontSizeMedium;
            if (fontSizePref == "Small") {
                themeID = R.style.FontSizeSmall;
            }
            else if (fontSizePref == "Large") {
                themeID = R.style.FontSizeLarge;
            }

            // Set the theme for the activity.
            setTheme(themeID);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home) {
            Intent homePageIntent = new Intent(getBaseContext(), MainPageActivity.class);
            startActivity(homePageIntent);

        } else if(id == R.id.nav_search) {
            Intent searchJobsIntent = new Intent(getBaseContext(), SearchJobsActivity.class);
            startActivity(searchJobsIntent);

        } else if (id == R.id.nav_applied) {
            Intent appliedJobIntent = new Intent(getBaseContext(), AppliedJobActivity.class);
            startActivity(appliedJobIntent);

        } else if (id == R.id.nav_cv) {
            Intent cvIntent = new Intent(getBaseContext(), CvActivity.class);
            startActivity(cvIntent);

        } else if (id == R.id.nav_salary) {
            Intent salaryCheckIntent = new Intent(getBaseContext(), SalaryCheckActivity.class);
            startActivity(salaryCheckIntent);

        } else if (id == R.id.nav_report) {
            Intent salaryReportIntent = new Intent(getBaseContext(), SalaryReportActivity.class);
            startActivity(salaryReportIntent);

        } else if (id == R.id.nav_faq) {
            Intent faqIntent = new Intent(getBaseContext(), FeedbackActivity.class);
            startActivity(faqIntent);

        } else if (id == R.id.nav_aboutus) {
            Intent aboutUsIntent = new Intent(getBaseContext(), AboutUsActivity.class);
            startActivity(aboutUsIntent);

        } else if (id == R.id.nav_exit) {
            //Exit app
            exitAppDialog();
        } else if (id == R.id.nav_setting){
            Intent settingIntent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(settingIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void exitAppDialog() {
        new MaterialDialog.Builder(this)
                .content(res.getString(R.string.baseAct_reminder4))
                .positiveText(res.getString(R.string.baseAct_reminder5))
                .negativeText(res.getString(R.string.baseAct_reminder6))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .show();

    }

}
