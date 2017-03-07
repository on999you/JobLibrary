package com.example.dennislam.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.dennislam.myapplication.AppliedJobGetDataTask;
import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.adapter.AppliedJobCardViewAdapter;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobActivity extends BaseActivity{

    //Card View
    private AnimRFRecyclerView recyclerView;
    private AnimRFLinearLayoutManager manager ;
    private View headerView;
    private View footerView;
    private AppliedJobCardViewAdapter customAdapter;
    private List<String> jobTitleList= new ArrayList<>();
    private List<String> companyNameList= new ArrayList<>();
    private List<String> applyDateList= new ArrayList<>();
    int rownumStart, rownumEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_applied_job, null, false);
        mDrawer.addView(contentView, 0);

        //Card View
        recyclerView = (AnimRFRecyclerView)findViewById(R.id.refresh_layout);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, null);
        customAdapter = new AppliedJobCardViewAdapter(this,jobTitleList,companyNameList,applyDateList);
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
        new AppliedJobGetDataTask(recyclerView,jobTitleList,companyNameList,applyDateList,rownumStart,rownumEnd).execute();
    }

    public void newData() {
        jobTitleList.clear();
        companyNameList.clear();
        applyDateList.clear();
        new AppliedJobGetDataTask(recyclerView,jobTitleList,companyNameList,applyDateList,rownumStart,rownumEnd).execute();
    }

}
