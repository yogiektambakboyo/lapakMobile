package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_Product extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_Product> initdatalist = null;
    private ArrayList<Data_Product> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;

    public Adapter_Product(Context context, List<Data_Product> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_Product>();
        this.arraylist.addAll(initdatalist);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

    }

    public class ViewHolder {
        TextView Name,Price,Qty;
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_Product getItem(int position) {
        return initdatalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        Adapter_Product.ViewHolder holder;
        if (view == null) {
            holder = new Adapter_Product.ViewHolder();
            view = inflater.inflate(R.layout.l_order, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LProduct_Name);
            holder.Price = (TextView) view.findViewById(R.id.LProduct_Price);
            holder.Qty = (TextView) view.findViewById(R.id.LProduct_Qty);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getProduct_name());
        holder.Price.setText("@"+formatter.format(Integer.parseInt(initdatalist.get(position).getPrice()))+" x "+initdatalist.get(position).getQty()+" = "+formatter.format(Integer.parseInt(initdatalist.get(position).getQty())*Integer.parseInt(initdatalist.get(position).getPrice())));
        holder.Qty.setText(""+initdatalist.get(position).getQty());

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
            for (Data_Product a : arraylist)
            {
                if (a.getProduct_name().toLowerCase(Locale.getDefault()).contains(searchText)||a.getBrand_name().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


    public void setTotal() {
        for (Data_Product a : initdatalist)
        {
           a.setTotal(""+(Integer.parseInt(a.getPrice())*Integer.parseInt(a.getQty())));
        }
        notifyDataSetChanged();
    }

    public int getTotal() {
        int result = 0;
        for (Data_Product a : initdatalist)
        {
            if (a.getTotal() == null) {
            }else {
                result = result + Integer.parseInt(a.getTotal());
            }
        }
        return result;
    }

    public int getTotalSKU() {
        int counter = 0;
        for (Data_Product a : initdatalist)
        {
            if (!a.getQty().toString().trim().equals("0")&&a.getQty().toString().trim().length()>0){
                counter++;
            }
        }
        notifyDataSetChanged();
        return counter;
    }

    public List<Data_Product> getData(){
        ArrayList<Data_Product> dataProducts = new ArrayList<Data_Product>();
        for (Data_Product a : initdatalist)
        {
            if (!a.getQty().toString().trim().equals("0")&&a.getQty().toString().trim().length()>0){
                dataProducts.add(a);
            }
        }

        return dataProducts;
    }

}
