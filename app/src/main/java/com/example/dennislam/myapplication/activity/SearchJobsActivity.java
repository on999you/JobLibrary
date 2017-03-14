package com.example.dennislam.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.criteria.IndustryDao;
import com.example.dennislam.myapplication.dao.criteria.JobCatDao;
import com.example.dennislam.myapplication.xml.IndustryXML;
import com.example.dennislam.myapplication.xml.JobCatXML;

import java.util.ArrayList;
import java.util.List;

public class SearchJobsActivity extends BaseActivity {

    private Toast toast;

    JobCatDao jobCatItemDao = new JobCatDao();
    IndustryDao industryItemDao = new IndustryDao();

    List<JobCatXML.JobCatItem> jobCatItemList = new ArrayList<JobCatXML.JobCatItem>();
    ArrayList<String> jobCatArray = new ArrayList<String>();
    ArrayList<String> jobCatIdArray = new ArrayList<String>();

    List<IndustryXML.IndustryItem> industryItemList = new ArrayList<IndustryXML.IndustryItem>();
    ArrayList<String> industryArray = new ArrayList<String>();
    ArrayList<String> industryIdArray = new ArrayList<String>();

    TextView jobFunctionButton , jobIndustryButton;

    ArrayList<String> tempJobCatArray = new ArrayList<String>();

    ArrayList<String> selectedJobCatArray = new ArrayList<String>();
    ArrayList<String> selectedJobIndustryArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_jobs, null, false);
        mDrawer.addView(contentView, 0);

        jobFunctionButton = (TextView)findViewById(R.id.jobFunctionButton);
        jobIndustryButton = (TextView)findViewById(R.id.jobIndustryButton);

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getCriteriasAsyncTaskRunner().execute();
        }
    }

    class getCriteriasAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(SearchJobsActivity.this);
            loadingInternetDialog.setMessage("Loading...");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jobCatItemList = jobCatItemDao.getJobCatItemDao();
            industryItemList = industryItemDao.getIndustryItemDao();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            //Get Job Cat
            for(int i = 0; i < jobCatItemList.size(); i++){
                jobCatArray.add(i, jobCatItemList.get(i).getJobCatName());
                jobCatIdArray.add(i, jobCatItemList.get(i).getJobCatID());
            }

            //Get Job Industry
            for(int i = 0; i < industryItemList.size(); i++){
                industryArray.add(i, industryItemList.get(i).getIndustryName());
                industryIdArray.add(i, industryItemList.get(i).getIndustryID());
            }

        }
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showJobFunction(View v){
        new MaterialDialog.Builder(this)
                .title("Job Function")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(jobCatArray.toArray(new CharSequence[jobCatArray.size()]))
                .itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {

                    boolean allowSelectionChange = which.length <= 5;
                    if (!allowSelectionChange) {
                        showToast("Select up to 5 choices only.");
                    }
                    if(which.length == 6){
                        jobFunctionButton.setText(5 + " items selected");
                    } else {
                        jobFunctionButton.setText(which.length + " items selected");

                        tempJobCatArray.clear();
                        for(int i=0; i< which.length; i++){
                            tempJobCatArray.add(which[i].toString());
                        }
                    }
                    return allowSelectionChange;

                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText("Done")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.v("criterias_bg", tempJobCatArray.toString());
                        for(int i =0; i<tempJobCatArray.size(); i++){
                            selectedJobCatArray.add(jobCatIdArray.get(Integer.parseInt(tempJobCatArray.get(i))));
                        }
                        Log.v("criterias_bg", selectedJobCatArray.toString());
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .show();
    }

    public void showJobIndustry (View v){
        new MaterialDialog.Builder(this)
                .title("Job Industry")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(industryArray.toArray(new CharSequence[industryArray.size()]))
                .itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {
                    boolean allowSelectionChange = which.length <= 5;
                    if (!allowSelectionChange) {
                        showToast("Select up to 5 choices only.");
                    }
                    if(which.length == 6){
                        jobIndustryButton.setText(5 + " items selected");
                    } else {
                        jobIndustryButton.setText(which.length + " items selected");
                    }
                    return allowSelectionChange;
                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText("Done")
                .alwaysCallMultiChoiceCallback()
                .show();
    }

    public void testing(View view){
        Intent intent = new Intent(getBaseContext(), JobListActivity.class);
        startActivity(intent);
    }
}
