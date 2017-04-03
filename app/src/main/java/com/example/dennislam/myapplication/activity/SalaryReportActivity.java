package com.example.dennislam.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.example.dennislam.myapplication.R;

public class SalaryReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_salary_report, null, false);
        mDrawer.addView(contentView, 0);

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            WebView browser = (WebView) findViewById(R.id.webview);
            browser.loadUrl("http://192.168.232.66:8009/SalaryReport.html");
        }

    }
}
