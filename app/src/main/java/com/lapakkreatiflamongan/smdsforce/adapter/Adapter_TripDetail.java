package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_TripDetail extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_ActiveTrip> initdatalist = null;
    private ArrayList<Data_ActiveTrip> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_TripDetail(Context context, List<Data_ActiveTrip> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_ActiveTrip>();
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
    public Data_ActiveTrip getItem(int position) {
        return initdatalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        Adapter_TripDetail.ViewHolder holder;
        if (view == null) {
            holder = new Adapter_TripDetail.ViewHolder();
            view = inflater.inflate(R.layout.l_trip, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LTrip_Id);
            holder.Address = (TextView) view.findViewById(R.id.LTrip_Time);
            holder.Channel = (TextView) view.findViewById(R.id.LTrip_Duration);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getDuration());
        holder.Address.setText(initdatalist.get(position).getLongitude()+", "+initdatalist.get(position).getLatitude());
        holder.Channel.setText(initdatalist.get(position).getGeoreverse());

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
            for (Data_ActiveTrip a : arraylist)
            {
                if (a.getId().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}
