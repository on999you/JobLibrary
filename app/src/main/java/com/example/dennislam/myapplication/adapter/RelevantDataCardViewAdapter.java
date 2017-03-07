package com.example.dennislam.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennislam.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennislam on 6/3/2017.
 */

public class RelevantDataCardViewAdapter extends RecyclerView.Adapter<RelevantDataCardViewAdapter.ViewHolder> {

    private Context context;
    private List<String> relevantJobTitleList= new ArrayList<>();
    private List<String> relevantJobCatList= new ArrayList<>();
    private List<String> relevantWorkExpList= new ArrayList<>();
    private List<String> relevantSalaryList= new ArrayList<>();

    public RelevantDataCardViewAdapter(Context context, List<String> relevantJobTitleList2, List<String> relevantJobCatList2, List<String> relevantWorkExpList2, List<String> relevantSalaryList2) {
        this.context=context;
        this.relevantJobTitleList = relevantJobTitleList2;
        this.relevantJobCatList = relevantJobCatList2;
        this.relevantWorkExpList = relevantWorkExpList2;
        this.relevantSalaryList = relevantSalaryList2;

        System.out.println(relevantJobTitleList);
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitle;
        public TextView jobCat;
        public TextView workExp;
        public TextView salary;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = (TextView)itemView.findViewById(R.id.jobTitle);
            jobCat = (TextView)itemView.findViewById(R.id.jobCat);
            workExp = (TextView)itemView.findViewById(R.id.workExp);
            salary = (TextView)itemView.findViewById(R.id.salary);
        }

    }


    @Override
    public RelevantDataCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.relevant_data_cards_layout,parent,false);
        RelevantDataCardViewAdapter.ViewHolder myViewHolder = new RelevantDataCardViewAdapter.ViewHolder(itemView);
        return  myViewHolder;

    }

    @Override
    public void onBindViewHolder(RelevantDataCardViewAdapter.ViewHolder holder, int position) {
        TextView jobTitle = holder.jobTitle;
        jobTitle.setText("Job Title: "+relevantJobTitleList.get(position));

        TextView jobCat = holder.jobCat;
        jobCat.setText(relevantJobCatList.get(position));

        TextView workExp = holder.workExp;
        workExp.setText("WorkExp : "+relevantWorkExpList.get(position));

        TextView salary = holder.salary;
        salary.setText("Wages : "+relevantSalaryList.get(position));
    }

    @Override
    public int getItemCount() {
        return relevantJobTitleList.size();
    }

}
