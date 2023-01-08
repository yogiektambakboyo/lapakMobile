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
import com.lapakkreatiflamongan.smdsforce.schema.Data_Store;

/**
 * Created by IT-SUPERMASTER on 02/01/2016.
 */
public class Adapter_Store  extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_Store> initdatalist = null;
    private ArrayList<Data_Store> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_Store(Context context, List<Data_Store> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_Store>();
        this.arraylist.addAll(initdatalist);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

    }

    public class ViewHolder {
        TextView Name,Address,Channel;
        ImageView Img;
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_Store getItem(int position) {
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
            //holder.Img = (ImageView) view.findViewById(R.id.LStore_Img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getStorename());
        holder.Address.setText(initdatalist.get(position).getAddress());
        holder.Channel.setText(initdatalist.get(position).getChanneldesc());

        if (!initdatalist.get(position).getLongitude().equals("0")){
            Picasso.get().load(R.drawable.sfa_pelanggan_done).into(holder.Img);
        }else{
            Picasso.get().load(R.drawable.sfa_pelanggan).into(holder.Img);
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
            for (Data_Store a : arraylist)
            {
                if (a.getStorename().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}
