package com.example.dennislam.myapplication;

import android.os.AsyncTask;

import com.example.dennislam.myapplication.dao.JobListDao;
import com.example.dennislam.myapplication.dao.RelevantDataDao;
import com.example.dennislam.myapplication.xml.JobListXML;
import com.example.dennislam.myapplication.xml.RelevantDataXML;
import com.sch.rfview.AnimRFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennislam on 6/3/2017.
 */

public class RelevantDataGetDataTask extends AsyncTask<Void,Void,Void> {

    private AnimRFRecyclerView mRecyclerView;
    private List<String> relevantJobTitleList= new ArrayList<>();
    private List<String> relevantJobCatList= new ArrayList<>();
    private List<String> relevantWorkExpList= new ArrayList<>();
    private List<String> relevantSalaryList= new ArrayList<>();

    int rownumStart, rownumEnd;

    String jobTitle;

    RelevantDataDao relevantDataItemDao = new RelevantDataDao();
    List<RelevantDataXML.RelevantDataItem> relevantDataItemList = new ArrayList<RelevantDataXML.RelevantDataItem>();

    public RelevantDataGetDataTask(String jobTitle2,AnimRFRecyclerView recyclerView, List<String> relevantJobTitleList2, List<String> relevantJobCatList2, List<String> relevantWorkExpList2, List<String> relevantSalaryList2, Integer rownumStart, Integer rownumEnd) {
        jobTitle = jobTitle2;
        mRecyclerView = recyclerView;
        relevantJobTitleList = relevantJobTitleList2;
        relevantJobCatList = relevantJobCatList2;
        relevantWorkExpList = relevantWorkExpList2;
        relevantSalaryList = relevantSalaryList2;

        this.rownumStart=rownumStart;
        this.rownumEnd=rownumEnd;
    }
    


    @Override
    protected Void doInBackground(Void... params) {
        relevantDataItemList = relevantDataItemDao.getRelevantDataItemDao(jobTitle,rownumStart,rownumEnd);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.setRefresh(false);

        if(relevantDataItemList == null){
            System.out.println("no anymore");
            mRecyclerView.getAdapter().notifyDataSetChanged();
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
