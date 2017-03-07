package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dennislam.myapplication.JobListGetDataTask;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.RecyclerItemClickListener;
import com.example.dennislam.myapplication.RelevantDataGetDataTask;
import com.example.dennislam.myapplication.adapter.JobListCardViewAdapter;
import com.example.dennislam.myapplication.adapter.RelevantDataCardViewAdapter;
import com.example.dennislam.myapplication.dao.RelevantDataDao;
import com.example.dennislam.myapplication.xml.RelevantDataXML;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class RelevantDataActivity extends BaseActivity {

    //Card View
    private AnimRFRecyclerView recyclerView;
    private AnimRFLinearLayoutManager manager ;
    private View headerView;
    private View footerView;
    private RelevantDataCardViewAdapter customAdapter;
    private List<String> relevantJobTitleList= new ArrayList<>();
    private List<String> relevantJobCatList= new ArrayList<>();
    private List<String> relevantWorkExpList= new ArrayList<>();
    private List<String> relevantSalaryList= new ArrayList<>();
    int rownumStart, rownumEnd;


    String jobTitle, workExpFrom, workExpTo;
    Boolean withSimilarWord;

    ArrayList<String> relevantJobTitleArray = new ArrayList<String>();
    ArrayList<String> relevantJobCatArray = new ArrayList<String>();
    ArrayList<String> relevantWorkExpArray = new ArrayList<String>();
    ArrayList<String> relevantSalaryArray = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_relevant_data, null, false);
        mDrawer.addView(contentView, 0);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            jobTitle =(String) b.get("jobTitle");
            withSimilarWord =(Boolean) b.get("withSimilarWord");
            workExpFrom =(String) b.get("workExpFrom");
            workExpTo =(String) b.get("workExpTo");
        }

        /*
        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new getRelevantDataAsyncTaskRunner().execute();
        }
        */




        //Card View
        recyclerView = (AnimRFRecyclerView)findViewById(R.id.refresh_layout2);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, null);
        customAdapter = new RelevantDataCardViewAdapter(this,relevantJobTitleList,relevantJobCatArray,relevantWorkExpArray,relevantSalaryArray);
        recyclerView.addHeaderView(headerView);
        recyclerView.addFootView(footerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setHeaderImage((ImageView)headerView.findViewById(R.id.iv_hander));
        manager = new AnimRFLinearLayoutManager(this);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(manager);

        recyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener(){
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rownumStart = 1;
                        rownumEnd = 5;
                        newData();
                        recyclerView.refreshComplate();
                    }
                }).start();
            }
            @Override
            public void onLoadMore() {
                Runnable myRun = new Runnable() {
                    @Override
                    public void run() {
                        addData();
                    }
                };
                Thread loadMore = new Thread(myRun);
                loadMore.start();
                loadMoreComplate();
                recyclerView.loadMoreComplate();
                //recyclerView.loadMoreComplate();
            }
        });
        recyclerView.setRefresh(true);


    }

    public void loadMoreComplate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addData() {
        rownumStart += 5;
        rownumEnd += 5;
        new RelevantDataGetDataTask(jobTitle,recyclerView,relevantJobTitleList,relevantJobCatArray,relevantWorkExpArray,relevantSalaryArray,rownumStart,rownumEnd).execute();
    }

    public void newData() {
        relevantJobTitleList.clear();
        relevantJobCatArray.clear();
        relevantWorkExpArray.clear();
        relevantSalaryArray.clear();
        new RelevantDataGetDataTask(jobTitle,recyclerView,relevantJobTitleList,relevantJobCatArray,relevantWorkExpArray,relevantSalaryArray,rownumStart,rownumEnd).execute();
    }

}
