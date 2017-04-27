package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dennislam.myapplication.MyFirebaseMessagingService;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.criteria.IndustryDao;
import com.example.dennislam.myapplication.dao.criteria.JobCatDao;
import com.example.dennislam.myapplication.xml.GetJobCatXML;
import com.example.dennislam.myapplication.xml.GetJobIndustryXML;
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
    ArrayList<String> jobCatIdArray = new ArrayList<String>();


    private Switch switch_noti;
    private Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification, null, false);
        mDrawer.addView(contentView, 0);

        //Detect Language
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        if (prefs.getString("Language", "") == "zh") {
            currLanguage = "chi";
        } else {
            currLanguage = "eng";
        }

        new getCriteriasAsyncTaskRunner().execute();



            }

    class getCriteriasAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetJobCatXML.JobCatItem> jobCatItemList = new ArrayList<GetJobCatXML.JobCatItem>();

        JobCatDao jobCatItemDao = new JobCatDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(NotificationActivity.this);
            loadingInternetDialog.setMessage("Loading");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jobCatItemList = jobCatItemDao.getJobCatItemDao();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if(jobCatItemList == null || jobCatItemList.isEmpty()) {
                Toast.makeText(NotificationActivity.this, "cannot get job cat", Toast.LENGTH_LONG).show();
            } else {
                //Get Job Cat
                for(int i = 0; i < jobCatItemList.size(); i++){
                    if(currLanguage == "chi") {
                        jobCatArray.add(i, jobCatItemList.get(i).getJobCatNameChi());
                    } else {
                        jobCatArray.add(i, jobCatItemList.get(i).getJobCatName());
                    }
                    jobCatIdArray.add(i, jobCatItemList.get(i).getJobCatID());

                }
            }

            SharedPreferences sharedPreferences_noticate = getSharedPreferences("Notification",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences_noticate.edit();

            switch_noti = (Switch)findViewById(R.id.notification_switch);
            mySpinner = (Spinner)findViewById(R.id.mySpinner);

            ArrayAdapter<String> topicArray = new ArrayAdapter<>(NotificationActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    jobCatArray);

            mySpinner.setAdapter(topicArray);
 Log.v("Boolean  "+sharedPreferences_noticate.getBoolean("isChecked_Main",true),"~");
        if(sharedPreferences_noticate.getBoolean("isChecked_Main",true)){
           switch_noti.setChecked(true);
            mySpinner.setVisibility(View.VISIBLE);
        }

            SharedPreferences firebaseTopic = getSharedPreferences("firebaseTopic",MODE_PRIVATE);
            SharedPreferences.Editor firebaseEditor = firebaseTopic.edit();


            ArrayAdapter myAdap = (ArrayAdapter) mySpinner.getAdapter(); //cast to an ArrayAdapter

            int subedTopic = myAdap.getPosition(firebaseTopic.getString("Noti_Topic",""));


            mySpinner.setSelection(subedTopic);

            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newTopic =jobCatArray.get(position);//.replaceAll(" ","");
                    String oldTopic  = firebaseTopic.getString("Noti_Topic","").replaceAll(" ","");
                    oldTopic = oldTopic.replaceAll("/","");

                    if (!"Empty".equals(firebaseTopic.getString("Noti_Topic",""))) {
                        String unWantTopic = oldTopic.replace(" ","");
                        unWantTopic=unWantTopic.replaceAll("/","");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(unWantTopic);
                        Log.v("unsub   "+unWantTopic,"~");
                    }

                    Log.v("oldTopic "+oldTopic,"~");
                    Log.v("Topic name "+newTopic,"~");


                    firebaseEditor.putString("Noti_Topic",newTopic);
                    Log.v("subscribed "+jobCatArray.get(position),"~");
                    firebaseEditor.commit();

                    if (!"Empty".equals(firebaseTopic.getString("Noti_Topic",""))) {
                        String subedTopic = firebaseTopic.getString("Noti_Topic","").replace(" ","");
                        subedTopic=subedTopic.replaceAll("/","");
                        Log.v("will sub   "+subedTopic,"~");
                        FirebaseMessaging.getInstance().subscribeToTopic(subedTopic);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            switch_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.v("Checked the switch","~");
                        mySpinner.setVisibility(View.VISIBLE);
                        mySpinner.setEnabled(true);
                        editor.putBoolean("isChecked_Main",true).commit();




                        Log.v("subscribed "+firebaseTopic.getString("Noti_Topic",""),"~");


                    } else {
                        Log.v("uncheck switch","~");
                        mySpinner.setEnabled(false);
                        mySpinner.setVisibility(View.INVISIBLE);
                        editor.putBoolean("isChecked_Main",false);
                        editor.commit();
                        if (!"Empty".equals(firebaseTopic.getString("Noti_Topic",""))) {
                            String unsubTopic = firebaseTopic.getString("Noti_Topic","").replace(" ","");
                            unsubTopic=unsubTopic.replaceAll("/","");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(unsubTopic);
                        }
                        firebaseEditor.putString("Noti_Topic","Empty");
                        firebaseEditor.commit();
                    }
                }
            });
        }
    }
        }
