package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.criteria.IndustryDao;
import com.example.dennislam.myapplication.dao.criteria.JobCatDao;
import com.example.dennislam.myapplication.xml.GetJobIndustryXML;
import com.example.dennislam.myapplication.xml.GetJobCatXML;

import java.util.ArrayList;
import java.util.List;

public class SearchJobsActivity extends BaseActivity {

    String currLanguage;

    private Toast toast;
    EditText jobTitleField,salaryMin, salaryMax;
    CheckBox similarCheckBox;

    ArrayList<String> jobCatArray = new ArrayList<String>();
    ArrayList<String> jobCatIdArray = new ArrayList<String>();

    ArrayList<String> industryArray = new ArrayList<String>();
    ArrayList<String> industryIdArray = new ArrayList<String>();

    TextView jobFunctionButton , jobIndustryButton;

    ArrayList<String> finalSelectedJobCatArray = new ArrayList<String>();
    ArrayList<String> finalSelectedJobIndustryArray = new ArrayList<String>();

    ArrayList<String> tempJobCatArray = new ArrayList<String>();
    ArrayList<String> tempJobIndustryArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_jobs, null, false);
        mDrawer.addView(contentView, 0);

        //Detect Language
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        if(prefs.getString("Language","")=="zh"){
            currLanguage = "chi";
        } else{
            currLanguage = "eng";
        }

        jobTitleField = (EditText)findViewById(R.id.jobTitleField);
        similarCheckBox = (CheckBox)findViewById(R.id.similarCheckBox);
        jobFunctionButton = (TextView)findViewById(R.id.jobFunctionButton);
        jobIndustryButton = (TextView)findViewById(R.id.jobIndustryButton);
        salaryMin = (EditText)findViewById(R.id.salaryMin);
        salaryMax = (EditText)findViewById(R.id.salaryMax);

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getCriteriasAsyncTaskRunner().execute();
        }
    }

    public void searchNow(View view){

        if( ((!salaryMin.getText().toString().equals("") && salaryMax.getText().toString().equals("") )    ||    (salaryMin.getText().toString().equals("") && !salaryMax.getText().toString().equals("")) ) ||
                ( (!salaryMin.getText().toString().equals("") && !salaryMax.getText().toString().equals("") ) && (Integer.parseInt(salaryMin.getText().toString()) > Integer.parseInt(salaryMax.getText().toString())) )){
            Toast.makeText(getBaseContext(), "Wrong salary range", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getBaseContext(), JobListActivity.class);
            intent.putExtra("jobTitle", jobTitleField.getText().toString().trim());
            intent.putExtra("withSimilarWord", similarCheckBox.isChecked());
            intent.putExtra("jobCat", finalSelectedJobCatArray);
            intent.putExtra("jobIndustry", finalSelectedJobIndustryArray);
            intent.putExtra("salaryMin", salaryMin.getText().toString());
            intent.putExtra("salaryMax", salaryMax.getText().toString());
            startActivity(intent);
        }
    }

    class getCriteriasAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetJobCatXML.JobCatItem> jobCatItemList = new ArrayList<GetJobCatXML.JobCatItem>();
        List<GetJobIndustryXML.IndustryItem> industryItemList = new ArrayList<GetJobIndustryXML.IndustryItem>();

        JobCatDao jobCatItemDao = new JobCatDao();
        IndustryDao industryItemDao = new IndustryDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(SearchJobsActivity.this);
            loadingInternetDialog.setMessage("Loading");
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

            if(jobCatItemList == null || jobCatItemList.isEmpty()) {
                Toast.makeText(SearchJobsActivity.this, "cannot get job cat", Toast.LENGTH_LONG).show();
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

            if(industryItemList == null || industryItemList.isEmpty()) {
                Toast.makeText(SearchJobsActivity.this, "cannot get job industry", Toast.LENGTH_LONG).show();
            } else {
                //Get Job Industry
                for(int i = 0; i < industryItemList.size(); i++){
                    if(currLanguage == "chi") {
                        industryArray.add(i, industryItemList.get(i).getIndustryNameChi());
                    } else {
                        industryArray.add(i, industryItemList.get(i).getIndustryName());
                    }
                    industryIdArray.add(i, industryItemList.get(i).getIndustryID());
                }
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
                        showToast(res.getString(R.string.sC_reminder2) + "");
                    }
                    if(which.length == 6){
                        jobFunctionButton.setText(5 + res.getString(R.string.sC_reminder3));
                    } else {
                        jobFunctionButton.setText(which.length + "" + res.getString(R.string.sC_reminder3));

                        tempJobCatArray.clear();
                        for(int i=0; i< which.length; i++){
                            tempJobCatArray.add(which[i].toString());
                        }
                    }
                    return allowSelectionChange;

                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText(res.getString(R.string.faq_btnD))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finalSelectedJobCatArray.clear();
                        for(int i =0; i<tempJobCatArray.size(); i++){
                            finalSelectedJobCatArray.add(jobCatIdArray.get(Integer.parseInt(tempJobCatArray.get(i))));
                        }
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
                        showToast(res.getString(R.string.sC_reminder2) + "");
                    }
                    if(which.length == 6){
                        jobIndustryButton.setText(5 + res.getString(R.string.sC_reminder3));
                    } else {
                        jobIndustryButton.setText(which.length + "" + res.getString(R.string.sC_reminder3));

                        tempJobIndustryArray.clear();
                        for(int i=0; i< which.length; i++){
                            tempJobIndustryArray.add(which[i].toString());
                        }
                    }
                    return allowSelectionChange;
                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText(res.getString(R.string.faq_btnD))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finalSelectedJobIndustryArray.clear();
                        for(int i =0; i<tempJobIndustryArray.size(); i++){
                            finalSelectedJobIndustryArray.add(industryIdArray.get(Integer.parseInt(tempJobIndustryArray.get(i))));
                        }
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .show();
    }

}
