package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.lapakkreatiflamongan.smdsforce.schema.Data_JbpReview;

/**
 * Created by handy on 07/01/2020.
 * contact : Handikadwiputradev@gmail.com
 * Project : app.bcp.supervision
 */

public class Adapter_JbpReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context appContext;
    private static final int MULTI_JBP_REVIEW = 0;
    private static final int SINGLE_JBP_REVIEW = 1;
    private LayoutInflater mLayoutInflater;

    private ArrayList <Data_JbpReview> dataJbpReviewArrayList;

    Adapter_JbpReview(Context appContext, ArrayList<Data_JbpReview> dataJbpReviewArrayList) {
        this.appContext = appContext;
        this.dataJbpReviewArrayList = dataJbpReviewArrayList;
        mLayoutInflater = LayoutInflater.from(appContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SINGLE_JBP_REVIEW:
            //    View view = mLayoutInflater.inflate(R.layout.item_jbpreview, parent, false);
           //     return new ViewHolder_SingleJbpReview(view);
            case MULTI_JBP_REVIEW:
          //      View viewMulti = mLayoutInflater.inflate(R.layout.item_multijbpreview,parent,false);
        //        return new ViewHolder_MultiJbpReview(viewMulti);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //if (holder instanceof ViewHolder_SingleJbpReview) ((ViewHolder_SingleJbpReview) holder).bindData(dataJbpReviewArrayList);
       // if (holder instanceof ViewHolder_MultiJbpReview)  ((ViewHolder_MultiJbpReview) holder).bindData(dataJbpReviewArrayList);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return dataJbpReviewArrayList.size() > 1 ? MULTI_JBP_REVIEW : SINGLE_JBP_REVIEW;
    }

    @Override
    public int getItemCount() {
        return dataJbpReviewArrayList.size();
    }

}