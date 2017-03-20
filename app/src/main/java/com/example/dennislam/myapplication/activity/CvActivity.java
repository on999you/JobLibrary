package com.example.dennislam.myapplication.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialcamera.MaterialCamera;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.GlobalClass;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.GetCvDao;
import com.example.dennislam.myapplication.dao.criteria.EducationLevelDao;
import com.example.dennislam.myapplication.dao.SendCvDao;
import com.example.dennislam.myapplication.xml.EducationLevelXML;
import com.example.dennislam.myapplication.xml.GetCvXML;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;

public class CvActivity extends BaseActivity {

    File videoFile = new File("");

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
    TextView educationLevelField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cv, null, false);
        mDrawer.addView(contentView, 0);

        settings = getSharedPreferences(data2,0);
        videoCvName = settings.getString("existingVideoCv", "");

        nameField = (EditText)findViewById(R.id.nameField);
        emailField = (EditText)findViewById(R.id.emailField);
        mobileNoField = (EditText)findViewById(R.id.mobileNoField);
        expectedSalaryField = (EditText)findViewById(R.id.expectedSalaryField);
        educationLevelField = (TextView)findViewById(R.id.educationLevelField);

        Button sendCvButton = (Button)findViewById(R.id.sendCvButton);
        Drawable exclamation= ResourcesCompat.getDrawable(getResources(), R.drawable.exclamation_mark, null);
        exclamation.setBounds(0, 0, exclamation.getMinimumWidth(), exclamation.getMinimumHeight());
        sendCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().isEmpty() || emailField.getText().toString().isEmpty() || mobileNoField.getText().toString().isEmpty()
                        || expectedSalaryField.getText().toString().isEmpty() || educationLevelId.isEmpty()){
                    alertMsg("Empty Field(s) Found","Please fill in every field !");
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



    private void alertMsg(String title,String msg){
        android.app.AlertDialog.Builder myAD = new android.app.AlertDialog.Builder(this);
        myAD.setTitle(title);
        myAD.setMessage(msg);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        myAD.setNeutralButton("OK",OkClick);
        myAD.show();
    }

    class getEduLvAndCvAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetCvXML.GetCvItem> getCvItemList = new ArrayList<GetCvXML.GetCvItem>();
        List<EducationLevelXML.EducationLevelItem> educationLevelItemList = new ArrayList<EducationLevelXML.EducationLevelItem>();
        EducationLevelDao educationLevelItemDao = new EducationLevelDao();
        GetCvDao getCvItemDao = new GetCvDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(CvActivity.this);
            loadingInternetDialog.setMessage("Loading...");
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
                        .content("Internet are not working")
                        .positiveText("ok")
                        .show();
            }
            else{
                for(int i = 0; i < educationLevelItemList.size(); i++){
                    educationLevelArray.add(i, educationLevelItemList.get(i).getEducationName());
                    educationLevelIdArray.add(i, educationLevelItemList.get(i).getEducationID());
                }
            }

            //CV
            if(getCvItemList == null){
                new MaterialDialog.Builder(CvActivity.this)
                        .content("Internet are not working")
                        .positiveText("ok")
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
                .content("Take a new video / Play currently video ?")
                .positiveText("Take")
                .negativeText("Play")
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
                                .videoPreferredHeight(640)
                                //.qualityProfile(MaterialCamera.QUALITY_480P)
                                .videoPreferredAspect(4f / 3f)
                                .countdownMinutes(0.5f)
                                .videoFrameRate(30)

                                .showPortraitWarning(false)
                                .saveDir(saveDir)
                                .allowRetry(true)
                                .defaultToFrontFacing(false)
                                .autoSubmit(false)
                                .labelConfirm(R.string.mcam_use_video);

                        materialCamera.start(CAMERA_RQ);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoCvName));
                        intent.setDataAndType(Uri.parse(videoCvName), "video/mp4");
                        startActivity(intent);
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
                //Toast.makeText(this, String.format("Saved to: %s, size: %s",
                        //file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                ImageButton click_videobox = (ImageButton)findViewById(R.id.click_videobox);
                click_videobox.setImageBitmap(thumb);

                settings.edit()
                        .putString("existingVideoCv", file.getAbsolutePath())
                        .apply();
                videoCvName = settings.getString("existingVideoCv", "");


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

            new AlertDialog.Builder(CvActivity.this)
                    .setMessage(dialogMessage)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

}
