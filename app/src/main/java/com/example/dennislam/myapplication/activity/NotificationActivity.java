package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dennislam.myapplication.MyFirebaseMessagingService;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.xml.GetJobCatXML;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shaun on 23/4/2017.
 */

public class NotificationActivity extends BaseActivity  {

    String currLanguage;

    ArrayList<String> jobCatArray = new ArrayList<String>();

   static Set<String> topic = new HashSet<String>();

    private Switch switch_noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Detect Language
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        if (prefs.getString("Language", "") == "zh") {
            currLanguage = "chi";
        } else {
            currLanguage = "eng";
        }

        List<GetJobCatXML.JobCatItem> jobCatItemList = new ArrayList<GetJobCatXML.JobCatItem>();

        if (jobCatItemList == null || jobCatItemList.isEmpty()) {
            Toast.makeText(NotificationActivity.this, "cannot get job cat", Toast.LENGTH_LONG).show();
        } else {
            //Get Job Cat
            for (int i = 0; i < jobCatItemList.size(); i++) {
                if (currLanguage == "chi") {
                    jobCatArray.add(i, jobCatItemList.get(i).getJobCatNameChi());
                } else {
                    jobCatArray.add(i, jobCatItemList.get(i).getJobCatName());
                }
            }
        }
                switch_noti = (Switch)findViewById(R.id.notification_switch);

                LinearLayout context = (LinearLayout) findViewById(R.id.contextNotification);
                SharedPreferences sharedPreferences_noticate = getSharedPreferences("Notification",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_noticate.edit();

        if(sharedPreferences_noticate.getBoolean("isChecked_Main",true)){
            //switch_noti.setChecked(true);
        }

                switch_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            for (int j = 0; j < jobCatArray.size(); j++) {

                                editor.putBoolean("isChecked_Main",true).commit();

                                isChecked = false;
                                String switchTitle = jobCatArray.get(j);
                                Switch mySwitch = new Switch(NotificationActivity.this);
                                mySwitch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 55));
                                mySwitch.setText(switchTitle);

                                SharedPreferences firebaseTopic = getSharedPreferences("firebaseTopic",MODE_PRIVATE);
                                SharedPreferences.Editor firebaseEditor = firebaseTopic.edit();


                                if(sharedPreferences_noticate.getBoolean("isChecked_"+switchTitle,true)){
                                    mySwitch.setChecked(true);
                                }


                                mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            editor.putBoolean("isChecked_"+switchTitle,true).commit();
                                            topic.add(switchTitle);
                                            FirebaseMessaging.getInstance().subscribeToTopic(switchTitle);
                                        } else {
                                            editor.putBoolean("isChecked_"+switchTitle,false).commit();
                                            topic.remove(switchTitle);
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic(switchTitle);
                                        }
                                    }
                                });

                               firebaseEditor.putStringSet("topic",topic).commit();
                            }
                        } else {
                             editor.putBoolean("isChecked_Main",false).commit();
                            context.removeAllViews();
                        }
                    }
                });
            }
        }
