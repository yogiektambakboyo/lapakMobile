package com.lapakkreatiflamongan.smdsforce.adapter;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreMaster;

/**
 * Created by IT-SUPERMASTER on 02/01/2016.
 */
public class Adapter_StoreMaster  extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Data_StoreMaster> initdatalist = null;
    private ArrayList<Data_StoreMaster> arraylist;

    DecimalFormatSymbols symbol;
    DecimalFormat formatter;


    public Adapter_StoreMaster(Context context, List<Data_StoreMaster> initdatalist) {
        mContext = context;
        this.initdatalist = initdatalist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Data_StoreMaster>();
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
    public Data_StoreMaster getItem(int position) {
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
            view = inflater.inflate(R.layout.l_storemaster, null);
            // Locate the TextViews in listview_item.xml
            holder.Name = (TextView) view.findViewById(R.id.LTrip_Id);
            holder.Address = (TextView) view.findViewById(R.id.LTrip_Time);
            holder.Channel = (TextView) view.findViewById(R.id.LTrip_Duration);
            holder.Img = (ImageView) view.findViewById(R.id.LStoreImage_Img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Name.setText(initdatalist.get(position).getStorename());
        holder.Address.setText(initdatalist.get(position).getAddress());
        holder.Channel.setText(initdatalist.get(position).getChanneldesc());

        if (initdatalist.get(position).getIsvalidated().equals("2")){
            Picasso.get().load(R.drawable.sfa_pelanggan_verified).into(holder.Img);
        }else if (initdatalist.get(position).getIsvalidated().equals("1")){
            Picasso.get().load(R.drawable.sfa_pelanggan_onverified).into(holder.Img);
        }else if(initdatalist.get(position).getNetizenid().length()<12){
            Picasso.get().load(R.drawable.sfa_netizencard).into(holder.Img);
        }
        else {
            Picasso.get().load(R.drawable.sfa_pelanggan).into(holder.Img);
        }

        holder.Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada).duration(300).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ShowInfo(initdatalist.get(position));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(holder.Img);
            }
        });
        return view;
    }

    public void ShowInfo(Data_StoreMaster d) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.d_storeinfo);

        final LinearLayout LyProfile = (LinearLayout) dialog.findViewById(R.id.Planning_InfoLyProfile);
        LyProfile.setVisibility(View.VISIBLE);


        final TextView TxtNama = (TextView) dialog.findViewById(R.id.Planning_InfoNama);
        TxtNama.setText(d.getStorename());

        final TextView TxtKode = (TextView) dialog.findViewById(R.id.Planning_InfoKode);
        TxtKode.setText(""+d.getStorecode());

        final TextView TxtKTP = (TextView) dialog.findViewById(R.id.Planning_InfoKTP);
        TxtKTP.setText(d.getNetizenid());

        final TextView TxtCity = (TextView) dialog.findViewById(R.id.Planning_InfoCity);
        TxtCity.setText(d.getCity());

        final TextView TxtFJP = (TextView) dialog.findViewById(R.id.Planning_InfoFJP);
        TxtFJP.setText(d.getFjp());

        final TextView TxtAlamat1 = (TextView) dialog.findViewById(R.id.Planning_InfoAlamat1);
        TxtAlamat1.setText(d.getAddress());

        final TextView TxtNoTelp = (TextView) dialog.findViewById(R.id.Planning_InfoNoTelp);
        TxtNoTelp.setText(""+d.getPhoneno());

        final TextView TxtNoWa = (TextView) dialog.findViewById(R.id.Planning_InfoNoWA);
        TxtNoWa.setText(""+d.getWhatsappno());
        if (d.getWhatsappno().equals("0")){
            TxtNoWa.setText("-");
        }

        final TextView TxtChannel = (TextView) dialog.findViewById(R.id.Planning_InfoChannel);
        TxtChannel.setText(d.getChanneldesc());

        ImageView MapsIcon = dialog.findViewById(R.id.Planning_InfoImg);

        MapsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).duration(300).withListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        //String uri = "geo:"+d.getLatitude()+","+d.getLongitude();
                        String nav = "google.navigation:q="+d.getLatitude()+","+d.getLongitude();
                        Uri gmmIntentUri = Uri.parse(nav);
                        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        intent.setPackage("com.google.android.apps.maps");
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animation) {

                    }
                }).playOn(MapsIcon);
            }
        });
        dialog.show();

    }



    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault()).trim();
        initdatalist.clear();
        if (searchText.length() == 0) {
            initdatalist.addAll(arraylist);
        }
        else
        {
            for (Data_StoreMaster a : arraylist)
            {
                if (a.getStorename().toLowerCase(Locale.getDefault()).contains(searchText)){
                    initdatalist.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }


}
