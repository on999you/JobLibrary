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

    public RelevantDataCardViewAdapter(Context context, List<String> listItemss, List<String> listItems, List<String> listItems2, List<String> listItems3) {
        this.context=context;
        this.relevantJobTitleList = listItemss;
        this.relevantJobCatList = listItems;
        this.relevantWorkExpList = listItems2;
        this.relevantSalaryList = listItems3;

        System.out.println(relevantJobTitleList);
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitle;
        public TextView textViewName2;
        public TextView textViewName3;
        public TextView textViewName4;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = (TextView)itemView.findViewById(R.id.jobTitle);
            textViewName2 = (TextView)itemView.findViewById(R.id.textView2);
            textViewName3 = (TextView)itemView.findViewById(R.id.textView3);
            textViewName4 = (TextView)itemView.findViewById(R.id.textView4);
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
        jobTitle.setText(relevantJobTitleList.get(position));

        TextView textViewName2 = holder.textViewName2;
        textViewName2.setText(relevantJobCatList.get(position));

        TextView textViewName3 = holder.textViewName3;
        textViewName3.setText(relevantWorkExpList.get(position));

        TextView textViewName4 = holder.textViewName4;
        textViewName4.setText(relevantSalaryList.get(position));
    }

    @Override
    public int getItemCount() {
        return relevantJobTitleList.size();
    }

}
