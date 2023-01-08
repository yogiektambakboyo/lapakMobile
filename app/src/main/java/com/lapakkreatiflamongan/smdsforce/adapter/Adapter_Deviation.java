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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_Numbered;

/**
 * Created by IT-SUPERMASTER on 02/01/2016.
 */
public class Adapter_Deviation  extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_Value_Detail_Numbered> initdatalist = null;
    private ArrayList<Data_Value_Detail_Numbered> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_Deviation(Context context, List<Data_Value_Detail_Numbered> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_Value_Detail_Numbered>();
        this.arraylist.addAll(initdatalist);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

    }

    public class ViewHolder {
        TextView Name,Address,Channel,Procent;
        ImageView Img;
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_Value_Detail getItem(int position) {
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
            view = inflater.inflate(R.layout.l_store, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LStore_Nama);
            holder.Address = (TextView) view.findViewById(R.id.LStore_Alamat);
            holder.Channel = (TextView) view.findViewById(R.id.LStore_Channel);
            holder.Procent = (TextView) view.findViewById(R.id.LStore_Procent);
            holder.Img = (ImageView) view.findViewById(R.id.LStoreImage_Img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getName());
        holder.Address.setText(""+formatter.format(Float.parseFloat(initdatalist.get(position).getActual()))+"/ "+formatter.format(Float.parseFloat(initdatalist.get(position).getTarget())));
        holder.Channel.setText(initdatalist.get(position).getProcent());
        holder.Procent.setText(initdatalist.get(position).getProcent()+"%");


        if (((Float.parseFloat(initdatalist.get(position).getProcent()))>70f)){
            Picasso.get().load(R.drawable.circle_red).into(holder.Img);
            holder.Procent.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }else if (((Float.parseFloat(initdatalist.get(position).getProcent()))>=50f)&&((Float.parseFloat(initdatalist.get(position).getProcent()))<=70f)){
            Picasso.get().load(R.drawable.circle_yellow).into(holder.Img);
            holder.Procent.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }else {
            Picasso.get().load(R.drawable.circle_green).into(holder.Img);
            holder.Procent.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }

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
            for (Data_Value_Detail_Numbered a : arraylist)
            {
                if (a.getName().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}

