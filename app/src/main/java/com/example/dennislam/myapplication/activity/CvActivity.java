package com.example.dennislam.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialcamera.MaterialCamera;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.GetCurrentCvDao;
import com.example.dennislam.myapplication.dao.criteria.EducationLevelDao;
import com.example.dennislam.myapplication.dao.SendCvDao;
import com.example.dennislam.myapplication.xml.GetEducationLevelXML;
import com.example.dennislam.myapplication.xml.GetCvXML;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;

public class CvActivity extends BaseActivity {

    String currLanguage;

    File videoFile = new File("");
    ImageButton clickToVideo;
    Bitmap previewVideo;

    String videoCvName;
    private SharedPreferences settings;
    private static final String data2 = "DATA";

    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;

    String udid,name,email,mobileNo,expectedSalary;
    String educationLevelId = "";

    ArrayList<String> educationLevelArray = new ArrayList<String>();
    ArrayList<String> educationLevelIdArray = new ArrayList<String>();

    EditText nameField, emailField, mobileNoField, expectedSalaryField;
    TextView educationLevelField, videoCvField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cv, null, false);
        mDrawer.addView(contentView, 0);

        settings = getSharedPreferences(data2,0);
        videoCvName = settings.getString("existingVideoCv", "");

        File file = new File(videoCvName);
        videoFile = file;

        videoCvField = (TextView)findViewById(R.id.videoCvField);

        if(!videoCvName.isEmpty()){
            videoCvField.setText(res.getString(R.string.Cv_reminder1));
            //previewVideo = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
            //clickToVideo.setImageBitmap(previewVideo);
        }

        //Detect Language
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        if("zh".equals(prefs.getString("Language", ""))){
            currLanguage = "chi";
            Log.v("testinglang", "chi now");
        } else{
            currLanguage = "eng";
            Log.v("testinglang", "eng now");
        }

        nameField = (EditText)findViewById(R.id.nameField);
        emailField = (EditText)findViewById(R.id.emailField);
        mobileNoField = (EditText)findViewById(R.id.mobileNoField);
        expectedSalaryField = (EditText)findViewById(R.id.expectedSalaryField);
        educationLevelField = (TextView)findViewById(R.id.educationLevelField);
        //clickToVideo = (ImageButton)findViewById(R.id.clickToVideo);

        Drawable form_name= ResourcesCompat.getDrawable(getResources(), R.drawable.form_name, null);
        form_name.setBounds(80, 0, 140, 60);

        Drawable form_email= ResourcesCompat.getDrawable(getResources(), R.drawable.form_email, null);
        form_email.setBounds(80, 0, 140, 60);

        Drawable form_mobile_num= ResourcesCompat.getDrawable(getResources(), R.drawable.form_mobile_num, null);
        form_mobile_num.setBounds(80, 0, 140, 60);

        Drawable form_salary= ResourcesCompat.getDrawable(getResources(), R.drawable.form_salary, null);
        form_salary.setBounds(80, 0, 140, 60);

        Drawable form_education_level= ResourcesCompat.getDrawable(getResources(), R.drawable.form_education_level, null);
        form_education_level.setBounds(80, 0, 140, 60);

        Drawable form_cv= ResourcesCompat.getDrawable(getResources(), R.drawable.form_camera, null);
        form_cv.setBounds(80, 0, 140, 60);

        nameField.setCompoundDrawables(form_name,null,null,null);
        emailField.setCompoundDrawables(form_email,null,null,null);
        mobileNoField.setCompoundDrawables(form_mobile_num,null,null,null);
        expectedSalaryField.setCompoundDrawables(form_salary,null,null,null);
        educationLevelField.setCompoundDrawables(form_education_level,null,null,null);
        videoCvField.setCompoundDrawables(form_cv,null,null,null);

        Button sendCvButton = (Button)findViewById(R.id.sendCvButton);
        Drawable exclamation= ResourcesCompat.getDrawable(getResources(), R.drawable.exclamation_mark, null);
        exclamation.setBounds(0, 0, exclamation.getMinimumWidth(), exclamation.getMinimumHeight());
        sendCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().isEmpty() || emailField.getText().toString().isEmpty() || mobileNoField.getText().toString().isEmpty()
                        || expectedSalaryField.getText().toString().isEmpty() || educationLevelId.isEmpty()){
                    alertMsg(R.string.Cv_reminder2,R.string.Cv_reminder10);
                } else{
                    name = nameField.getText().toString();
                    email = emailField.getText().toString();
                    mobileNo = mobileNoField.getText().toString();
                    expectedSalary = expectedSalaryField.getText().toString();
                    new sendCvAsyncTaskRunner().execute();
                }

                //add drawleft if fields did not complete
                if(nameField.getText().toString().isEmpty()){nameField.setCompoundDrawables(exclamation,null,null,null);}else{nameField.setCompoundDrawables(null,null,null,null);}
                if(emailField.getText().toString().isEmpty()){emailField.setCompoundDrawables(exclamation,null,null,null);}else{emailField.setCompoundDrawables(null,null,null,null);}
                if(mobileNoField.getText().toString().isEmpty()){mobileNoField.setCompoundDrawables(exclamation,null,null,null);}else{mobileNoField.setCompoundDrawables(null,null,null,null);}
                if(expectedSalaryField.getText().toString().isEmpty()){expectedSalaryField.setCompoundDrawables(exclamation,null,null,null);}else{expectedSalaryField.setCompoundDrawables(null,null,null,null);}
                if(educationLevelId.isEmpty()){educationLevelField.setCompoundDrawables(exclamation,null,null,null);}else{educationLevelField.setCompoundDrawables(null,null,null,null);}
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getEduLvAndCvAsyncTaskRunner().execute();
        }
    }

    private void alertMsg(int title,int msg){
        android.app.AlertDialog.Builder myAD = new android.app.AlertDialog.Builder(this);
        myAD.setTitle(title);
        myAD.setMessage(msg);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        myAD.setNeutralButton(R.string.baseAct_reminder3,OkClick);
        myAD.show();
    }

    class getEduLvAndCvAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetCvXML.GetCvItem> getCvItemList = new ArrayList<GetCvXML.GetCvItem>();
        List<GetEducationLevelXML.EducationLevelItem> educationLevelItemList = new ArrayList<GetEducationLevelXML.EducationLevelItem>();
        EducationLevelDao educationLevelItemDao = new EducationLevelDao();
        GetCurrentCvDao getCvItemDao = new GetCurrentCvDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(CvActivity.this);
            loadingInternetDialog.setMessage("Loading..");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            educationLevelItemList = educationLevelItemDao.getEducationLevelItemDao();

            udid = globalVariable.getUdid();
            getCvItemList = getCvItemDao.getCvItemDao(udid);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if(educationLevelItemList == null || educationLevelItemList.isEmpty()) {
                new MaterialDialog.Builder(CvActivity.this)
                        .content(res.getString(R.string.Cv_reminder4))
                        .positiveText(res.getString(R.string.baseAct_reminder3))
                        .show();
            }
            else{
                for(int i = 0; i < educationLevelItemList.size(); i++){

                    if(currLanguage == "chi") {
                        educationLevelArray.add(i, educationLevelItemList.get(i).getEducationNameChi());
                    } else {
                        educationLevelArray.add(i, educationLevelItemList.get(i).getEducationName());
                    }
                    educationLevelIdArray.add(i, educationLevelItemList.get(i).getEducationID());
                }
            }

            //CV
            if(getCvItemList == null){
                new MaterialDialog.Builder(CvActivity.this)
                        .content(res.getString(R.string.Cv_reminder4))
                        .positiveText(res.getString(R.string.baseAct_reminder3))
                        .show();
            }
            else{
                String getName = getCvItemList.get(0).getName();
                String getEmail = getCvItemList.get(0).getEmailAddress();
                String getMobileNo = getCvItemList.get(0).getMobileNo();
                String getExpectedSalary = getCvItemList.get(0).getExpectedSalary();
                String getEducationLevel = getCvItemList.get(0).getEducationLevel();

                nameField.setText(getName);
                emailField.setText(getEmail);
                mobileNoField.setText(getMobileNo);
                expectedSalaryField.setText(getExpectedSalary);
                educationLevelId = getEducationLevel;

                for(int i = 0; i<educationLevelIdArray.size(); i++){
                    if(educationLevelIdArray.get(i).equals(getEducationLevel)){
                        educationLevelField.setText(educationLevelArray.get(i));
                    }
                }

            }
        }
    }

    public void EducationLevelDialog(View v) {
        System.out.println(educationLevelArray);
        if(educationLevelArray.isEmpty()) {
            Toast.makeText(getBaseContext(), "Cannot get education level", Toast.LENGTH_LONG).show();
        }
        else {
            new MaterialDialog.Builder(this)
                    .title("Education Level")
                    .widgetColor(Color.parseColor("#6F9394"))
                    .items(educationLevelArray.toArray(new CharSequence[educationLevelArray.size()]))
                    .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                        TextView educationLevelField = (TextView)findViewById(R.id.educationLevelField);
                        educationLevelField.setText(text);
                        educationLevelId = educationLevelIdArray.get(which);
                        return true; // allow selection
                    })
                    .positiveColor(Color.parseColor("#486E76"))
                    .positiveText("Done")
                    .show();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void startRecordVideo(View view) {

        new MaterialDialog.Builder(this)
                .content(res.getString(R.string.Cv_reminder5))
                .positiveText(res.getString(R.string.Cv_reminder6))
                .negativeText(res.getString(R.string.Cv_reminder7))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        File saveDir = null;

                        if (ContextCompat.checkSelfPermission(CvActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
                            saveDir = new File(Environment.getExternalStorageDirectory(), "JobLibrary");
                            saveDir.mkdirs();
                        }

                        MaterialCamera materialCamera = new MaterialCamera(CvActivity.this)
                                .forceCamera1()
                                .primaryColor(Color.parseColor("#3E5975"))
                                .videoEncodingBitRate(500000)
                                .audioEncodingBitRate(50000)
                                .videoPreferredHeight(720)
                                //.qualityProfile(MaterialCamera.QUALITY_480P)
                                .videoPreferredAspect(16f / 9f)
                                .countdownMinutes(0.5f)
                                .videoFrameRate(30)

                                .showPortraitWarning(false)
                                .saveDir(saveDir)
                                .allowRetry(true)
                                .defaultToFrontFacing(false)
                                .autoSubmit(false)
                                .labelConfirm(R.string.mcam_use_video)
                                .iconRecord(R.drawable.video_start)
                                .iconFrontCamera(R.drawable.video_change)
                                .iconRearCamera(R.drawable.video_change);

                        materialCamera.start(CAMERA_RQ);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        File file = new File(videoCvName);
                        if(file.exists()){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoCvName));
                            intent.setDataAndType(Uri.parse(videoCvName), "video/mp4");
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getBaseContext(), res.getString(R.string.Cv_reminder9), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            return;
        }

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                final File file = new File(data.getData().getPath());

                //previewVideo = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
                //clickToVideo.setImageBitmap(previewVideo);

                videoCvField.setText(res.getString(R.string.Cv_reminder1));
                videoFile = file;

            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class sendCvAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<ItemsInfoBaseXML> cvItemList = new ArrayList<ItemsInfoBaseXML>();
        SendCvDao cvItemDao = new SendCvDao();

        @Override
        protected Void doInBackground(Void... params) {
            udid = globalVariable.getUdid();

            cvItemList = cvItemDao.sendCvItemDao(videoFile,udid,name,email,mobileNo,expectedSalary,educationLevelId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            int feedbackStatusCode = cvItemList.get(0).getStatus_code();
            String dialogMessage = cvItemList.get(0).getMsg();

            System.out.println(feedbackStatusCode);

            settings.edit()
                    .putString("existingVideoCv", videoFile.getAbsolutePath())
                    .apply();
            videoCvName = settings.getString("existingVideoCv", "");


            new MaterialDialog.Builder(CvActivity.this)
                    .content(dialogMessage)
                    .positiveText("ok")
                    .show();
        }
    }

}
