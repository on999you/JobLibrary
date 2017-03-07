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
 * Created by shaunlau on 24/1/2017.
 */

public class JobListCardViewAdapter extends RecyclerView.Adapter<JobListCardViewAdapter.ViewHolder> {
    private Context context;
    private List<String> jobTitleList;
    private List<String> companyNameList;
    private List<String> createDateList;
    private List<String> jobIdList;

    public JobListCardViewAdapter(Context context, List<String> jobIdList2, List<String> jobTitleList2, List<String> companyNameList2, List<String> createDateList2) {
        this.context=context;
        this.jobIdList = jobIdList2;
        this.jobTitleList = jobTitleList2;
        this.companyNameList = companyNameList2;
        this.createDateList = createDateList2;
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitle;
        public TextView companyName;
        public TextView createDate;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = (TextView)itemView.findViewById(R.id.jobTitle);
            companyName = (TextView)itemView.findViewById(R.id.companyName);
            createDate = (TextView)itemView.findViewById(R.id.createDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_cards_layout,parent,false);
        ViewHolder myViewHolder = new ViewHolder(itemView);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView jobTitle = holder.jobTitle;
        jobTitle.setText(jobTitleList.get(position));

        TextView companyName = holder.companyName;
        companyName.setText(companyNameList.get(position));

        TextView createDate = holder.createDate;
        createDate.setText(createDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return jobTitleList.size();
    }
}
