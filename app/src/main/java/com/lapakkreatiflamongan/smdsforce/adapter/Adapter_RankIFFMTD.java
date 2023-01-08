package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_RankIFFMTD;


public class Adapter_RankIFFMTD extends BaseAdapter {

    private Context appContext;
    private List<Data_RankIFFMTD> dataValueList;
    private LayoutInflater mLayoutInflater;
    DecimalFormatSymbols symbol;
    DecimalFormat formatter;
    float workDayGlobal;

    public Adapter_RankIFFMTD(Context appContext, List<Data_RankIFFMTD> dataValueList,float workDayGlobal) {
        this.appContext = appContext;
        this.dataValueList = dataValueList;
        this.workDayGlobal = workDayGlobal;
        mLayoutInflater = LayoutInflater.from(appContext);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);
    }

    @Override
    public int getCount() {
        return (dataValueList.size() > 0) ? dataValueList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankIFFViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new RankIFFViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.l_mtdreport,null);
            viewHolder.tvActualPercent = convertView.findViewById(R.id.tv_actual_procent);
            viewHolder.tvTimeGonePercent = convertView.findViewById(R.id.tv_timegone_procent);
            viewHolder.tvNameMTDReport = convertView.findViewById(R.id.tv_nama_mtdreport);
            viewHolder.tvChannelMTDReport = convertView.findViewById(R.id.tv_channel_mtdreport);
            viewHolder.tvAddressMTDReport = convertView.findViewById(R.id.tv_alamat_mtdreport);
            viewHolder.ImageViewProcent = convertView.findViewById(R.id.Progress_ImgMTD);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RankIFFViewHolder) convertView.getTag();
        }

        int procent = 0;
        int procentTimeGone = 0;
        if (Float.parseFloat(dataValueList.get(position).getTarget()) > 0) {
            procent = ((int)((Float.parseFloat(dataValueList.get(position).getActual())*100)/Float.parseFloat(dataValueList.get(position).getTarget())));
        }

        if (Float.parseFloat(dataValueList.get(position).getActual()) > 0) {
            procentTimeGone = (int) ((procent * 100) / workDayGlobal);
        }

        String actSales = (String) String.valueOf(dataValueList.get(position).getActual().toString());
        String targetSales = (String) String.valueOf(dataValueList.get(position).getTarget().toString());


        viewHolder.tvActualPercent.setText(procent +"%");
        viewHolder.tvNameMTDReport.setText(dataValueList.get(position).getSellername());
        viewHolder.tvAddressMTDReport.setText(formatter.format(Double.parseDouble(actSales))+"/"+formatter.format(Double.parseDouble(targetSales)));
        viewHolder.tvTimeGonePercent.setText(procentTimeGone+"%");

        if (procentTimeGone >=0f && procentTimeGone <= 94f){
            viewHolder.tvTimeGonePercent.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
            Picasso.get().load(R.drawable.circle_red).into(viewHolder.ImageViewProcent);
        } else if (procentTimeGone >= 95f && procentTimeGone <= 99f) {
            viewHolder.tvTimeGonePercent.setTextColor(appContext.getResources().getColor(R.color.colorBlack));
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorBlack));
            Picasso.get().load(R.drawable.circle_yellow).into(viewHolder.ImageViewProcent);
        } else {
            viewHolder.tvTimeGonePercent.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
            Picasso.get().load(R.drawable.circle_green).into(viewHolder.ImageViewProcent);
        }

        /**
        if (procent<50){
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorRed));
        }else if(procent>=50 && procent<70){
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorYellow));
        }else{
            viewHolder.tvActualPercent.setTextColor(appContext.getResources().getColor(R.color.colorGreen));
        }**/

        return convertView;
    }

    class RankIFFViewHolder {
        TextView tvNameMTDReport,tvAddressMTDReport,tvChannelMTDReport;
        TextView tvActualPercent, tvTimeGonePercent;
        ImageView ImageViewProcent;
    }
}
