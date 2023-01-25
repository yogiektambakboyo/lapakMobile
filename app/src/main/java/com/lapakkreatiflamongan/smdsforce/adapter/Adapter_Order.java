package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_OrderMaster;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreVisit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_Order extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_OrderMaster> initdatalist = null;
    private ArrayList<Data_OrderMaster> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_Order(Context context, List<Data_OrderMaster> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_OrderMaster>();
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
    public Data_OrderMaster getItem(int position) {
        return initdatalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        Adapter_Order.ViewHolder holder;
        if (view == null) {
            holder = new Adapter_Order.ViewHolder();
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
        holder.Name.setText(initdatalist.get(position).getCustomer_name());
        holder.Address.setText(initdatalist.get(position).getOrder_no());
        holder.Channel.setText(formatter.format(Float.parseFloat(initdatalist.get(position).getTotal())));

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
            for (Data_OrderMaster a : arraylist)
            {
                if (a.getCustomer_name().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}
