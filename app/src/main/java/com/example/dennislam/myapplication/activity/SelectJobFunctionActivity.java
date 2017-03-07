package com.example.dennislam.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dennislam.myapplication.R;

import java.util.ArrayList;

public class SelectJobFunctionActivity extends BaseActivity {

    ArrayList<String> array = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_select_job_function, null, false);
        mDrawer.addView(contentView, 0);

        Intent intent=getIntent();
        array = intent.getStringArrayListExtra("array");
        Log.v("abc", "" + array.get(1));



    }




}


