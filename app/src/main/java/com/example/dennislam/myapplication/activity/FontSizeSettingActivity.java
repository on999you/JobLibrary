package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.dennislam.myapplication.R;

import java.util.Locale;

/**
 * Created by shaunlau on 14/3/2017.
 */

public class FontSizeSettingActivity extends BaseActivity{
    private TextView font_L,font_M,font_S;
    Drawable chinese, english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_fonsize_setting, null, false);
        mDrawer.addView(contentView, 0);

        font_L = (TextView)findViewById(R.id.fontSize_large);
        font_M = (TextView)findViewById(R.id.fontSize_medium);
        font_S = (TextView)findViewById(R.id.fontSize_small);

        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        Drawable checked= ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null);
        checked.setBounds(0, 0, checked.getMinimumWidth(), checked.getMinimumHeight());


        if("Large".equals(prefs.getString("FONT_SIZE",""))){
            font_L.setCompoundDrawables(null,null,checked,null);
        }else  if("Medium".equals(prefs.getString("FONT_SIZE",""))){
            font_M.setCompoundDrawables(null,null,checked,null);
        }else{
            font_S.setCompoundDrawables(null,null,checked,null);
        }
    }

    public void changeFontLarge(View v){
        setFontSize("Large");
    }

    public void changeFontMed(View v){
        setFontSize("Medium");
    }
    public void changeFontSmall(View v){
        setFontSize("Small");
    }
    public void setFontSize(String fontSize) {
        SharedPreferences prefs = getSharedPreferences("fontSizeSetting",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Drawable checked= ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null);
        checked.setBounds(0, 0, checked.getMinimumWidth(), checked.getMinimumHeight());

        if (!fontSize.equals(prefs.getString("FONT_SIZE",""))) {
            Log.v("change","~");
            editor.putString("FONT_SIZE", fontSize);
            editor.commit();

            Intent refresh = new Intent(this, MainPageActivity.class);
            startActivity(refresh);
            finish();

            if("Large".equals(prefs.getString("FONT_SIZE",""))){
                font_L.setCompoundDrawables(null,null,checked,null);
                font_M.setCompoundDrawables(null,null,null,null);
                font_S.setCompoundDrawables(null,null,null,null);
            }else  if("Medium".equals(prefs.getString("FONT_SIZE",""))){
                font_L.setCompoundDrawables(null,null,null,null);
                font_M.setCompoundDrawables(null,null,checked,null);
                font_S.setCompoundDrawables(null,null,null,null);
            }else{
                font_L.setCompoundDrawables(null,null,null,null);
                font_M.setCompoundDrawables(null,null,null,null);
                font_S.setCompoundDrawables(null,null,checked,null);
            }
        }
    }

}
