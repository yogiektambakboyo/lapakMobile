package com.lapakkreatiflamongan.smdsforce.intent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Product;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Col_Product;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Product;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_Order extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;
    Dialog dialog;
    SpinKitView loader;

    Retrofit retrofit;
    API_SFA myAPi;

    ListView listViewTrip;
    Adapter_Product adapter;
    EditText InputSearch;
    TextView TxtInfo;
    String Order_No = "XXX";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_order);

        BASE_URL = appConfig.getBASE_URL();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);


        getSupportActionBar().setTitle("Pesanan");

        listViewTrip  = findViewById(R.id.Order_List);
        loader          = findViewById(R.id.Order_Loading);
        InputSearch     = findViewById(R.id.Order_Search);
        TxtInfo         = findViewById(R.id.Order_Total);

        Order_No = getTodaySecond()+"-"+getPref(appConfig.getTAG_CUSTOMERID());

        loader.setVisibility(View.VISIBLE);

        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        dialog = new Dialog(Activity_Order.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

        FloatingActionButton fab = findViewById(R.id.Order_FabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getTotalSKU()<=0){
                    Toast.makeText(Activity_Order.this, "Silahkan masukkan minimal 1 barang yang akan dibeli", Toast.LENGTH_SHORT).show();
                }else{
                    setData();
                }
            }
        });

    }

    


    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public int getPrefInt(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        int Value= SettingPref.getInt(KEY, 0);
        return  Value;
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getTodayMiliSecond() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getTodaySecond() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void getData(){
        dialog.show();
        Call<Col_Product> callData = myAPi.getProductOrder(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_CUSTOMERID()));
        callData.enqueue(new Callback<Col_Product>() {
            @Override
            public void onResponse(Call<Col_Product> call, Response<Col_Product> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_Product col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_Order.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_Product> data = col.getData();
                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setCustomers_id(getPref(appConfig.getTAG_CUSTOMERID()));
                                data.get(i).setSales_id(getPref(appConfig.getTAG_SPVCODE()));
                                data.get(i).setOrder_no(Order_No);
                                data.get(i).setTotal(""+Integer.parseInt(data.get(i).getPrice())*Integer.parseInt(data.get(i).getQty()));
                            }

                            adapter = new Adapter_Product(getApplicationContext(),data);
                            listViewTrip.setAdapter(adapter);

                            adapter.setTotal();
                            adapter.notifyDataSetChanged();
                            TxtInfo.setText("Total : "+adapter.getTotalSKU()+" SKU/ Rp. "+formatter.format(adapter.getTotal()));

                            listViewTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Dialog dialog = new Dialog(Activity_Order.this);
                                    dialog.setContentView(R.layout.d_order);

                                    adapter.getItem(i).getId();
                                    adapter.getItem(i).getSeq();
                                    adapter.getItem(i).getSales_id();
                                    adapter.getItem(i).getCustomers_id();
                                    adapter.getItem(i).getTotal();
                                    adapter.getItem(i).getPrice();
                                    adapter.getItem(i).getOrder_no();

                                   /* Toast.makeText(Activity_Order.this, "Product "+adapter.getItem(i).getId(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "Seq "+adapter.getItem(i).getSeq(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "Sales "+adapter.getItem(i).getSales_id(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "Customer "+adapter.getItem(i).getCustomers_id(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "total "+adapter.getItem(i).getTotal(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "Price "+adapter.getItem(i).getPrice(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Activity_Order.this, "Order No "+adapter.getItem(i).getOrder_no(), Toast.LENGTH_SHORT).show();
*/
                                    TextView TxtName,TxtPrice,TxtTotal;
                                    EditText InputQty;
                                    Button BtnCancel,BtnSave;

                                    TxtName = dialog.findViewById(R.id.LOrder_ProductName);
                                    TxtPrice = dialog.findViewById(R.id.LOrder_ProductPrice);
                                    TxtTotal = dialog.findViewById(R.id.LOrder_ProductTotal);
                                    InputQty = dialog.findViewById(R.id.LOrder_Qty);
                                    BtnCancel = dialog.findViewById(R.id.LOrder_Cancel);
                                    BtnSave = dialog.findViewById(R.id.LOrder_Save);

                                    BtnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    int qty = 0;

                                    if (adapter.getItem(i).getQty().length()<=0){
                                        qty = 0;
                                    }else{
                                        qty = Integer.parseInt(adapter.getItem(i).getQty());
                                    }
                                    TxtName.setText(adapter.getItem(i).getProduct_name());
                                    TxtPrice.setText("Harga : "+formatter.format(Float.parseFloat(adapter.getItem(i).getPrice())));
                                    TxtTotal.setText("Total : "+formatter.format(qty*(Integer.parseInt(adapter.getItem(i).getPrice()))));
                                    InputQty.setText(""+qty);

                                    InputQty.setSelectAllOnFocus(true);

                                    InputQty.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable editable) {
                                            if (editable.toString().trim().length()<=0){
                                                TxtTotal.setText("Total : 0");
                                            }else{
                                                TxtTotal.setText("Total : "+formatter.format(Integer.parseInt(editable.toString().trim())*(Integer.parseInt(adapter.getItem(i).getPrice()))));
                                            }
                                        }
                                    });

                                    BtnSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (InputQty.getText().toString().trim().length()<=0){
                                                InputQty.setText("0");
                                            }
                                            adapter.getItem(i).setQty(""+InputQty.getText().toString().trim());
                                            adapter.notifyDataSetChanged();
                                            adapter.getItem(i).setCustomers_id(getPref(appConfig.getTAG_CUSTOMERID()));
                                            adapter.getItem(i).setSales_id(getPref(appConfig.getTAG_SPVCODE()));
                                            adapter.notifyDataSetChanged();
                                            adapter.getItem(i).setOrder_no(Order_No);
                                            adapter.getItem(i).setSeq(getTodayMiliSecond());
                                            adapter.setTotal();
                                            adapter.notifyDataSetChanged();
                                            TxtInfo.setText("Total : "+adapter.getTotalSKU()+" SKU/ Rp. "+formatter.format(adapter.getTotal()));
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();


                                }
                            });

                            if (data.size()>0){
                                InputSearch.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        adapter.filter(charSequence.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });
                            }
                        }else{
                        }
                    }
                }else{
                    loader.setVisibility(View.GONE);
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_Product> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Order.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(){
        dialog.show();
        Call<Col_ActiveTrip> callData = myAPi.insertOrder(adapter.getData());
        callData.enqueue(new Callback<Col_ActiveTrip>() {
            @Override
            public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_ActiveTrip res = response.body();
                    if (res == null){
                        Toast.makeText(Activity_Order.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (res.getStatus().equals("1")){
                            Intent intent1 = new Intent(getApplicationContext(),Activity_Checkout.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
                        }else{
                            Toast.makeText(Activity_Order.this, "Ambil Data Gagal : Respon "+res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    loader.setVisibility(View.GONE);
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Order.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Order.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}