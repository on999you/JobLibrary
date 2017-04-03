package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dennislam.myapplication.R;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_about_us, null, false);
        mDrawer.addView(contentView, 0);
    }

    //Action after clicking the "Email Us" button
    public void emailUs (View v) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"lcyyyin24@gmail.com"});
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
