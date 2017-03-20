package com.example.dennislam.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.ApplyJobDao;
import com.example.dennislam.myapplication.dao.JobDetailDao;
import com.example.dennislam.myapplication.dao.JobListDao;
import com.example.dennislam.myapplication.xml.ApplyJobXML;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;
import com.example.dennislam.myapplication.xml.JobDetailXML;
import com.example.dennislam.myapplication.xml.JobListXML;

import java.util.ArrayList;
import java.util.List;

import static com.example.dennislam.myapplication.R.id.jobDescription;

public class JobDetailActivity extends BaseActivity {

    private SharedPreferences settings;
    private static final String data = "DATA";
    String udid, jobId;

    ArrayList<String> jobTitleArray = new ArrayList<String>();
    ArrayList<String> industryArray = new ArrayList<String>();
    ArrayList<String> jobAreaArray = new ArrayList<String>();
    ArrayList<String> companyArray = new ArrayList<String>();
    ArrayList<String> emailArray = new ArrayList<String>();
    ArrayList<String> jobDescArray = new ArrayList<String>();
    ArrayList<String> contactArray = new ArrayList<String>();
    ArrayList<String> companyDescArray = new ArrayList<String>();
    ArrayList<String> createDateArray = new ArrayList<String>();
    ArrayList<String> salaryArray = new ArrayList<String>();

    TextView jobTitleView, companyView, createDateView, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_job_detail, null, false);
        mDrawer.addView(contentView, 0);

        jobId = getIntent().getStringExtra("jobId");

        settings = getSharedPreferences(data,0);
        udid = settings.getString("existingUdid", "");

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getJobDetailAsyncTaskRunner().execute();
        }

        jobTitleView = (TextView)findViewById(R.id.jobTitleView);
        companyView = (TextView)findViewById(R.id.companyView);
        createDateView = (TextView)findViewById(R.id.createDateView);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView6 = (TextView)findViewById(R.id.textView6);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView8 = (TextView)findViewById(R.id.textView8);

        RadioGroup jobDetailRadioGroup = (RadioGroup)findViewById(R.id.jobDetailRadioGroup);
        RadioButton jobDescRadio = (RadioButton) findViewById(R.id.jobDescription);
        RadioButton companyInfoRadio = (RadioButton) findViewById(R.id.companyInfo);
        jobDetailRadioGroup.check(jobDescription);
        jobDescRadio.setBackgroundColor(Color.parseColor("#85A4A0"));
        companyInfoRadio.setBackgroundColor(Color.parseColor("#FFFFFF"));
        jobDescRadio.setTextColor(Color.parseColor("#F7F7F7"));
        companyInfoRadio.setTextColor(Color.parseColor("#3B616B"));

        jobDetailRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == jobDescription) {
                    jobDescRadio.setBackgroundColor(Color.parseColor("#85A4A0"));
                    companyInfoRadio.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    jobDescRadio.setTextColor(Color.parseColor("#F7F7F7"));
                    companyInfoRadio.setTextColor(Color.parseColor("#3B616B"));
                    //Toast.makeText(getApplicationContext(), "choice: job", Toast.LENGTH_SHORT).show();

                    textView1.setText("Job Function");
                    textView3.setText("Job Industry");
                    textView5.setText("Job Description");
                    textView7.setText("Salary");

                    textView2.setText(jobAreaArray.get(0) + salaryArray.get(0));
                    textView4.setText(industryArray.get(0));
                    textView6.setText(jobDescArray.get(0));
                    textView8.setText(salaryArray.get(0));

                } else if(checkedId == R.id.companyInfo) {
                    companyInfoRadio.setBackgroundColor(Color.parseColor("#85A4A0"));
                    jobDescRadio.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    companyInfoRadio.setTextColor(Color.parseColor("#F7F7F7"));
                    jobDescRadio.setTextColor(Color.parseColor("#3B616B"));
                    //Toast.makeText(getApplicationContext(), "choice: company", Toast.LENGTH_SHORT).show();

                    textView1.setText("Email Address");
                    textView3.setText("Contact");
                    textView5.setText("Company Description");
                    textView7.setText("");

                    textView2.setText(emailArray.get(0));
                    textView4.setText(contactArray.get(0));
                    textView6.setText(companyDescArray.get(0));
                    textView8.setText("");
                }
            }
        });


        Button applyJobButton = (Button)findViewById(R.id.applyJobButton);


        applyJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prevent double click in a second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    new MaterialDialog.Builder(JobDetailActivity.this)
                            .content("Cannot apply the same job within 3 seconds")
                            .positiveText("ok")
                            .show();
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                new applyJobAsyncTaskRunner().execute();
            }
        });
    }

    class getJobDetailAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<JobDetailXML.JobDetailItem> jobDetailItemList = new ArrayList<JobDetailXML.JobDetailItem>();
        JobDetailDao jobDetailItemDao = new JobDetailDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(JobDetailActivity.this);
            loadingInternetDialog.setMessage("Loading...");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jobDetailItemList = jobDetailItemDao.jobDetailItemDao(jobId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if(jobDetailItemList == null || jobDetailItemList.isEmpty()) {
                Toast.makeText(JobDetailActivity.this, "error", Toast.LENGTH_LONG).show();
            }

            else {
                for(int i = 0; i < jobDetailItemList.size(); i++){

                    jobTitleArray.add(i, jobDetailItemList.get(i).getJobTitle());
                    industryArray.add(i, jobDetailItemList.get(i).getIndustry());
                    jobAreaArray.add(i, jobDetailItemList.get(i).getJobArea());
                    companyArray.add(i, jobDetailItemList.get(i).getCompany());
                    emailArray.add(i, jobDetailItemList.get(i).getEmail());
                    jobDescArray.add(i, jobDetailItemList.get(i).getJobDesc());
                    contactArray.add(i, jobDetailItemList.get(i).getContact());
                    companyDescArray.add(i, jobDetailItemList.get(i).getCompanyDesc());
                    createDateArray.add(i, jobDetailItemList.get(i).getCreateDate());
                    salaryArray.add(i, jobDetailItemList.get(i).getSalary());

                    jobTitleView.setText(jobTitleArray.get(0));
                    companyView.setText("( " + companyArray.get(0) + " )");
                    createDateView.setText(createDateArray.get(0));

                    textView1.setText("Job Function");
                    textView3.setText("Industry");
                    textView5.setText("Job Description");
                    textView7.setText("Salary");

                    textView2.setText(jobAreaArray.get(0));
                    textView4.setText(industryArray.get(0));
                    textView6.setText(jobDescArray.get(0));
                    textView8.setText(salaryArray.get(0));

                }
            }



        }
    }


    class applyJobAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<ItemsInfoBaseXML> applyJobItemList = new ArrayList<ItemsInfoBaseXML>();
        ApplyJobDao applyJobItemDao = new ApplyJobDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //loadingInternetDialog = new ProgressDialog(JobDetailActivity.this);
            //loadingInternetDialog.setMessage("Loading...");
            //loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            applyJobItemList = applyJobItemDao.applyJobItemDao(udid, jobId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //loadingInternetDialog.dismiss();

            if(applyJobItemList == null || applyJobItemList.isEmpty()){
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();

            }
            else {
                String dialogMessage = applyJobItemList.get(0).getMsg();

                new MaterialDialog.Builder(JobDetailActivity.this)
                        .content(dialogMessage)
                        .positiveText("ok")
                        .show();
            }
        }
    }
}
