package com.example.dennislam.myapplication;

import android.os.AsyncTask;

import com.example.dennislam.myapplication.dao.AppliedJobDao;
import com.example.dennislam.myapplication.dao.JobListDao;
import com.example.dennislam.myapplication.xml.AppliedJobXML;
import com.example.dennislam.myapplication.xml.JobListXML;
import com.sch.rfview.AnimRFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennislam on 7/3/2017.
 */

public class AppliedJobGetDataTask extends AsyncTask<Void,Void,Void> {
    //HOHOHOHOHOH
    //ll
    GlobalClass globalVariable;

    private AnimRFRecyclerView mRecyclerView;
    private List<String> jobTitleList;
    private List<String> companyNameList;
    private List<String> applyDateList;
    int rownumStart, rownumEnd;

    List<AppliedJobXML.AppliedJobItem> appliedJobItemList = new ArrayList<AppliedJobXML.AppliedJobItem>();
    AppliedJobDao appliedJobItemDao = new AppliedJobDao();

    public AppliedJobGetDataTask(AnimRFRecyclerView recyclerView,List<String> jobTitleList2, List<String> companyNameList2, List<String> createDateList2, Integer rownumStart, Integer rownumEnd) {
        mRecyclerView = recyclerView;
        jobTitleList = jobTitleList2;
        companyNameList = companyNameList2;
        applyDateList = createDateList2;
        this.rownumStart=rownumStart;
        this.rownumEnd=rownumEnd;
    }



    @Override
    protected Void doInBackground(Void... params) {
        //String udid = globalVariable.getUdid();
        String udid = "1133XHIKYKJBEW";
        appliedJobItemList = appliedJobItemDao.getAppliedJobItemDao(rownumStart, rownumEnd,udid);
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.setRefresh(false);

        if(appliedJobItemList == null){
            System.out.println("no anymore");
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        else{
            for(int i = 0; i < appliedJobItemList.size(); i++){
                jobTitleList.add(appliedJobItemList.get(i).getJobTitle());
                companyNameList.add(appliedJobItemList.get(i).getCompany());
                applyDateList.add(appliedJobItemList.get(i).getApplyDate());

                System.out.println(jobTitleList.get(i));
            }
        }


    }

}
