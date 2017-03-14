package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

public class SearchJobsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_jobs, null, false);
        mDrawer.addView(contentView, 0);
    }

    public void testing(View view){
        Intent intent = new Intent(getBaseContext(), JobListActivity.class);
        startActivity(intent);
    }
}
