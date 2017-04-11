package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.OpenAppDao;
import com.example.dennislam.myapplication.xml.OpenAppXML;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenAppActivity extends BaseActivity {

    private SharedPreferences settings;
    private static final String data = "DATA";
    String udid, appVersion = "1.5.0", mobAppId = "3", osType = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_app);

        //Get Currently udid
        settings = getSharedPreferences(data,0);
        udid = settings.getString("existingUdid", "");

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new openAppAsyncTaskRunner().execute();
        }
    }

    //Detect which language
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString("Language", "");
        setLocale(language);

         Log.v(prefs.getString("Language",""),"~nionoiopnionionoinionojk");
    }

    //Set language that chose last time
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    //Async Task to handle open app checking
    class openAppAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<OpenAppXML.OpenAppItem> openAppItemList = new ArrayList<OpenAppXML.OpenAppItem>();
        OpenAppDao openAppItemDao = new OpenAppDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            //Loading Dialog
            loadingInternetDialog = new ProgressDialog(OpenAppActivity.this);
            loadingInternetDialog.setMessage("Loading");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Check the device contains udid already or not
            //Check app version is the latest version or not
            openAppItemList = openAppItemDao.getOpenAppItemDao(udid, appVersion, mobAppId, osType);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //Stop loading dialog
            loadingInternetDialog.dismiss();

            //Internet is not working
            if(openAppItemList == null || openAppItemList.isEmpty()){
                new MaterialDialog.Builder(OpenAppActivity.this)
                        .content(res.getString(R.string.Cv_reminder4))
                        .positiveText(res.getString(R.string.baseAct_reminder3))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
            //Internet is working
            else {
                //Detect Language
                loadLocale();

                //Get status code ; 0 is correct ; 1 is error
                int status_code = openAppItemDao.getStatusCode();

                //When udid is empty ; which mean the device open the app first time
                if(udid == "" && status_code == 0) {
                    settings = getSharedPreferences(data,0);
                    settings.edit()
                            .putString("existingUdid", openAppItemList.get(0).getUdid())
                            .apply();
                    udid = settings.getString("existingUdid", "");


                    //Show welcome notification when there are the new user
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(OpenAppActivity.this);
                    Intent notIntent = new Intent(OpenAppActivity.this, AboutUsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(OpenAppActivity.this, 0, notIntent, 0);

                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setSmallIcon(R.drawable.appicon);
                    mBuilder.setContentTitle("Welcome to Job Library");
                    mBuilder.setContentText("Tap for know more about us");
                    mBuilder.setAutoCancel(true);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());

                }

                //Set the updated udid
                globalVariable.setUdid(udid);

                Log.v("Testing steps", "Open App : Udid = " + udid);

                //Pass To Main Page
                Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
                startActivity(intent);


            }

        }
    }
}
