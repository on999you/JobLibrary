package com.example.dennislam.myapplication.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dennislam.myapplication.adapter.CustomExpandableListAdapter;
import com.example.dennislam.myapplication.ExpandableListDataPump;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.dao.SendFeedbackDao;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedbackActivity extends BaseActivity {
    String udid, name, email, comments;
    float rating;

    SendFeedbackDao feedbackItemDao = new SendFeedbackDao();

    List<ItemsInfoBaseXML> feedbackItemList = new ArrayList<ItemsInfoBaseXML>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_feedback, null, false);
        mDrawer.addView(contentView, 0);

        //ExpandableListView
        ExpandableListView expandableListView;
        ExpandableListAdapter expandableListAdapter;
        final List<String> expandableListTitle;
        HashMap<String, List<String>> expandableListDetail;
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        Button resetFeedback = (Button) findViewById(R.id.resetFeedback);
        Button submitFeedback = (Button) findViewById(R.id.submitFeedback);
        final EditText feedbackName = (EditText) findViewById(R.id.nameField);
        final EditText feedbackEmail = (EditText) findViewById(R.id.emailField);
        final EditText feedbackComment = (EditText) findViewById(R.id.commentField);

        final RatingBar feedbackRating = (RatingBar)findViewById(R.id.feedbackRating);

        resetFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackName.setText("");
                feedbackEmail.setText("");
                feedbackComment.setText("");
            }
        });

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(feedbackName.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "Please enter your name first", Toast.LENGTH_LONG).show();
                } else if(feedbackEmail.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter your email address first", Toast.LENGTH_LONG).show();
                } else if(feedbackComment.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter your comment first", Toast.LENGTH_LONG).show();
                } else {
                    name = feedbackName.getText().toString();
                    email = feedbackEmail.getText().toString();
                    comments = feedbackComment.getText().toString();
                    rating = feedbackRating.getRating();
                    new sendFeedbackAsyncTaskRunner().execute();
                }
            }
        });
    }

    class sendFeedbackAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            udid = globalVariable.getUdid();
            feedbackItemList = feedbackItemDao.getFeedbackItemDao(udid,name,email,comments,rating);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(feedbackItemList == null || feedbackItemList.isEmpty()) {
                Toast.makeText(getBaseContext(), "Internet are not working", Toast.LENGTH_LONG).show();
            }
            else {
                int feedbackStatusCode = feedbackItemList.get(0).getStatus_code();
                String dialogMessage = feedbackItemList.get(0).getMsg();
                System.out.println(feedbackStatusCode);
                Toast.makeText(getBaseContext(), dialogMessage, Toast.LENGTH_LONG).show();

                feedbackItemList.clear();
            }



        }
    }

}
