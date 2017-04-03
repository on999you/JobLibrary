package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.RecyclerItemClickListener;
import com.example.dennislam.myapplication.adapter.AppliedJobCardViewAdapter;
import com.example.dennislam.myapplication.dao.GetAppliedJobDao;
import com.example.dennislam.myapplication.xml.GetAppliedJobXML;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobActivity extends BaseActivity{

    //Set the card view
    private AnimRFRecyclerView recyclerView;
    private AnimRFLinearLayoutManager manager ;
    private View headerView;
    private View footerView;
    private AppliedJobCardViewAdapter customAdapter;

    //List of job information
    private List<String> jobIdList= new ArrayList<>();
    private List<String> jobTitleList= new ArrayList<>();
    private List<String> companyNameList= new ArrayList<>();
    private List<String> applyDateList= new ArrayList<>();

    int rownumStart, rownumEnd;
    Boolean needLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_applied_job, null, false);
        mDrawer.addView(contentView, 0);

        //Set the card view
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
                        rownumEnd = 10;
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
                        addData(needLoadMore);
                    }
                };
                Thread loadMore = new Thread(myRun);
                loadMore.start();
                loadMoreComplate();
                recyclerView.loadMoreComplate();
            }
        });
        recyclerView.setRefresh(true);

        //OnClick function of selecting specific job
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //Pass To Job Detail
                        Intent intent = new Intent(getBaseContext(), JobDetailActivity.class);
                        intent.putExtra("jobId", jobIdList.get(position-2));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //Not necessary to use this function
                    }
                })
        );

    }

    public void loadMoreComplate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addData(Boolean needLoadMore) {
        if(needLoadMore){
            rownumStart += 10;
            rownumEnd += 10;
            new AppliedJobGetDataTask().execute();
        }
        else{
            System.out.println("No more data");
        }

    }

    public void newData() {
        jobTitleList.clear();
        companyNameList.clear();
        applyDateList.clear();
        new AppliedJobGetDataTask().execute();
    }

    class AppliedJobGetDataTask extends AsyncTask<Void, Void, Void> {

        List<GetAppliedJobXML.AppliedJobItem> appliedJobItemList = new ArrayList<GetAppliedJobXML.AppliedJobItem>();
        GetAppliedJobDao appliedJobItemDao = new GetAppliedJobDao();

        @Override
        protected Void doInBackground(Void... params) {
            String udid = globalVariable.getUdid();
            appliedJobItemList = appliedJobItemDao.getAppliedJobItemDao(rownumStart, rownumEnd,udid);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setRefresh(false);

            if(appliedJobItemList == null){
                needLoadMore = false;
                Toast.makeText(AppliedJobActivity.this,"No record was found!",Toast.LENGTH_LONG).show();
            }
            else{
                for(int i = 0; i < appliedJobItemList.size(); i++){
                    jobIdList.add(appliedJobItemList.get(i).getJobId());
                    jobTitleList.add(appliedJobItemList.get(i).getJobTitle());
                    companyNameList.add(appliedJobItemList.get(i).getCompany());
                    applyDateList.add(appliedJobItemList.get(i).getApplyDate());

                    System.out.println(jobTitleList.get(i));
                }
            }


        }
    }

}
