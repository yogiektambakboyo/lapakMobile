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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreVisit;

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
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_CUSTOMERID = "customer_id";
    private final String TAG_CUSTOMERNAME = "customer_name";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";
    private String VERSION_APK = "0.0.3";
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
    SpinKitView loader;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);

    String procentTimeGone = "0";

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

        listViewTrip  = findViewById(R.id.Reg_List);
        loader          = findViewById(R.id.Reg_Loading);
        InputSearch     = findViewById(R.id.Reg_Search);

        loader.setVisibility(View.VISIBLE);

        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(1);

        Intent intent = getIntent();
        procentTimeGone = intent.getStringExtra("data");

        if (procentTimeGone==null){
            procentTimeGone = "0";
        }

        dialog = new Dialog(Activity_Visit.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();

    }


    public void setPrefCustomerID(String CustomerID, String CustomerName){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_CUSTOMERID,CustomerID);
        SettingPrefEditor.putString(TAG_CUSTOMERNAME,CustomerName);
        SettingPrefEditor.commit();
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

    public void getData(){
        dialog.show();
        Call<Col_StoreVisit> callData = myAPi.getStoreVisitToday(getPref(TAG_SPVCODE));
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
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_StoreVisit> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Visit.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

}