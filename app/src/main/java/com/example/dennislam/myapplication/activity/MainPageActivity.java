package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

/**
 * Created by dennislam on 21/3/2017.
 */

public class MainPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

