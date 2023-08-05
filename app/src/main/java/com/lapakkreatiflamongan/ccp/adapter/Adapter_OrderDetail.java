package com.lapakkreatiflamongan.ccp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lapakkreatiflamongan.ccp.R;
import com.lapakkreatiflamongan.ccp.schema.Data_OrderDetail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_OrderDetail extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_OrderDetail> initdatalist = null;
    private ArrayList<Data_OrderDetail> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_OrderDetail(Context context, List<Data_OrderDetail> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_OrderDetail>();
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
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_OrderDetail getItem(int position) {
        return initdatalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        Adapter_OrderDetail.ViewHolder holder;
        if (view == null) {
            holder = new Adapter_OrderDetail.ViewHolder();
            view = inflater.inflate(R.layout.l_trip, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LTrip_Name);
            holder.Address = (TextView) view.findViewById(R.id.LTrip_Address);
            holder.Channel = (TextView) view.findViewById(R.id.LTrip_Channel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getProduct_name());
        holder.Address.setText(initdatalist.get(position).getQty()+"x"+formatter.format(Integer.parseInt(initdatalist.get(position).getPrice())));
        holder.Channel.setText("");

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
            for (Data_OrderDetail a : arraylist)
            {
                if (a.getCustomer_name().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}
