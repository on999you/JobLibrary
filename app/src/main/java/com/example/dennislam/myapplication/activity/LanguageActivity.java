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

public class LanguageActivity extends BaseActivity{
    private String lang;
    private TextView chi,eng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_lang, null, false);
        mDrawer.addView(contentView, 0);

        chi = (TextView)findViewById(R.id.chineseLangField);
        eng = (TextView)findViewById(R.id.englishLangField);

        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        Drawable checked= ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null);
        checked.setBounds(0, 0, checked.getMinimumWidth(), checked.getMinimumHeight());
        if(prefs.getString("Language","")=="zh"){
            chi.setCompoundDrawables(null,null,checked,null);
        } else{
            eng.setCompoundDrawables(null,null,checked,null);
        }
    }

    public void changeEngLang(View v){
        lang = "en";
        setLocale(lang);
    }

    public void changeChinLang(View v){
        lang = "zh";
        setLocale(lang);
    }

    public void setLocale(String langu) {
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Drawable checked= ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null);
        checked.setBounds(0, 0, checked.getMinimumWidth(), checked.getMinimumHeight());
        if (!langu.equals(prefs.getString("Language",""))) {
            Log.v("change","~");
            editor.putString("Language", langu);
            editor.commit();
            Locale myLocale = new Locale(langu);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Locale.setDefault(myLocale);
            Intent refresh = new Intent(this, MainPageActivity.class);
            startActivity(refresh);
            finish();
            if(langu=="zh"){
                chi.setCompoundDrawables(null,null,checked,null);
                eng.setCompoundDrawables(null,null,null,null);
            }else{
                eng.setCompoundDrawables(null,null,checked,null);
                chi.setCompoundDrawables(null,null,null,null);
            }
        }
    }

}
