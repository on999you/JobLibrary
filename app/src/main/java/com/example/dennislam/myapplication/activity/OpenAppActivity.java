package com.example.dennislam.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.GlobalClass;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.OpenAppDao;
import com.example.dennislam.myapplication.xml.OpenAppXML;

import java.util.ArrayList;
import java.util.List;

public class OpenAppActivity extends BaseActivity {

    private SharedPreferences settings;
    private static final String data = "DATA";

    String udid;
    String appVersion = "1.5.0";
    String mobAppId = "3";
    String osType = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_app);

        settings = getSharedPreferences(data,0);
        udid = settings.getString("existingUdid", "");

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new openAppAsyncTaskRunner().execute();
        }
    }

    class openAppAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<OpenAppXML.OpenAppItem> openAppItemList = new ArrayList<OpenAppXML.OpenAppItem>();
        OpenAppDao openAppItemDao = new OpenAppDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(OpenAppActivity.this);
            loadingInternetDialog.setMessage("Loading...");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            openAppItemList = openAppItemDao.getOpenAppItemDao(udid, appVersion, mobAppId, osType);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if(openAppItemList == null || openAppItemList.isEmpty()){
                new MaterialDialog.Builder(OpenAppActivity.this)
                        .content("Internet are not working")
                        .positiveText("ok")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
            else {
                int status_code = openAppItemDao.getStatusCode();

                if(udid == "" && status_code == 0) {
                    settings = getSharedPreferences(data,0);
                    settings.edit()
                            .putString("existingUdid", openAppItemList.get(0).getUdid())
                            .apply();
                    udid = settings.getString("existingUdid", "");
                }

                //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.setUdid(udid);

                Log.v("Testing steps", "Open App : Udid = " + udid);

                Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
                startActivity(intent);
            }

        }
    }
}
