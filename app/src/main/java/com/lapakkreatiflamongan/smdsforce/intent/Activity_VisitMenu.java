package com.lapakkreatiflamongan.smdsforce.intent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_VisitActive;
import com.lapakkreatiflamongan.smdsforce.schema.Data_VisitActive;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class Activity_VisitMenu extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    private String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    CardView Cv_Photo,Cv_Order,Cv_CloseCall;
    Dialog dialog;
    List<Data_VisitActive> dataVisitActive;

    String procentTimeGone = "0";

    Retrofit retrofit;
    API_SFA myAPi;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_visitmenu);

        BASE_URL = appConfig.getBASE_URL();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        dataVisitActive = new ArrayList<Data_VisitActive>();

        getSupportActionBar().setTitle(""+getPref(appConfig.getTAG_CUSTOMERNAME()));

        Cv_Photo = findViewById(R.id.VisitMenu_PhotoCapture);
        Cv_Order = findViewById(R.id.VisitMenu_Order);
        Cv_CloseCall = findViewById(R.id.VisitMenu_CallAnalys);

        Cv_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataVisitActive != null) {
                    if (dataVisitActive.size() <= 0) {
                        Toast.makeText(Activity_VisitMenu.this, "Silahkan melakukan Foto Kunjungan Toko dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(Activity_VisitMenu.this,Activity_Order.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });

        Cv_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataVisitActive != null) {
                    if (dataVisitActive.size() > 0) {
                        Toast.makeText(Activity_VisitMenu.this, "Anda sudah melakukan foto kunjungan untuk toko ini, Silahkan lanjutkan proses selanjutnya", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(Activity_VisitMenu.this,Activity_NewVisit.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(Activity_VisitMenu.this,Activity_NewVisit.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        Cv_CloseCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_VisitMenu.this,Activity_VisitClose.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        Intent intent = getIntent();
        procentTimeGone = intent.getStringExtra("data");

        if (procentTimeGone==null){
            procentTimeGone = "0";
        }

        dialog = new Dialog(Activity_VisitMenu.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

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

    public void getData(){
        dialog.show();
        Call<Col_VisitActive> callData = myAPi.getVisitActive(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_CUSTOMERID()));
        callData.enqueue(new Callback<Col_VisitActive>() {
            @Override
            public void onResponse(Call<Col_VisitActive> call, Response<Col_VisitActive> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    Col_VisitActive col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_VisitMenu.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_VisitActive> data = col.getData();
                            dataVisitActive = data;
                        }else{
                        }
                    }
                }else{
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_VisitMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_VisitMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_VisitMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_VisitMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_VisitActive> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_VisitMenu.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, "Silahkan selesaikan kunjungan lewat menu tutup kunjungan", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}