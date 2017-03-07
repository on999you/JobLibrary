package com.example.dennislam.myapplication.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.criteria.EducationLevelDao;
import com.example.dennislam.myapplication.dao.SendCvDao;
import com.example.dennislam.myapplication.xml.EducationLevelXML;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;

public class CvActivity extends BaseActivity {

    File videoFile = new File("");

    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;

    List<ItemsInfoBaseXML> cvItemList = new ArrayList<ItemsInfoBaseXML>();
    ProgressBar progressBar;
    String udid,name,email,mobileNo,expectedSalary;
    String educationLevelId = "";

    SendCvDao cvItemDao = new SendCvDao();

    List<EducationLevelXML.EducationLevelItem> educationLevelItemList = new ArrayList<EducationLevelXML.EducationLevelItem>();
    ArrayList<String> educationLevelArray = new ArrayList<String>();
    ArrayList<String> educationLevelIdArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cv, null, false);
        mDrawer.addView(contentView, 0);

        final EditText nameField = (EditText)findViewById(R.id.nameField);
        final EditText emailField = (EditText)findViewById(R.id.emailField);
        final EditText mobileNoField = (EditText)findViewById(R.id.mobileNoField);
        final EditText expectedSalaryField = (EditText)findViewById(R.id.expectedSalaryField);

        Button sendCvButton = (Button)findViewById(R.id.sendCvButton);

        sendCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameField.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "Please enter your full name first", Toast.LENGTH_LONG).show();
                } else if(emailField.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter your email address first", Toast.LENGTH_LONG).show();
                } else if(mobileNoField.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter your mobile number first", Toast.LENGTH_LONG).show();
                } else if(expectedSalaryField.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter the expected salary first", Toast.LENGTH_LONG).show();
                } else if(educationLevelId.isEmpty()){
                    Toast.makeText(view.getContext(), "Please select the education level first", Toast.LENGTH_LONG).show();
                } else if(!videoFile.exists()){
                    Toast.makeText(view.getContext(), "Please take a video first", Toast.LENGTH_LONG).show();
                } else {
                    name = nameField.getText().toString();
                    email = emailField.getText().toString();
                    mobileNo = mobileNoField.getText().toString();
                    expectedSalary = expectedSalaryField.getText().toString();
                    new sendCvAsyncTaskRunner().execute();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getEduLevelAsyncTaskRunner().execute();
        }


    }

    class getEduLevelAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            EducationLevelDao educationLevelItemDao = new EducationLevelDao();
            educationLevelItemList = educationLevelItemDao.getEducationLevelItemDao();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(educationLevelItemList == null || educationLevelItemList.isEmpty()) {
                Toast.makeText(getBaseContext(), "Internet are not working", Toast.LENGTH_LONG).show();
            }
            else{
                for(int i = 0; i < educationLevelItemList.size(); i++){
                    educationLevelArray.add(i, educationLevelItemList.get(i).getEducationName());
                    educationLevelIdArray.add(i, educationLevelItemList.get(i).getEducationID());
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
            new android.app.AlertDialog.Builder(this)
                    .setSingleChoiceItems(educationLevelArray.toArray(new String[educationLevelArray.size()]), 0, null)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            int selectedPosition = ((android.app.AlertDialog)dialog).getListView().getCheckedItemPosition();
                            TextView educationLevelField = (TextView)findViewById(R.id.educationLevelField);
                            educationLevelField.setText(((android.app.AlertDialog)dialog).getListView().getItemAtPosition(selectedPosition).toString());
                            educationLevelId = educationLevelIdArray.get(selectedPosition);

                        }
                    })
                    .show();
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void startRecordVideo(View view) {

        File saveDir = null;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
            saveDir.mkdirs();
        }

        MaterialCamera materialCamera = new MaterialCamera(this)
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

        @Override
        protected Void doInBackground(Void... params) {
            udid = globalVariable.getUdid();

            cvItemList = cvItemDao.getCvItemDao(videoFile,udid,name,email,mobileNo,expectedSalary,educationLevelId);
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
