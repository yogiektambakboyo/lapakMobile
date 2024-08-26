package com.lapakkreatiflamongan.smdsforce.intent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;
import com.lapakkreatiflamongan.smdsforce.utils.UtilsHelper;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_MainMenu extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    String VERSION_APK = "0.0.7";
    String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;
    String DownloadDate = "";
    TextView TxtWelcome;
    TextView TxtTransactionDate;

    String SalesName = "", SalesCode = "";
    String weekstring = "wk1";
    String trip_id = "0";

    LinearLayout MainMenu_LyActiveTrip;
    TextView MainMenu_TxtActiveTripLabel,MainMenu_TxtActiveTripID,MainMenu_TxtActiveTripGeo;
    CardView MainMenu_CvTripNew,MainMenu_CvVisit,MainMenu_CvReport,MainMenu_CvActiveTrip,MainMenu_CvRegister;
    SwipeRefreshLayout SWRefresh;
    Dialog dialog;

    Retrofit retrofit;
    API_SFA myAPi;
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

        BASE_URL = appConfig.getBASE_URL();
        VERSION_APK = appConfig.getVERSION_APK();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        dialog = new Dialog(Activity_MainMenu.this);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        SWRefresh = findViewById(R.id.sw_refresh);

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
        MainMenu_TxtActiveTripID = findViewById(R.id.MainMenu_TxtActiveTripID);

        MainMenu_CvActiveTrip = findViewById(R.id.MainMenu_CvActiveTrip);
        MainMenu_CvRegister = findViewById(R.id.MainMenu_CvRegister);
        MainMenu_CvVisit = findViewById(R.id.MainMenu_CvVisit);
        MainMenu_CvTripNew = findViewById(R.id.MainMenu_CvTripNew);
        MainMenu_CvReport = findViewById(R.id.MainMenu_CvReport);

        MainMenu_CvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Activity_Report.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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
                labelQuestion.setText("Trip aktif saat ini adalah "+MainMenu_TxtActiveTripID.getText().toString());

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


                                                            Call<Col_ActiveTrip> callData = myAPi.insertActiveTripDetail(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_LONGITUDE()),getPref(appConfig.getTAG_LATITUDE()),trip_id,getPref(appConfig.getTAG_GEOREVERSE()));
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
                                                                            }
                                                                        }
                                                                    }else{
                                                                            switch (response.code()) {
                                                                                case 404:
                                                                                    Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                                                                                    break;
                                                                                case 500:
                                                                                    Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                                                    break;
                                                                                case 502:
                                                                                    Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                                                                                    break;
                                                                                default:
                                                                                    Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                                                    break;
                                                                            }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                                                    Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
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


                                                            Call<Col_ActiveTrip> callData = myAPi.insertStopActiveTrip(getPref(appConfig.getTAG_SPVCODE()),getPref(appConfig.getTAG_LONGITUDE()),getPref(appConfig.getTAG_LATITUDE()),trip_id,getPref(appConfig.getTAG_GEOREVERSE()));
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
                                                                                Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 500:
                                                                                Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            case 502:
                                                                                Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                            default:
                                                                                Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                                                break;
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                                                    Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
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
        VERSION_APK = "1.0.0";
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
        TxtWelcome.setText("Selamat Datang "+getPref(appConfig.getTAG_SPVNAME()));

        SalesName = getPref("Selamat Datang "+getPref(appConfig.getTAG_SPVNAME()));
        SalesCode = getPref(appConfig.getTAG_SPVCODE());

        getSupportActionBar().setTitle(" Beranda");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.x_house);

        TxtTransactionDate = (TextView) findViewById(R.id.MainMenu_TglTransaksi);
        TxtTransactionDate.setText("Date : "+DownloadDate+" ("+weekstring+")");
    }

    public void getWeekNo(){
        Call<String> callWeekNo = myAPi.getWeekNo(getPref(appConfig.getTAG_SPVCODE()),"30011988AA");
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
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getActiveTrip(){
        MainMenu_LyActiveTrip.setVisibility(View.GONE);
        Call<Col_ActiveTrip> callData = myAPi.getActiveTrip(getPref(appConfig.getTAG_SPVCODE()));
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
                            MainMenu_TxtActiveTripLabel.setText(" Durasi "+data.get(0).getDuration()+" Menit");
                            MainMenu_TxtActiveTripID.setText("Trip #"+data.get(0).getId());
                        }else{
                            MainMenu_LyActiveTrip.setVisibility(View.GONE);
                        }
                    }
                }else{
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean checkPref(String KEY){
        boolean a = false;
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
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
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Activity_MainMenu.this.getSharedPreferences(appConfig.getTAG_PREF(), 0).edit().remove(appConfig.getTAG_LASTLOGIN()).commit();
                                Activity_MainMenu.this.getSharedPreferences(appConfig.getTAG_PREF(), 0).edit().remove(appConfig.getTAG_PASSWORD()).commit();
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Activity_MainMenu.this.finishAffinity();
                            Activity_MainMenu.this.finish();
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
        TxtLblNama.setText("eOrder v "+VERSION_APK);
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
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_LOGINTIME(),LoginTime);
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
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_WEEKNUMBER(),WeekNo);
        SettingPrefEditor.commit();
    }

    public void callTracingLog(String Desc) {
        Call<String> callLogout = myAPi.Tracing(getPref(appConfig.getTAG_SPVCODE()).replace("/", ""), getPref(appConfig.getTAG_SESSION()),Desc);
        callLogout.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                } else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MainMenu.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Activity_MainMenu.this, "Logout Gagal : " + appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPrefLocation(String Longitude,String Latitude,String Georeverse){
        SharedPreferences SettingPref = getApplicationContext().getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_LONGITUDE(),Longitude);
        SettingPrefEditor.putString(appConfig.getTAG_LATITUDE(),Latitude);
        SettingPrefEditor.putString(appConfig.getTAG_GEOREVERSE(),Georeverse);
        SettingPrefEditor.commit();
    }
}
