package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.dennislam.myapplication.JobListGetDataTask;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.RecyclerItemClickListener;
import com.example.dennislam.myapplication.adapter.JobListCardViewAdapter;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends BaseActivity {
//HI
    //Card View
    private AnimRFRecyclerView recyclerView;
    private AnimRFLinearLayoutManager manager ;
    private View headerView;
    private View footerView;
    private JobListCardViewAdapter customAdapter;
    private List<String> jobTitleList= new ArrayList<>();
    private List<String> companyNameList= new ArrayList<>();
    private List<String> createDateList= new ArrayList<>();
    private List<String> jobIdList= new ArrayList<>();
    private List<String> salaryList= new ArrayList<>();
    int rownumStart, rownumEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_job_list, null, false);
        mDrawer.addView(contentView, 0);

        //Card View
        recyclerView = (AnimRFRecyclerView)findViewById(R.id.refresh_layout);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, null);
        customAdapter = new JobListCardViewAdapter(this,jobIdList,jobTitleList,companyNameList,createDateList,salaryList);
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
            }
        });
        recyclerView.setRefresh(true);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getBaseContext(), JobDetailActivity.class);
                        intent.putExtra("jobId", jobIdList.get(position-2));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void loadMoreComplate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addData() {
        rownumStart += 5;
        rownumEnd += 5;
        new JobListGetDataTask(recyclerView,jobIdList,jobTitleList,companyNameList,createDateList,salaryList,rownumStart,rownumEnd).execute();
    }

    public void newData() {
        jobTitleList.clear();
        companyNameList.clear();
        createDateList.clear();
        new JobListGetDataTask(recyclerView,jobIdList,jobTitleList,companyNameList,createDateList,salaryList,rownumStart,rownumEnd).execute();
    }

}
