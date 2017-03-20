package com.example.dennislam.myapplication.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.RecyclerItemClickListener;
import com.example.dennislam.myapplication.adapter.JobListCardViewAdapter;
import com.example.dennislam.myapplication.dao.JobListDao;
import com.example.dennislam.myapplication.xml.JobListXML;
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
    int rownumStart = 0, rownumEnd = 0;

    Boolean needLoadMore = true;

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

    private void addData(Boolean needLoadMore) {
        if(needLoadMore) {
            rownumStart += 5;
            rownumEnd += 5;
            new getJobListAsyncTaskRunner().execute();
        }
        else{
            System.out.println("No more data");
        }
    }

    public void newData() {
        jobTitleList.clear();
        companyNameList.clear();
        createDateList.clear();
        new getJobListAsyncTaskRunner().execute();
    }

    class getJobListAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<JobListXML.JobListItem> jobListItemList = new ArrayList<JobListXML.JobListItem>();
        JobListDao jobListItemDao = new JobListDao();

        @Override
        protected Void doInBackground(Void... params) {
            jobListItemList = jobListItemDao.jobListItemDao(rownumStart, rownumEnd);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setRefresh(false);

            if(jobListItemList == null){
                needLoadMore = false;
                Toast.makeText(JobListActivity.this, "No more data", Toast.LENGTH_LONG).show();
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


            /*
            final int notifyID = 1; // 通知的識別號碼
            final boolean autoCancel = true;
            final int requestCode = notifyID; // PendingIntent的Request Code
            final Intent intent = new Intent(getApplicationContext(), MainPageActivity.class); // 開啟另一個Activity的Intent
            final int flags = PendingIntent.FLAG_UPDATE_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
            final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext()); // 建立TaskStackBuilder
            stackBuilder.addParentStack(MainPageActivity.class); // 加入目前要啟動的Activity，這個方法會將這個Activity的所有上層的Activity(Parents)都加到堆疊中
            stackBuilder.addNextIntent(intent); // 加入啟動Activity的Intent
            final PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, flags); // 取得PendingIntent
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
            final Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.drawer_icon)
                    .setContentTitle("Testing - Title")
                    .setContentText("Testing - Text")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(autoCancel)
                    .setVibrate(new long[] { 1000, 1000, 1000 })
                    .build(); // 建立通知
            notificationManager.notify(notifyID, notification); // 發送通知
            */

        }
    }
}


