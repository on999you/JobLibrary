package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.criteria.IndustryDao;
import com.example.dennislam.myapplication.dao.criteria.JobCatDao;
import com.example.dennislam.myapplication.dao.criteria.SalarySourceDao;
import com.example.dennislam.myapplication.dao.criteria.WorkExpDao;
import com.example.dennislam.myapplication.xml.IndustryXML;
import com.example.dennislam.myapplication.xml.JobCatXML;
import com.example.dennislam.myapplication.xml.SalarySourceXML;
import com.example.dennislam.myapplication.xml.WorkExpXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.dennislam.myapplication.R.id.jobDescription;
import static com.example.dennislam.myapplication.R.id.unspecifiedRadio;

public class SalaryCheckActivity extends BaseActivity {

    String currLanguage;
    Resources res ;

    private Toast toast;
    public static String sdtvName;

    ArrayList<String> workExpNameArray = new ArrayList<String>();
    ArrayList<String> workExpIdArray = new ArrayList<String>();

    ArrayList<String> jobCatArray = new ArrayList<String>();
    ArrayList<String> jobCatIdArray = new ArrayList<String>();

    ArrayList<String> industryArray = new ArrayList<String>();
    ArrayList<String> industryIdArray = new ArrayList<String>();

    ArrayList<String> salarySourceArray = new ArrayList<String>();
    ArrayList<String> salarySourceIdArray = new ArrayList<String>();

    TextView jobFunctionButton , jobIndustryButton;

    //Values that pass to database
    String finalWorkExpFromID, finalWorkExpToID, finalSalarySourceID;
    ArrayList<String> finalSelectedJobCatArray = new ArrayList<String>();
    ArrayList<String> finalSelectedJobIndustryArray = new ArrayList<String>();

