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
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Registration;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Trip;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_TripDetail;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreReg;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreReg;
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

public class Activity_Register extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    private String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    Retrofit retrofit;
    API_SFA myAPi;

    Dialog dialog;
    SpinKitView loader;

    String procentTimeGone = "0";

    ListView listViewTrip;
    Adapter_Registration adapter;
    EditText InputSearch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_register);

        BASE_URL = appConfig.getBASE_URL();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        getSupportActionBar().setTitle("Pendaftaran Toko Baru ");

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
        procentTimeGone = intent.getStringExtra("data");

        if (procentTimeGone==null){
            procentTimeGone = "0";
        }

        dialog = new Dialog(Activity_Register.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

        FloatingActionButton fab = findViewById(R.id.Reg_FabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),Activity_RegForm.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
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

    public void getData(){
        dialog.show();
        Call<Col_StoreReg> callData = myAPi.getStoreRegAll(getPref(appConfig.getTAG_SPVCODE()));
        callData.enqueue(new Callback<Col_StoreReg>() {
            @Override
            public void onResponse(Call<Col_StoreReg> call, Response<Col_StoreReg> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_StoreReg col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_Register.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_StoreReg> data = col.getData();
                            adapter = new Adapter_Registration(getApplicationContext(),data);
                            listViewTrip.setAdapter(adapter);

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
                            Toast.makeText(Activity_Register.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Register.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Register.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Register.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_StoreReg> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Register.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}