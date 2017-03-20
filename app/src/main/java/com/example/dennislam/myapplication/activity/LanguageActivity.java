package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

import java.util.Locale;

/**
 * Created by shaunlau on 14/3/2017.
 */

public class LanguageActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_lang, null, false);
        mDrawer.addView(contentView, 0);
    }
    public void changeEngLang(View v){
        setLocale("en");
    }
    public void changeChinLang(View v){
        setLocale("zh");
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainPageActivity.class);
        startActivity(refresh);
        finish();
    }

}