    ArrayList<String> tempJobCatArray = new ArrayList<String>();
    ArrayList<String> tempJobIndustryArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_salary_check_search, null, false);
        mDrawer.addView(contentView, 0);

        res = getResources();

        //Detect Language
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        if(prefs.getString("Language","")=="zh"){
            currLanguage = "chi";
        } else{
            currLanguage = "eng";
        }

        final EditText jobTitleField = (EditText)findViewById(R.id.jobTitleField);
        Button searchButton = (Button)findViewById(R.id.searchButton);
        final CheckBox similarCheckBox = (CheckBox)findViewById(R.id.similarCheckBox);

        jobFunctionButton = (TextView)findViewById(R.id.jobFunctionButton);
        jobIndustryButton = (TextView)findViewById(R.id.jobIndustryButton);

        RadioGroup soruceRadioGroup = (RadioGroup)findViewById(R.id.soruceRadioGroup);
        RadioButton unspecifiedRadioBtn = (RadioButton) findViewById(R.id.unspecifiedRadio);
        RadioButton employerRadioBtn = (RadioButton) findViewById(R.id.employerRadio);
        RadioButton jobSeekerRadioBtn = (RadioButton) findViewById(R.id.jobSeekerRadio);

        soruceRadioGroup.check(unspecifiedRadio);
        unspecifiedRadioBtn.setBackgroundColor(Color.parseColor("#85A4A0"));
        unspecifiedRadioBtn.setTextColor(Color.parseColor("#F7F7F7"));
        employerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        employerRadioBtn.setTextColor(Color.parseColor("#3B616B"));
        jobSeekerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        jobSeekerRadioBtn.setTextColor(Color.parseColor("#3B616B"));

        soruceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == unspecifiedRadio) {
                    unspecifiedRadioBtn.setBackgroundColor(Color.parseColor("#85A4A0"));
                    unspecifiedRadioBtn.setTextColor(Color.parseColor("#F7F7F7"));
                    employerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    employerRadioBtn.setTextColor(Color.parseColor("#3B616B"));
                    jobSeekerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    jobSeekerRadioBtn.setTextColor(Color.parseColor("#3B616B"));

                } else if(checkedId == R.id.employerRadio) {
                    employerRadioBtn.setBackgroundColor(Color.parseColor("#85A4A0"));
                    employerRadioBtn.setTextColor(Color.parseColor("#F7F7F7"));
                    unspecifiedRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    unspecifiedRadioBtn.setTextColor(Color.parseColor("#3B616B"));
                    jobSeekerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    jobSeekerRadioBtn.setTextColor(Color.parseColor("#3B616B"));

                } else if(checkedId == R.id.jobSeekerRadio) {
                    jobSeekerRadioBtn.setBackgroundColor(Color.parseColor("#85A4A0"));
                    jobSeekerRadioBtn.setTextColor(Color.parseColor("#F7F7F7"));
                    employerRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    employerRadioBtn.setTextColor(Color.parseColor("#3B616B"));
                    unspecifiedRadioBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    unspecifiedRadioBtn.setTextColor(Color.parseColor("#3B616B"));

                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(jobTitleField.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), res.getString(R.string.sC_reminder1), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), SalaryCheckResultActivity.class);
                    intent.putExtra("jobTitle", jobTitleField.getText().toString().trim());
                    intent.putExtra("withSimilarWord", similarCheckBox.isChecked());
                    intent.putExtra("jobCat", finalSelectedJobCatArray);
                    intent.putExtra("jobIndustry", finalSelectedJobIndustryArray);
                    intent.putExtra("workExpFrom", finalWorkExpFromID);
                    intent.putExtra("workExpTo", finalWorkExpToID);
                    intent.putExtra("salarySource", finalSalarySourceID);

                    Log.v("criterias_bg", jobTitleField.getText().toString().trim() + "\n" + similarCheckBox.isChecked() + "\n" + finalSelectedJobCatArray + "\n" + finalSelectedJobIndustryArray + "\n" + finalWorkExpFromID + "\n" + finalWorkExpToID + "\n" + finalSalarySourceID);
                    startActivity(intent);
                }
            }
        });

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getCriteriasAsyncTaskRunner().execute();
        }
    }

    class getCriteriasAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<WorkExpXML.WorkExpItem> workExpItemList = new ArrayList<WorkExpXML.WorkExpItem>();
        List<JobCatXML.JobCatItem> jobCatItemList = new ArrayList<JobCatXML.JobCatItem>();
        List<IndustryXML.IndustryItem> industryItemList = new ArrayList<IndustryXML.IndustryItem>();
        List<SalarySourceXML.SalarySourceItem> salarySourceItemList = new ArrayList<SalarySourceXML.SalarySourceItem>();

        WorkExpDao workExpItemDao = new WorkExpDao();
        SalarySourceDao salarySourceItemDao = new SalarySourceDao();
        JobCatDao jobCatItemDao = new JobCatDao();
        IndustryDao industryItemDao = new IndustryDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(SalaryCheckActivity.this);
            loadingInternetDialog.setMessage("Loading...");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jobCatItemList = jobCatItemDao.getJobCatItemDao();
            workExpItemList = workExpItemDao.getWorkExpItemDao();
            salarySourceItemList = salarySourceItemDao.getSalarySourceItemDao();
            industryItemList = industryItemDao.getIndustryItemDao();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if(jobCatItemList == null || jobCatItemList.isEmpty()) {
                Toast.makeText(SalaryCheckActivity.this, "cannot get job cat", Toast.LENGTH_LONG).show();
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
                Toast.makeText(SalaryCheckActivity.this, "cannot get job industry", Toast.LENGTH_LONG).show();
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

            if(workExpItemList == null || workExpItemList.isEmpty()) {
                Toast.makeText(SalaryCheckActivity.this, "cannot get work exp", Toast.LENGTH_LONG).show();
            } else {
                //Get Work Exp
                for(int i = 0; i < workExpItemList.size(); i++){
                    if(currLanguage == "chi") {
                        workExpNameArray.add(i, workExpItemList.get(i).getWorkExpnameChi());
                    } else {
                        workExpNameArray.add(i, workExpItemList.get(i).getWorkExpname());
                    }
                    workExpIdArray.add(i, workExpItemList.get(i).getWorkExpid());
                }
            }

            if(salarySourceItemList == null || salarySourceItemList.isEmpty()) {
                Toast.makeText(SalaryCheckActivity.this, "cannot get salary source", Toast.LENGTH_LONG).show();
            } else {
                //Get Salary Source
                for (int i = 0; i < salarySourceItemList.size(); i++) {
                    salarySourceArray.add(i, salarySourceItemList.get(i).getDescription());
                    salarySourceIdArray.add(i, salarySourceItemList.get(i).getSoruce_id());
                }
            }

        }
    }

    public void WorkExpFromDialog(View v) {
        new MaterialDialog.Builder(this)
                .title("Work Experience")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(workExpNameArray.toArray(new CharSequence[workExpNameArray.size()]))
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    TextView WorkExpFrom = (TextView)findViewById(R.id.WorkExpFrom);
                    WorkExpFrom.setText(res.getString(R.string.sC_reminder6) + "" + text );
                    finalWorkExpFromID = workExpIdArray.get(which);
                    return true; // allow selection
                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText("Done")
                .show();
    }

    public void WorkExpToDialog(View v) {
        new MaterialDialog.Builder(this)
                .title("Work Experience")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(workExpNameArray.toArray(new CharSequence[workExpNameArray.size()]))
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    TextView WorkExpTo = (TextView)findViewById(R.id.WorkExpTo);
                    WorkExpTo.setText(res.getString(R.string.sC_reminder5) + "" + text);
                    finalWorkExpToID = workExpIdArray.get(which);
                    return true; // allow selection
                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText("Done")
                .show();
    }

    public void salarySourceDialog(View v) {
        new MaterialDialog.Builder(this)
                .title("Salary Source(s) Within")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(salarySourceArray.toArray(new CharSequence[salarySourceArray.size()]))
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    TextView salarySource = (TextView)findViewById(R.id.salarySource);
                    salarySource.setText(res.getString(R.string.sC_reminder4) + "" + text);
                    finalSalarySourceID = salarySourceIdArray.get(which);
                    return true; // allow selection
                })
                .positiveColor(Color.parseColor("#486E76"))
                .positiveText("Done")
                .show();
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
                        for(int i =0; i<tempJobIndustryArray.size(); i++){
                            finalSelectedJobIndustryArray.add(industryIdArray.get(Integer.parseInt(tempJobIndustryArray.get(i))));
                        }
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .show();
    }
}