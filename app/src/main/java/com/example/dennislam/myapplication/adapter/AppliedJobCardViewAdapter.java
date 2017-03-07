package com.example.dennislam.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennislam.myapplication.R;

import java.util.List;

/**
 * Created by dennislam on 6/3/2017.
 */

public class AppliedJobCardViewAdapter extends RecyclerView.Adapter<AppliedJobCardViewAdapter.ViewHolder>{

    private Context context;
    private List<String> jobTitleList;
    private List<String> companyNameList;
    private List<String> applyDateList;

    public AppliedJobCardViewAdapter(Context context,List<String> jobTitleList2, List<String> companyNameList2, List<String> applyDateList2) {
        this.context=context;
        this.jobTitleList = jobTitleList2;
        this.companyNameList = companyNameList2;
        this.applyDateList = applyDateList2;
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitle;
        public TextView companyName;
        public TextView applyDate;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = (TextView)itemView.findViewById(R.id.jobTitle);
            companyName = (TextView)itemView.findViewById(R.id.companyName);
            applyDate = (TextView)itemView.findViewById(R.id.applyDate);
        }

    }


    @Override
    public AppliedJobCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.applied_job_cards_layout,parent,false);
        AppliedJobCardViewAdapter.ViewHolder myViewHolder = new AppliedJobCardViewAdapter.ViewHolder(itemView);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(AppliedJobCardViewAdapter.ViewHolder holder, int position) {
        TextView jobTitle = holder.jobTitle;
        jobTitle.setText(jobTitleList.get(position));

        TextView companyName = holder.companyName;
        companyName.setText(companyNameList.get(position));

        TextView applyDate = holder.applyDate;
        applyDate.setText("Apply Date : " + System.lineSeparator() + applyDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return jobTitleList.size();
    }

}
