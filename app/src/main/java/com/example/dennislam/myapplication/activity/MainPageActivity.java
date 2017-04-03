package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

import java.util.Locale;

/**
 * Created by dennislam on 21/3/2017.
 */

public class MainPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main_page, null, false);
        mDrawer.addView(contentView, 0);
        Log.v("Testing steps", "Main Page : Get Udid from global class = " + globalVariable.getUdid());

    }

    public void passToJobList(View v){
        Intent intent = new Intent(getBaseContext(), SearchJobsActivity.class);
        startActivity(intent);
    }

    public void passToSalary(View v){
        Intent intent = new Intent(getBaseContext(), SalaryCheckActivity.class);
        startActivity(intent);
    }

    public void passToAppliedJob(View v){
        Intent intent = new Intent(getBaseContext(), AppliedJobActivity.class);
        startActivity(intent);
    }

    public void passToCV(View v){
        Intent intent = new Intent(getBaseContext(), CvActivity.class);
        startActivity(intent);
    }

    public void passToFAQ(View v){
        Intent intent = new Intent(getBaseContext(), FeedbackActivity.class);
        startActivity(intent);
    }

    public void exitApp(View v){
        exitAppDialog();
    }

    public void onBackPressed() {
        exitAppDialog();
    }

}

