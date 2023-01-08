package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_IFF;
import com.lapakkreatiflamongan.smdsforce.schema.Data_IFF_Ex;

/**
 * Created by IT-SUPERMASTER on 02/01/2016.
 */
public class Adapter_IFF extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_IFF_Ex> initdatalist = null;
    private ArrayList<Data_IFF_Ex> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_IFF(Context context, List<Data_IFF_Ex> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_IFF_Ex>();
        this.arraylist.addAll(initdatalist);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

    }

    public class ViewHolder {
        TextView Name,IFF1,IFF2,IFF3,IFF4,IFF5,IFF6,IFF0,Idx;
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_IFF getItem(int position) {
        return initdatalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.l_iff, null);
            // Locate the TextViews in listview_item.xml
            holder.Idx = (TextView) view.findViewById(R.id.IFFL_Idx);
            holder.Name = (TextView) view.findViewById(R.id.IFFL_SellerName);
            holder.IFF0 = (TextView) view.findViewById(R.id.IFFL_iff1);
            holder.IFF1 = (TextView) view.findViewById(R.id.IFFL_iff2);
            holder.IFF2 = (TextView) view.findViewById(R.id.IFFL_iff3);
            holder.IFF3 = (TextView) view.findViewById(R.id.IFFL_iff4);
            holder.IFF4 = (TextView) view.findViewById(R.id.IFFL_iff5);
            holder.IFF5 = (TextView) view.findViewById(R.id.IFFL_iff6);
            holder.IFF6 = (TextView) view.findViewById(R.id.IFFL_iff7);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getSellername());
        holder.IFF0.setText(""+initdatalist.get(position).getIff0());
        holder.IFF1.setText(""+initdatalist.get(position).getIff1());
        holder.IFF2.setText(""+initdatalist.get(position).getIff2());
        holder.IFF3.setText(""+initdatalist.get(position).getIff3());
        holder.IFF4.setText(""+initdatalist.get(position).getIff4());
        holder.IFF5.setText(""+initdatalist.get(position).getIff5());
        holder.IFF6.setText(""+initdatalist.get(position).getIff6());

        if (Float.parseFloat(initdatalist.get(position).getIdx())<=50){
            holder.Idx.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        }else if (Float.parseFloat(initdatalist.get(position).getIdx())>=50&&Float.parseFloat(initdatalist.get(position).getIdx())<=70){
            holder.Idx.setTextColor(mContext.getResources().getColor(R.color.colorYellow));
        }else {
            holder.Idx.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }

       holder.Idx.setText(""+initdatalist.get(position).getIdx()+"%");

        return view;
    }

    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault()).trim();
        initdatalist.clear();
        if (searchText.length() == 0) {
            initdatalist.addAll(arraylist);
        }
        else
        {
            for (Data_IFF_Ex a : arraylist)
            {
                if (a.getSellername().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}

