package com.lapakkreatiflamongan.smdsforce.intent;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.github.ybq.android.spinkit.SpinKitView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.lapakkreatiflamongan.smdsforce.BuildConfig;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Sales_Spinner;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Time;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_IFF;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_Numbered;
import com.lapakkreatiflamongan.smdsforce.schema.Data_summaryMTD;
import com.lapakkreatiflamongan.smdsforce.utils.UtilsHelper;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_MainMenu extends AppCompatActivity {
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_GEOREVERSE = "georeverse";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";
    private final String TAG_WEEKNUMBER = "weekno";
    private String VERSION_APK = "0.0.2";

    private String BASE_URL = "http://kakikupos.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;
    String DownloadDate = "";
    TextView TxtWelcome;
    TextView TxtTransactionDate;

    String SalesName = "", SalesCode = "";
    String weekstring = "wk1";
    String trip_id = "0";

    private final int REQUEST_CODE = 888;
    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_504 = "504 Gateway Time Out";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_408 = "408 Request Time Out";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";
    private final String TAG_SESSION = "session";

    LinearLayout MainMenu_LyActiveTrip;
    TextView MainMenu_TxtActiveTripLabel,MainMenu_TxtActiveTripGeo;
    CardView MainMenu_CvTripNew,MainMenu_CvVisit,MainMenu_CvActiveTrip,MainMenu_CvRegister;
    SwipeRefreshLayout SWRefresh;
    Dialog dialog;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);
    UtilsHelper utilsHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_mainmenu);
        utilsHelper = new UtilsHelper(Activity_MainMenu.this,Activity_MainMenu.this);

        dialog = new Dialog(Activity_MainMenu.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        SWRefresh = (SwipeRefreshLayout) findViewById(R.id.sw_refresh);

        SWRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SWRefresh.setRefreshing(false);
                getWeekNo();
                getActiveTrip();
            }
        });

        MainMenu_LyActiveTrip = findViewById(R.id.MainMenu_LyActiveTrip);
        MainMenu_TxtActiveTripGeo = findViewById(R.id.MainMenu_TxtActiveTripGeo);
        MainMenu_TxtActiveTripLabel = findViewById(R.id.MainMenu_TxtActiveTripLabel);

        MainMenu_CvActiveTrip = findViewById(R.id.MainMenu_CvActiveTrip);
        MainMenu_CvRegister = findViewById(R.id.MainMenu_CvRegister);
        MainMenu_CvVisit = findViewById(R.id.MainMenu_CvVisit);
        MainMenu_CvTripNew = findViewById(R.id.MainMenu_CvTripNew);

        MainMenu_CvVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Activity_Visit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        MainMenu_CvTripNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Activity_Trip.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        MainMenu_CvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Activity_Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        MainMenu_CvActiveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDownloadMaster = new AlertDialog.Builder(Activity_MainMenu.this);
                alertDownloadMaster.setTitle("Konfirmasi Active Trip");

                LinearLayout layout = new LinearLayout(Activity_MainMenu.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 5, 30, 0);

                final TextView labelQuestion = new TextView(Activity_MainMenu.this);
                labelQuestion.setText("Trip aktif saat ini adalah "+MainMenu_TxtActiveTripLabel.getText().toString());

                layout.addView(labelQuestion,params);

                alertDownloadMaster.setView(layout);

                alertDownloadMaster.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDownloadMaster.setNegativeButton("Check Point", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.show();
                        SmartLocation.with(getApplicationContext()).location()
                                .oneFix()
                                .start(new OnLocationUpdatedListener() {
                                    @Override
                                    public void onLocationUpdated(Location location) {
                                        Log.e( "doWork: ","Started Realtime Location Updated" );
                                        if (location != null){
                                            Log.e( "doWork: ","Started Realtime Location Updated Firebase" );

                                            SmartLocation.with(getApplicationContext()).geocoding()
                                                    .reverse(location, new OnReverseGeocodingListener() {
                                                        @Override
                                                        public void onAddressResolved(Location location, List<Address> list) {
                                                            if (list.size()>0){
                                                                setPrefLocation(location.getLongitude()+"",location.getLatitude()+"",""+list.get(0).getAddressLine(0));
                                                            }else{
                                                                setPrefLocation(location.getLongitude()+"",location.getLatitude()+"","");
                                                            }


                                                            Call<Col_ActiveTrip> callData = myAPi.insertActiveTripDetail(getPref(TAG_SPVCODE),getPref(TAG_LONGITUDE),getPref(TAG_LATITUDE),trip_id,getPref(TAG_GEOREVERSE));
                                                            callData.enqueue(new Callback<Col_ActiveTrip>() {
                                                                @Override
                                                                public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                                                                    if (response.isSuccessful()){
                                                                        Col_ActiveTrip col = response.body();
                                                                        if (col == null){
                                                                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            if (col.getStatus().equals("1")){
                                                                                Toast.makeText(Activity_MainMenu.this, "Check Point Berhasil", Toast.LENGTH_SHORT).show();

                                                                                Intent in = new Intent(Activity_MainMenu.this, Activity_MainMenu.class);
                                                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(in);
                                                                            }else{
                                                                            }
                                                                        }
                                                                    }else{
                                                                        switch (response.code()) {
                                                                            case 404:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 408:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 500:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 504:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 502:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            default:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                                                    Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(Activity_MainMenu.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });

                alertDownloadMaster.setNeutralButton("Stop Trip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.show();
                        SmartLocation.with(getApplicationContext()).location()
                                .oneFix()
                                .start(new OnLocationUpdatedListener() {
                                    @Override
                                    public void onLocationUpdated(Location location) {
                                        Log.e( "doWork: ","Started Realtime Location Updated" );
                                        if (location != null){
                                            Log.e( "doWork: ","Started Realtime Location Updated Firebase" );

                                            SmartLocation.with(getApplicationContext()).geocoding()
                                                    .reverse(location, new OnReverseGeocodingListener() {
                                                        @Override
                                                        public void onAddressResolved(Location location, List<Address> list) {
                                                            if (list.size()>0){
                                                                setPrefLocation(location.getLongitude()+"",location.getLatitude()+"",""+list.get(0).getAddressLine(0));
                                                            }else{
                                                                setPrefLocation(location.getLongitude()+"",location.getLatitude()+"","");
                                                            }


                                                            Call<Col_ActiveTrip> callData = myAPi.insertStopActiveTrip(getPref(TAG_SPVCODE),getPref(TAG_LONGITUDE),getPref(TAG_LATITUDE),trip_id,getPref(TAG_GEOREVERSE));
                                                            callData.enqueue(new Callback<Col_ActiveTrip>() {
                                                                @Override
                                                                public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                                                                    if (response.isSuccessful()){
                                                                        Col_ActiveTrip col = response.body();
                                                                        if (col == null){
                                                                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            if (col.getStatus().equals("1")){
                                                                                Toast.makeText(Activity_MainMenu.this, "Stop Trip Berhasil", Toast.LENGTH_SHORT).show();
                                                                                Intent in = new Intent(Activity_MainMenu.this, Activity_MainMenu.class);
                                                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(in);

                                                                            }else{
                                                                                MainMenu_LyActiveTrip.setVisibility(View.GONE);
                                                                            }
                                                                        }
                                                                    }else{
                                                                        switch (response.code()) {
                                                                            case 404:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 408:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 500:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 504:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 502:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            default:
                                                                                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                                                    Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(Activity_MainMenu.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                alertDownloadMaster.show();
            }
        });

        // Initil Request
        getWeekNo();
        getActiveTrip();

        /*ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/exo_2.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());*/

        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);
        VERSION_APK = BuildConfig.VERSION_NAME;
        DownloadDate = getDate();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                final List<SubscriptionInfo> subscriptionInfos;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TxtWelcome = (TextView) findViewById(R.id.MainMenu_WelcomeMsg);
        TxtWelcome.setText("Selamat Datang "+getPref(TAG_SPVNAME));

        SalesName = getPref(TAG_SPVNAME);
        SalesCode = getPref(TAG_SPVCODE);

        getSupportActionBar().setTitle(" Dashboard");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.x_house);

        TxtTransactionDate = (TextView) findViewById(R.id.MainMenu_TglTransaksi);
        TxtTransactionDate.setText("Date : "+DownloadDate+" ("+weekstring+")");
    }

    public void getWeekNo(){
        Call<String> callWeekNo = myAPi.getWeekNo(getPref(TAG_SPVCODE),"30011988AA");
        callWeekNo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body().toString().contains("wk")){
                        setPrefWeekNo(response.body().toString());
                    }
                }else{
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getActiveTrip(){
        MainMenu_LyActiveTrip.setVisibility(View.GONE);
        Call<Col_ActiveTrip> callData = myAPi.getActiveTrip(getPref(TAG_SPVCODE));
        callData.enqueue(new Callback<Col_ActiveTrip>() {
            @Override
            public void onResponse(Call<Col_ActiveTrip> call, Response<Col_ActiveTrip> response) {
                if (response.isSuccessful()){
                    Col_ActiveTrip col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_ActiveTrip> data = col.getData();
                            MainMenu_LyActiveTrip.setVisibility(View.VISIBLE);
                            trip_id = data.get(0).getId();
                            MainMenu_TxtActiveTripGeo.setText(""+data.get(0).getGeoreverse());
                            MainMenu_TxtActiveTripLabel.setText("Trip #"+data.get(0).getId()+" Durasi ("+data.get(0).getDuration()+" Menit)");
                        }else{
                            MainMenu_LyActiveTrip.setVisibility(View.GONE);
                        }
                    }
                }else{
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean checkPref(String KEY){
        boolean a = false;
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        a = SettingPref.contains(KEY);
        return  a;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_logout:
                ShowDialogLogOut();
                break;
            case R.id.login_maps:
                callTracingLog("Show Maps");
                Intent intent = new Intent(Activity_MainMenu.this,Activity_Maps.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:break;
        }

        return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent in = new Intent(Activity_MainMenu.this,Activity_Login.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Apakah anda yakin akan keluar?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
    }

    public void ShowDialogAboutUs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tentang");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 15, 30, 15);

        final TextView TxtLblNama = new TextView(this);
        TxtLblNama.setText("SMD SForce v "+VERSION_APK);
        TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TxtLblNama.setTextColor(Color.BLACK);

        layout.addView(TxtLblNama, params);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.show();
    }


    public void ShowDialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Log Out");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 15, 30, 15);

        final TextView TxtLblNama = new TextView(this);
        TxtLblNama.setText("Apakah anda yakin akan keluar?");
        TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


        layout.addView(TxtLblNama, params);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in = new Intent(Activity_MainMenu.this,Activity_Login.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private String getDateTime(String format, int hari) {
        Calendar calendar = Calendar.getInstance();
        Date myDate = new Date();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, hari);
        myDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format, Locale.getDefault());
        return dateFormat.format(myDate);
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public long getDateDiff(String Date1, String Date2, TimeUnit timeUnit) {
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
        a = timeUnit.convert(diffInMillies, TimeUnit.SECONDS)/1000;
        return a;
    }

    public void setPrefLoginTime(String LoginTime){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_LOGINTIME,LoginTime);
        SettingPrefEditor.commit();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public String getGPSTime(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = sdf.format(d);
        return result;
    }

    public String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }


    public void setPrefWeekNo(String WeekNo){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_WEEKNUMBER,WeekNo);
        SettingPrefEditor.commit();
    }

    public void callTracingLog(String Desc) {
        Call<String> callLogout = myAPi.Tracing(getPref(TAG_SPVCODE).replace("/", ""), getPref(TAG_SESSION),Desc);
        callLogout.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                } else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPrefLocation(String Longitude,String Latitude,String Georeverse){
        SharedPreferences SettingPref = getApplicationContext().getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_LONGITUDE,Longitude);
        SettingPrefEditor.putString(TAG_LATITUDE,Latitude);
        SettingPrefEditor.putString(TAG_GEOREVERSE,Georeverse);
        SettingPrefEditor.commit();
    }
}
