package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.dennislam.myapplication.R;
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

    Boolean needLoadMore = true;

    String jobTitle, workExpFrom, workExpTo;
    Boolean withSimilarWord;



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
        customAdapter = new RelevantDataCardViewAdapter(this,relevantJobTitleList,relevantJobCatList,relevantWorkExpList,relevantSalaryList);
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
                        addData(needLoadMore);
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

    private void addData(Boolean needLoadMore) {
        if(needLoadMore) {
            rownumStart += 5;
            rownumEnd += 5;
            new RelevantDataGetDataTask().execute();
        }
        else{
            System.out.println("No more data");
        }
    }

    public void newData() {
        relevantJobTitleList.clear();
        relevantJobCatList.clear();
        relevantWorkExpList.clear();
        relevantSalaryList.clear();
        new RelevantDataGetDataTask().execute();
    }


    RelevantDataDao relevantDataItemDao = new RelevantDataDao();
    List<RelevantDataXML.RelevantDataItem> relevantDataItemList = new ArrayList<RelevantDataXML.RelevantDataItem>();

    class RelevantDataGetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            relevantDataItemList = relevantDataItemDao.getRelevantDataItemDao(jobTitle,rownumStart,rownumEnd);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setRefresh(false);

            if(relevantDataItemList == null){
                needLoadMore = false;
            }
            else{
                for(int i = 0; i < relevantDataItemList.size(); i++) {
                    relevantJobTitleList.add(relevantDataItemList.get(i).getJobTitle());
                    relevantJobCatList.add(relevantDataItemList.get(i).getJobCat());
                    relevantWorkExpList.add(relevantDataItemList.get(i).getExp());
                    relevantSalaryList.add("$" + relevantDataItemList.get(i).getSalary());
                }


            }


        }
    }

}
