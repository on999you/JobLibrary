package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.RecyclerItemClickListener;
import com.example.dennislam.myapplication.adapter.JobListCardViewAdapter;
import com.example.dennislam.myapplication.dao.GetJobListDao;
import com.example.dennislam.myapplication.xml.GetJobListXML;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends BaseActivity {

    String finalJobTitle, salaryMin, salaryMax;
    Boolean withSimilarWord;
    ArrayList<String> finalSelectedJobCatArray = new ArrayList<String>();
    ArrayList<String> finalSelectedJobIndustryArray = new ArrayList<String>();

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
    int rownumStart = 0, rownumEnd = 0;

    Boolean needLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_job_list, null, false);
        mDrawer.addView(contentView, 0);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            finalJobTitle = (String) b.get("jobTitle");
            withSimilarWord =(Boolean) b.get("withSimilarWord");
            finalSelectedJobCatArray =(ArrayList<String>) b.get("jobCat");
            finalSelectedJobIndustryArray =(ArrayList<String>) b.get("jobIndustry");
            salaryMin =(String) b.get("salaryMin");
            salaryMax =(String) b.get("salaryMax");
        }

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
                JobListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rownumStart = 1;
                        rownumEnd = 5;
                        needLoadMore = true;
                        newData();
                    }
                });
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

    private void addData(Boolean needLoadMore) {
        if(needLoadMore) {
            rownumStart += 5;
            rownumEnd += 5;
            Log.v("testing123", "adddate : " + rownumStart + " " + rownumEnd);
            new getJobListAsyncTaskRunner().execute();
        }
        else{
            System.out.println("No more data");
        }
    }

    public void newData() {
        Log.v("testing123", "newdate : " + rownumStart + " " + rownumEnd);

        recyclerView.getAdapter().notifyDataSetChanged();

        new getJobListAsyncTaskRunner().execute();

        jobTitleList.clear();
        companyNameList.clear();
        createDateList.clear();
    }



    class getJobListAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetJobListXML.JobListItem> jobListItemList = new ArrayList<GetJobListXML.JobListItem>();
        GetJobListDao jobListItemDao = new GetJobListDao();

        @Override
        protected Void doInBackground(Void... params) {
            jobListItemList = jobListItemDao.jobListItemDao(rownumStart, rownumEnd, finalJobTitle, withSimilarWord, finalSelectedJobCatArray, finalSelectedJobIndustryArray, salaryMin, salaryMax);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setRefresh(false);

            if(jobListItemList == null){
                needLoadMore = false;
                Toast.makeText(JobListActivity.this, res.getString(R.string.appJob_reminder1), Toast.LENGTH_LONG).show();
            }
            else{
                for(int i = 0; i < jobListItemList.size(); i++){
                    jobIdList.add(jobListItemList.get(i).getJobID());
                    jobTitleList.add(jobListItemList.get(i).getJobTitle());
                    companyNameList.add(jobListItemList.get(i).getCompany());
                    createDateList.add(jobListItemList.get(i).getCreateDate());
                    salaryList.add(jobListItemList.get(i).getSalary());

                    System.out.println(jobTitleList);
                }

            }

        }
    }
}


