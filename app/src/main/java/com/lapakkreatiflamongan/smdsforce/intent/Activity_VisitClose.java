package com.lapakkreatiflamongan.smdsforce.intent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_VisitActive;
import com.lapakkreatiflamongan.smdsforce.schema.Data_VisitActive;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_VisitClose extends AppCompatActivity {
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";
    private String VERSION_APK = "0.0.6";
    private final String TAG_CUSTOMERID = "customer_id";
    private final String TAG_CUSTOMERNAME = "customer_name";
    private String BASE_URL = "http://kakikupos.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    String SalesName = "", SalesCode = "";
    String weekstring = "wk1";
    private final int REQUEST_CODE = 888;
    private final String TAG_LASTLOGIN = "lastlogin";
    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_504 = "504 Gateway Time Out";
    private final String ERROR_408 = "408 Request Time Out";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";
    private final String TAG_SESSION = "session";

    Dialog dialog;
    List<Data_VisitActive> dataVisitActive;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);
    Button BtnClose,BtnCancel;
    TextView TxtSales;
    long stopTimer = 0;
    private Chronometer mChronometer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_visitclose);

        BtnClose = findViewById(R.id.Close_BtnClose);
        BtnCancel = findViewById(R.id.Close_BtnCancel);
        TxtSales = findViewById(R.id.Close_TxtSales);
        TxtSales.setText("Rp. 0");
        mChronometer = (Chronometer) findViewById(R.id.Close_Chrono);

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_VisitClose.this, Activity_VisitMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });

        getSupportActionBar().setTitle("Tutup Kunjungan");

        dataVisitActive = new ArrayList<Data_VisitActive>();


        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        dialog = new Dialog(Activity_VisitClose.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

    }


        public long getDateDiffMili(String Date1, String Date2) {
            //18-12-2015 18:04:46
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date date1 = null;
            Date date2 = null;
            long a =0;
            try {
                date1 = format.parse(Date1);
                date2 = format.parse(Date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diffInMillies = date2.getTime() - date1.getTime();
            return diffInMillies;
        }


    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public int getPrefInt(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        int Value= SettingPref.getInt(KEY, 0);
        return  Value;
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }


    public String getTodaySecond() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void getData(){
        dialog.show();
        Call<Col_VisitActive> callData = myAPi.getVisitActive(getPref(TAG_SPVCODE),getPref(TAG_CUSTOMERID));
        callData.enqueue(new Callback<Col_VisitActive>() {
            @Override
            public void onResponse(Call<Col_VisitActive> call, Response<Col_VisitActive> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    Col_VisitActive col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_VisitActive> data = col.getData();
                            dataVisitActive = data;
                        }else{
                        }

                        String Today = getTodaySecond();
                        String isChekcout = "0";
                        if (dataVisitActive != null){
                            if (dataVisitActive.size()>0){
                                Today = dataVisitActive.get(0).getTime_start()+":00";
                                isChekcout = dataVisitActive.get(0).getIs_checkout();
                                TxtSales.setText("Rp. "+formatter.format(Float.parseFloat(dataVisitActive.get(0).getSales_vol())));
                            }
                        }

                        String d = Today;

                        stopTimer = 0;
                        stopTimer = getDateDiffMili(d,getTodaySecond());

                        mChronometer.setBase(SystemClock.elapsedRealtime() - stopTimer);

                        if (!isChekcout.equals("1")){
                            mChronometer.start();
                        }else {
                            mChronometer.setText("00:00");
                            Toast.makeText(Activity_VisitClose.this, "Durasi kunjungan yang tersimpan hanya kunjungan yang pertama!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_VisitActive> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(){
        dialog.show();
        Call<String> callData = myAPi.updateVisitActive(getPref(TAG_SPVCODE),getPref(TAG_CUSTOMERID),"1");
        callData.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    String col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(Activity_VisitClose.this,Activity_MainMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else{
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_VisitClose.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }
}