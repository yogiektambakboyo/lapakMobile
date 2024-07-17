package com.lapakkreatiflamongan.smdsforce.intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Trip;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_TripDetail;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_Trip extends AppCompatActivity {
    AppConfig appConfig = new AppConfig();
    String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    Dialog dialog;
    SpinKitView loader;

    Retrofit retrofit;
    API_SFA myAPi;

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

        BASE_URL = appConfig.getBASE_URL();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPi = retrofit.create(API_SFA.class);

        getSupportActionBar().setTitle("Daftar Trip "+getToday());

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean isActiveGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{

            if (!isActiveGPS) {
                String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak menyalakan Lokasi GPS!!!";
                new AlertDialog.Builder(Activity_Trip.this)
                        .setTitle("Information")
                        .setMessage(Msg)
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Activity_Trip.this, Activity_MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }



        listViewTrip  = findViewById(R.id.Trip_List);
        loader          = findViewById(R.id.Trip_Loading);
        InputSearch     = findViewById(R.id.Trip_Search);

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
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        String Value = SettingPref.getString(KEY, "0");
        return  Value;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 8: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak memberi izin untuk mengakses Telepon!!!";
                    new AlertDialog.Builder(Activity_Trip.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Trip.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;
            }

            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak memberi izin untuk mengakses Kamera!!! Aktifkan/Centang izin untuk Kamera di Menu Setting > Apps > SMD SForce BCP > Izin/Permission ";
                    new AlertDialog.Builder(Activity_Trip.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Trip.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak memberi izin untuk mengakses Lokasi!!!";
                    new AlertDialog.Builder(Activity_Trip.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Trip.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 4: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak memberi izin untuk mengakses Penyimpanan!!!";
                    new AlertDialog.Builder(Activity_Trip.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Trip.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    String Msg = "Aplikasi eOrder tidak akan berjalan jika anda tidak memberi izin untuk mengakses Penyimpanan!!!";
                    new AlertDialog.Builder(Activity_Trip.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Trip.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

    public void getActiveTripAll(){
        dialog.show();
        Call<Col_ActiveTrip> callData = myAPi.getActiveTripAll(getPref(appConfig.getTAG_SPVCODE()));
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

                                    Call<Col_ActiveTrip> callData = myAPi.getActiveTripDetail(getPref(appConfig.getTAG_SPVCODE()),adapterTrip.getItem(i).getId());
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
                                                        Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 500:
                                                        Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                        break;
                                                    case 502:
                                                        Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                                                        break;
                                                    default:
                                                        Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                                            dialog.dismiss();
                                            Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_Trip.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_ActiveTrip> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Trip.this, "Ambil Data Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}