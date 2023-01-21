package com.lapakkreatiflamongan.smdsforce.intent;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Trip;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_TripDetail;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreMaster;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreMaster;
import com.lapakkreatiflamongan.smdsforce.utils.UtilsHelper;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_Trip extends AppCompatActivity {
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
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
    Adapter_Trip adapterTrip;
    EditText InputSearch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_trip);

        getSupportActionBar().setTitle("Daftar Trip "+getToday());

        listViewTrip  = findViewById(R.id.Trip_List);
        loader          = findViewById(R.id.Trip_Loading);
        InputSearch     = findViewById(R.id.Trip_Search);

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

        dialog = new Dialog(Activity_Trip.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getActiveTripAll();

        FloatingActionButton fab = findViewById(R.id.Trip_FabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),Activity_NewTrip.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });
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

    public void getActiveTripAll(){
        dialog.show();
        Call<Col_ActiveTrip> callData = myAPi.getActiveTripAll(getPref(TAG_SPVCODE));
        callData.enqueue(new Callback<Col_ActiveTrip>() {
            @Override
            public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    loader.setVisibility(View.GONE);
                    Col_ActiveTrip col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_ActiveTrip> data = col.getData();
                            adapterTrip = new Adapter_Trip(getApplicationContext(),data);
                            listViewTrip.setAdapter(adapterTrip);

                            listViewTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Dialog dialog1 = new Dialog(Activity_Trip.this);
                                    dialog1.setContentView(R.layout.d_tripdetail);
                                    dialog1.setCancelable(true);

                                    TextView txtLabel = dialog1.findViewById(R.id.D_TripDetailLabel);
                                    ListView listView = dialog1.findViewById(R.id.D_TripDetailList);
                                    Button btnClose = dialog1.findViewById(R.id.D_TripDetailBtn);
                                    btnClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog1.dismiss();
                                        }
                                    });

                                    txtLabel.setText("Detail Trip #"+adapterTrip.getItem(i).getId());

                                    Call<Col_ActiveTrip> callData = myAPi.getActiveTripDetail(getPref(TAG_SPVCODE),adapterTrip.getItem(i).getId());
                                    callData.enqueue(new Callback<Col_ActiveTrip>() {
                                        @Override
                                        public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                                            if (response.isSuccessful()){

                                                dialog.dismiss();
                                                loader.setVisibility(View.GONE);
                                                Col_ActiveTrip col = response.body();
                                                if (col == null){
                                                    Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (col.getStatus().equals("1")){
                                                        List<Data_ActiveTrip> data = col.getData();
                                                        Adapter_TripDetail adapterTripDetail = new Adapter_TripDetail(getApplicationContext(),data);
                                                        listView.setAdapter(adapterTripDetail);
                                                    }else{
                                                    }
                                                }
                                            }else{
                                                loader.setVisibility(View.GONE);
                                                dialog.dismiss();
                                                switch (response.code()) {
                                                    case 404:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 408:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 500:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 504:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 502:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                                                        break;
                                                    default:
                                                        Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                            dialog.dismiss();
                                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    dialog1.show();
                                }
                            });

                            if (data.size()>0){
                                InputSearch.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        adapterTrip.filter(charSequence.toString());
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
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

}