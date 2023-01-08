package com.lapakkreatiflamongan.smdsforce.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Position;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_Numbered;

public class Adapter_PositionMaps  extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_Position> initdatalist = null;
    private ArrayList<Data_Position> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;

    private Float[] arrColor = new Float[25];

    public Adapter_PositionMaps(Context context, List<Data_Position> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_Position>();
        this.arraylist.addAll(initdatalist);
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        arrColor[0] = BitmapDescriptorFactory.HUE_AZURE;
        arrColor[1] = BitmapDescriptorFactory.HUE_BLUE;
        arrColor[2] = BitmapDescriptorFactory.HUE_CYAN;
        arrColor[3] = BitmapDescriptorFactory.HUE_GREEN;
        arrColor[4] = BitmapDescriptorFactory.HUE_MAGENTA;
        arrColor[5] = BitmapDescriptorFactory.HUE_ORANGE;
        arrColor[6] = BitmapDescriptorFactory.HUE_RED;
        arrColor[7] = BitmapDescriptorFactory.HUE_ROSE;
        arrColor[8] = BitmapDescriptorFactory.HUE_VIOLET;
        arrColor[9] = BitmapDescriptorFactory.HUE_YELLOW;
        arrColor[10] = 15f;
        arrColor[11] = 25f;
        arrColor[12] = 35f;
        arrColor[13] = 45f;
        arrColor[14] = 55f;
        arrColor[15] = 65f;
        arrColor[16] = 75f;
        arrColor[17] = 85f;
        arrColor[18] = 95f;
        arrColor[20] = 105f;
        arrColor[21] = 115f;
        arrColor[22] = 125f;
        arrColor[23] = 135f;
        arrColor[24] = 145f;
    }

    public class ViewHolder {
        TextView Name,Address,Channel,Idx;
    }

    @Override
    public int getCount() {
        return initdatalist.size();
    }

    @Override
    public Data_Position getItem(int position) {
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
            view = inflater.inflate(R.layout.l_positions_maps, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LPosition_Nama);
            holder.Address = (TextView) view.findViewById(R.id.LPosition_Alamat);
            holder.Channel = (TextView) view.findViewById(R.id.LPosition_Channel);
            holder.Idx = (TextView) view.findViewById(R.id.LPosition_Idx);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getSellername());
        holder.Address.setText(""+initdatalist.get(position).getSellercode());
        holder.Idx.setText(""+(Integer.parseInt(initdatalist.get(position).getCreatedate())+1));

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
            for (Data_Position a : arraylist)
            {
                if (a.getSellername().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}

