package com.lapakkreatiflamongan.smdsforce.intent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Product;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
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

public class Activity_Checkout extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    private String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    Dialog dialog;
    SpinKitView loader;
    String OrderNo = "";

    ListView listViewTrip;
    Adapter_Product adapter;
    EditText InputNotes,InputDeliveryDate;
    TextView TxtInfo;
    final Calendar myCalendar= Calendar.getInstance();

    Retrofit retrofit;
    API_SFA myAPi;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_checkout);

        BASE_URL = appConfig.getBASE_URL();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        getSupportActionBar().setTitle("Checkout Pesanan");

        listViewTrip  = findViewById(R.id.CheckOut_List);
        loader          = findViewById(R.id.CheckOut_Loading);
        TxtInfo         = findViewById(R.id.CheckOut_Total);
        InputDeliveryDate       = findViewById(R.id.CheckOut_DeliveryDate);
        InputNotes       = findViewById(R.id.CheckOut_Notes);

        InputDeliveryDate.setText(getToday());
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="YYYY-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                InputDeliveryDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        InputDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Activity_Checkout.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        loader.setVisibility(View.VISIBLE);

        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);


        dialog = new Dialog(Activity_Checkout.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

        FloatingActionButton fab = findViewById(R.id.CheckOut_FabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public void getData(){
        dialog.show();
        Call<Col_Product> callData = myAPi.getProductOrderCheckout(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_CUSTOMERID()));
        callData.enqueue(new Callback<Col_Product>() {
            @Override
            public void onResponse(Call<Col_Product> call, Response<Col_Product> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_Product col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_Checkout.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_Product> data = col.getData();
                            adapter = new Adapter_Product(getApplicationContext(),data);
                            listViewTrip.setAdapter(adapter);
                            OrderNo = data.get(0).getOrder_no();
                            adapter.setTotal();
                            adapter.notifyDataSetChanged();
                            TxtInfo.setText(adapter.getTotalSKU()+" SKU/ Rp. "+formatter.format(adapter.getTotal()));
                        }else{
                        }
                    }
                }else{
                    loader.setVisibility(View.GONE);
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_Product> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Checkout.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(){
        dialog.show();
        Call<String> callData = myAPi.confirmOrder(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_CUSTOMERID()),OrderNo,InputNotes.getText().toString(),InputDeliveryDate.getText().toString().trim());
        callData.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    String res = response.body();
                    if (res == null){
                        Toast.makeText(Activity_Checkout.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Checkout.this);
                        builder.setTitle("Konfirmasi Pesanan");
                        builder.setCancelable(false);

                        LinearLayout layout = new LinearLayout(Activity_Checkout.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(20, 15, 30, 15);

                        final TextView TxtLblNama = new TextView(Activity_Checkout.this);
                        TxtLblNama.setText("Pesanan atas nama "+getPref(appConfig.getTAG_CUSTOMERNAME())+" berhasil disimpan dengan No. "+OrderNo);
                        TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                        layout.addView(TxtLblNama, params);

                        builder.setView(layout);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               Intent intent = new Intent(Activity_Checkout.this, Activity_VisitMenu.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               startActivity(intent);
                            }
                        });


                        builder.show();
                    }
                }else{
                    loader.setVisibility(View.GONE);
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Checkout.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Checkout.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}