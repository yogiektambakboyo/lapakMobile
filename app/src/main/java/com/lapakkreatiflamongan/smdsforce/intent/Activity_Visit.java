package com.lapakkreatiflamongan.smdsforce.intent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreVisit;
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

public class Activity_Visit extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    String VERSION_APK = "0.0.7";
    String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;
    
    Dialog dialog;
    SpinKitView loader;

    Retrofit retrofit;
    API_SFA myAPi;

    ListView listViewTrip;
    Adapter_StoreVisit adapter;
    EditText InputSearch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_visit);

        getSupportActionBar().setTitle("Kunjungan Toko");

        BASE_URL = appConfig.getBASE_URL();
        VERSION_APK = appConfig.getVERSION_APK();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        listViewTrip  = findViewById(R.id.Reg_List);
        loader          = findViewById(R.id.Reg_Loading);
        InputSearch     = findViewById(R.id.Reg_Search);

        loader.setVisibility(View.VISIBLE);


        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        Intent intent = getIntent();
        dialog = new Dialog(Activity_Visit.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

        int PERMISSION_ALL = 8;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.CHANGE_NETWORK_STATE
        };

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean isActiveGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{

            if (!isActiveGPS) {
                String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak menyalakan Lokasi GPS!!!";
                new AlertDialog.Builder(Activity_Visit.this)
                        .setTitle("Information")
                        .setMessage(Msg)
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Activity_Visit.this, Activity_MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("onCreate ", "hasPermissions: "+permission );
                    return false;
                }
            }
        }
        return true;
    }


    public void setPrefCustomerID(String CustomerID, String CustomerName){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_CUSTOMERID(),CustomerID);
        SettingPrefEditor.putString(appConfig.getTAG_CUSTOMERNAME(),CustomerName);
        SettingPrefEditor.commit();
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
        Call<Col_StoreVisit> callData = myAPi.getStoreVisitToday(getPref(appConfig.getTAG_SPVCODE()));
        callData.enqueue(new Callback<Col_StoreVisit>() {
            @Override
            public void onResponse(Call<Col_StoreVisit> call, Response<Col_StoreVisit> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_StoreVisit col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_StoreVisit> data = col.getData();
                            adapter = new Adapter_StoreVisit(getApplicationContext(),data);
                            listViewTrip.setAdapter(adapter);

                            listViewTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    setPrefCustomerID(adapter.getItem(i).getCustomer_id(),adapter.getItem(i).getCustomer_name());
                                    Intent intent = new Intent(Activity_Visit.this, Activity_VisitMenu.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
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
                            Toast.makeText(Activity_Visit.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Visit.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Visit.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Visit.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_StoreVisit> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}