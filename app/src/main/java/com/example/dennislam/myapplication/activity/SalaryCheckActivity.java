package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
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
import com.example.dennislam.myapplication.dao.criteria.DataSourceDao;
import com.example.dennislam.myapplication.dao.criteria.IndustryDao;
import com.example.dennislam.myapplication.dao.criteria.JobCatDao;
import com.example.dennislam.myapplication.dao.criteria.SalarySourceDao;
import com.example.dennislam.myapplication.dao.criteria.WorkExpDao;
import com.example.dennislam.myapplication.xml.GetDataSourceXML;
import com.example.dennislam.myapplication.xml.GetJobIndustryXML;
import com.example.dennislam.myapplication.xml.GetJobCatXML;
import com.example.dennislam.myapplication.xml.GetSalarySourceXML;
import com.example.dennislam.myapplication.xml.GetWorkExpXML;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

    ArrayList<String> dataSourceArray = new ArrayList<String>();

    TextView jobFunctionButton , jobIndustryButton, WorkExpFrom, WorkExpTo, salarySource, dataSource;

    //Values that pass to database
    String finalSalarySourceID;
    String finalWorkExpFromID = "";
    String finalWorkExpToID = "";
    String finalDataSource = "0";
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
        if("zh".equals(prefs.getString("Language", ""))){
            currLanguage = "chi";
            Log.v("testinglang", "chi now");
        } else{
            currLanguage = "eng";
            Log.v("testinglang", "eng now");
        }

        final EditText jobTitleField = (EditText)findViewById(R.id.jobTitleField);
        Button searchButton = (Button)findViewById(R.id.searchButton);
        final CheckBox similarCheckBox = (CheckBox)findViewById(R.id.similarCheckBox);

        jobFunctionButton = (TextView)findViewById(R.id.jobFunctionButton);
        jobIndustryButton = (TextView)findViewById(R.id.jobIndustryButton);
        WorkExpFrom = (TextView)findViewById(R.id.WorkExpFrom);
        WorkExpTo = (TextView)findViewById(R.id.WorkExpTo);
        salarySource = (TextView)findViewById(R.id.salarySource);
        dataSource = (TextView)findViewById(R.id.dataSource);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(jobTitleField.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), res.getString(R.string.sC_reminder1), Toast.LENGTH_LONG).show();
                }
                else if( ((!finalWorkExpFromID.equals("") && finalWorkExpToID.equals("")) || (finalWorkExpFromID.equals("") && !finalWorkExpToID.equals(""))) ||
                        ((!finalWorkExpFromID.equals("") && !finalWorkExpToID.equals("")) && (Integer.parseInt(finalWorkExpFromID) > Integer.parseInt(finalWorkExpToID)) )){
                    Toast.makeText(getBaseContext(), "Please select the correct work exp range", Toast.LENGTH_LONG).show();
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
                    intent.putExtra("dataSource", finalDataSource);

                    Log.v("testingPassingCriterias", jobTitleField.getText().toString().trim() + "\n" + similarCheckBox.isChecked() + "\n" + finalSelectedJobCatArray + "\n" + finalSelectedJobIndustryArray + "\n" + finalWorkExpFromID + "\n" + finalWorkExpToID + "\n" + finalSalarySourceID+ "\n" + finalDataSource);
                    startActivity(intent);
                }
            }
        });

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getCriteriasAsyncTaskRunner().execute();
        }

        Drawable jobfun_icon= ResourcesCompat.getDrawable(getResources(), R.drawable.jobfun_icon, null);
        jobfun_icon.setBounds(80, 0, 140, 60);

        Drawable jobind_icon= ResourcesCompat.getDrawable(getResources(), R.drawable.jobind_icon, null);
        jobind_icon.setBounds(80, 0, 140, 60);

        Drawable form_exp= ResourcesCompat.getDrawable(getResources(), R.drawable.form_exp, null);
        form_exp.setBounds(80, 0, 140, 60);

        Drawable form_salary_source= ResourcesCompat.getDrawable(getResources(), R.drawable.form_salary_source, null);
        form_salary_source.setBounds(80, 0, 140, 60);

        Drawable form_source_type= ResourcesCompat.getDrawable(getResources(), R.drawable.form_source_type, null);
        form_source_type.setBounds(80, 0, 140, 60);

        jobFunctionButton.setCompoundDrawables(jobfun_icon,null,null,null);
        jobIndustryButton.setCompoundDrawables(jobind_icon,null,null,null);
        WorkExpFrom.setCompoundDrawables(form_exp,null,null,null);
        WorkExpTo.setCompoundDrawables(form_exp,null,null,null);
        salarySource.setCompoundDrawables(form_salary_source,null,null,null);
        dataSource.setCompoundDrawables(form_source_type,null,null,null);

    }

    class getCriteriasAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetWorkExpXML.WorkExpItem> workExpItemList = new ArrayList<GetWorkExpXML.WorkExpItem>();
        List<GetJobCatXML.JobCatItem> jobCatItemList = new ArrayList<GetJobCatXML.JobCatItem>();
        List<GetJobIndustryXML.IndustryItem> industryItemList = new ArrayList<GetJobIndustryXML.IndustryItem>();
        List<GetSalarySourceXML.SalarySourceItem> salarySourceItemList = new ArrayList<GetSalarySourceXML.SalarySourceItem>();
        List<GetDataSourceXML.DataSourceItem> dataSourceItemList = new ArrayList<GetDataSourceXML.DataSourceItem>();

        WorkExpDao workExpItemDao = new WorkExpDao();
        SalarySourceDao salarySourceItemDao = new SalarySourceDao();
        DataSourceDao dataSourceItemDao = new DataSourceDao();
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
            dataSourceItemList = dataSourceItemDao.getDataSourceItemDao();
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

                    if(currLanguage == "chi") {
                        salarySourceArray.add(i, salarySourceItemList.get(i).getDescriptionChi());
                    } else {
                        salarySourceArray.add(i, salarySourceItemList.get(i).getDescription());
                    }
                    salarySourceIdArray.add(i, salarySourceItemList.get(i).getSoruce_id());
                }
            }

            if(dataSourceItemList == null || dataSourceItemList.isEmpty()) {
                Toast.makeText(SalaryCheckActivity.this, "cannot get data source", Toast.LENGTH_LONG).show();
            } else {
                //Get Salary Source
                for (int i = 0; i < dataSourceItemList.size(); i++) {

                    if(currLanguage == "chi") {
                        dataSourceArray.add(i, dataSourceItemList.get(i).getDataSourceChi());
                    } else {
                        dataSourceArray.add(i, dataSourceItemList.get(i).getDataSource());
                    }
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

    public void dataSourceDialog(View v) {
        new MaterialDialog.Builder(this)
                .title("Source Type")
                .widgetColor(Color.parseColor("#6F9394"))
                .items(dataSourceArray.toArray(new CharSequence[dataSourceArray.size()]))
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    TextView dataSource = (TextView)findViewById(R.id.dataSource);
                    dataSource.setText(res.getString(R.string.sC_reminder9) + "" + text);
                    finalDataSource = Integer.toString(which);
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