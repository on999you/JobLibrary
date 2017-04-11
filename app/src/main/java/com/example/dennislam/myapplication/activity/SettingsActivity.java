package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

/**
 * Created by shaunlau on 14/3/2017.
 */

public class SettingsActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_setting, null, false);
        mDrawer.addView(contentView, 0);
    }
    public void passToLang(View v){
        Intent intent = new Intent(getBaseContext(),LanguageActivity.class);
        startActivity(intent);
    }
    public void passToFont(View v){
        Intent intent = new Intent(getBaseContext(),FontSizeSettingActivity.class);
        startActivity(intent);
    }
}
