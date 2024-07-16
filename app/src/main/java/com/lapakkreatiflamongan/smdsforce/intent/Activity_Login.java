package com.lapakkreatiflamongan.smdsforce.intent;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.common.util.concurrent.ListenableFuture;


import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.lapakkreatiflamongan.smdsforce.service.Worker_RealtimeTracking;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;
import com.lapakkreatiflamongan.smdsforce.utils.Fn_DBHandler;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.utils.TelephonyInfo;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Login;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Activity_Login extends AppCompatActivity {
    Fn_DBHandler dbMaster;
    AppConfig appConfig = new AppConfig();

    String Bearer="7",LastLogin = "", SPVCode = "", SPVName = "", Status = "0", Password = "", DownloadDate = "", BranchID = "", BranchName = "";
    String deviceIMEI = "undefined";
    List<Data_Login> listValLogin = null;

    Button btnSubmit;
    ImageView btnSetting, btnUpdate;
    EditText InputUserName, InputPasword;
    TextView TxtVersion,TxtForgotPassword;

    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolderSuperVision/";

    String LeaderName = "",Force_Upd = "0", Version_Upd = "0.0.7", Link = "", Desc = "", ReadMeLink = "", LinkWeb = "http://sfa.borwita.co.id/supervision/", LinkUpload = "http://sfa.borwita.co.id:3000/api/upload/photo", LinkUploadPHP = "http://sfa.borwita.co.id/supervision/api/v1/uploadfile.php";

    private int dpScreen = 0;
    int valid = 0;
    int isAutoDate = 0;
    int isAutoZonaTime = 0;
    int isAirPlaneMode = 0;
    private  String session = "";

    Retrofit retrofits;
    API_SFA myAPI;

    private String TAG_WORKMANAGERTRACKING = "TRACKING_START";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_login);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        retrofits = new Retrofit.Builder()
                .baseUrl(appConfig.getBASE_URL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = retrofits.create(API_SFA.class);

        dbMaster = new Fn_DBHandler(Activity_Login.this, appConfig.getDB_MASTER());
        final File dbFileMaster = new File(Activity_Login.this.getFilesDir() + "/" + appConfig.getDB_MASTER());

        if (!dbFileMaster.exists()) {
            dbMaster.CreateMaster();
        }


        if (!checkPref(appConfig.getTAG_VERSION_UPDATE())) {
            setPrefVersionNo(Version_Upd, "0");
        } else {
            Version_Upd = getPref(appConfig.getTAG_VERSION_UPDATE());
            Force_Upd = getPref(appConfig.getTAG_FORCE_UPDATE());
        }

        File newdir = new File(dir);
        if (!newdir.exists()) {
            newdir.mkdirs();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {

            isAutoDate = Settings.System.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            isAutoZonaTime = Settings.System.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE);
            isAirPlaneMode = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
            if ((isAutoDate == 0) || (isAutoZonaTime == 0) || (isAirPlaneMode == 1) || (!isMobileDataEnabled())) {
                String Msg = "Tanggal di perangkat tidak tersetting automatic, silahkan centang auto datetime time di setting?";
                if (isAutoDate == 0) {
                    Msg = "Tanggal di perangkat tidak tersetting automatic, silahkan centang auto datetime time di setting?";
                } else if (isAutoZonaTime == 0) {
                    Msg = "Zona waktu di perangkat tidak tersetting automatic, silahkan centang auto zona waktu di setting?";
                } else if (isAirPlaneMode == 1) {
                    Msg = "Setting Air plane mode aktif, silahkan non aktifkan dahulu!!!";
                } else if (!isMobileDataEnabled()) {
                    Msg = "Setting mobile data tidak aktif, silahkan aktifkan dahulu!!!";
                }
                else {
                    Msg = "GPS tidak aktif, silahkan aktifkan dahulu di setting!!!";
                }
                new AlertDialog.Builder(Activity_Login.this)
                        .setTitle("Information")
                        .setMessage(Msg)
                        .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                Activity_Login.this.finish();
                                Activity_Login.this.finishAffinity();
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else if (1>2 &&(!Version_Upd.equals("0.0.0")) && (!Version_Upd.equals(appConfig.getVERSION_APK()))) {
                if (Force_Upd.equals("1")) {
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage("Versi yang anda gunakan sudah kadaluarsa, silahkan update App eOrder anda ke versi " + Version_Upd + "!!!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    DialodKonfirmasiUpdate(Version_Upd);
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage("Versi yang anda gunakan sudah kadaluarsa, silahkan update App eOrder anda ke versi " + Version_Upd + "!!!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    DialodKonfirmasiUpdate(Version_Upd);
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(true)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (!checkPref(appConfig.getTAG_SELLERCODE())) {
            setPrefSeller("-", "-");
        }
        
        btnSubmit = (Button) findViewById(R.id.Login_Submit);
        btnSubmit.setText("Login");
        TxtForgotPassword = findViewById(R.id.Login_ResetPassword);


        InputUserName = (EditText) findViewById(R.id.Login_username);
        if (checkPref(appConfig.getTAG_LASTLOGIN())) {
            LastLogin = getPref(appConfig.getTAG_LASTLOGIN());
            InputUserName.setText(LastLogin);
        }
        InputPasword = (EditText) findViewById(R.id.Login_pass);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InputUserName.getText().toString().trim().length() <= 0) {
                    InputUserName.setError("Isi dahulu username!!!");
                } else if (InputPasword.getText().toString().trim().length() <= 0) {
                    InputPasword.setError("Isi dahulu password!!!");
                } else {
                    if (!Version_Upd.equals(appConfig.getVERSION_APK()) && 1>2) {
                        DialodKonfirmasiUpdate(Version_Upd);
                    } else {

                        // Download
                        final Dialog dialog = new Dialog(Activity_Login.this);
                        dialog.setContentView(R.layout.d_logindownload);
                        dialog.setCancelable(false);

                        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
                        TxtStatus.setText("Mohon Tunggu. . ");

                        dialog.show();

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .writeTimeout(60, TimeUnit.SECONDS)
                                .build();

                        session = getSession();

                        Call<List<Data_Login>> call2 = myAPI.Login(InputUserName.getText().toString().trim().toUpperCase(), InputPasword.getText().toString().trim(), appConfig.getVERSION_APK(), deviceIMEI,session);
                        call2.enqueue(new Callback<List<Data_Login>>() {
                            @Override
                            public void onResponse(Call<List<Data_Login>> call, Response<List<Data_Login>> response) {
                                TxtStatus.setText("Logging In. . ");
                                if (response.isSuccessful()) {
                                    listValLogin = response.body();

                                    for (Data_Login d : listValLogin) {
                                        Status = d.getStatus();

                                        if (Status.equals("1"))  {
                                            SPVCode = d.getCode();
                                            SPVName = d.getName();
                                            Password = d.getPassword();
                                            DownloadDate = d.getDownloadDate();
                                            BranchID = d.getBranchID();
                                            BranchName = d.getBranchName();
                                            LeaderName = d.getVersionUpdate();
                                            //Force_Upd = d.getForceUpdate();
                                            LastLogin = d.getUsername();
                                            Bearer = d.getBearer();

                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(Activity_Login.this, "Login Gagal (01) : Username/Password Salah!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if (Status.equals("1")) {
                                        setPrefLogin(InputUserName.getText().toString().toUpperCase().trim(), SPVCode, SPVName, DownloadDate, getToday(), BranchID, BranchName, Bearer, Password, appConfig.getBASE_URL(), LeaderName);
                                        setPrefVersionNo(Version_Upd, Force_Upd);

                                        Intent in = new Intent(Activity_Login.this, Activity_MainMenu.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(in);
                                    }

                                } else {
                                    dialog.dismiss();
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 502:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<Data_Login>> call, Throwable t) {
                                dialog.dismiss();
                                if ((isAirPlaneMode == 1) || (!isMobileDataEnabled())){
                                    Toast.makeText(Activity_Login.this, "Gagal 101 : Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Activity_Login.this, "Gagal 500 : Internal Server Error", Toast.LENGTH_SHORT).show();
                                }

                            }


                        });
                    }
                }
            }
        });

        SmartLocation.with(Activity_Login.this).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null){
                }
            }
        });
        TxtVersion = (TextView) findViewById(R.id.Login_TxtVersion);
        TxtVersion.setText("Version " + appConfig.getVERSION_APK() + " ( Build No " + appConfig.getBUILD_NO() + " )");

        btnSetting = (ImageView) findViewById(R.id.Login_ImgSetting);


        btnUpdate = (ImageView) findViewById(R.id.Login_Update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DialodKonfirmasiUpdate(Version_Upd);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).playOn(btnUpdate);
            }
        });

        if(!isWorkScheduled(TAG_WORKMANAGERTRACKING)) { // check if your work is not already scheduled
            Log.e("onCreate", "VM Realtim Tracking" );
            PeriodicWorkRequest.Builder BuilderWorkWM =
                    new PeriodicWorkRequest.Builder(Worker_RealtimeTracking.class, 10,
                            TimeUnit.MINUTES).setInitialDelay(2, TimeUnit.MINUTES);
            PeriodicWorkRequest wmRealtime = BuilderWorkWM.build();
            WorkManager instance = WorkManager.getInstance(Activity_Login.this.getApplicationContext());
            instance.enqueueUniquePeriodicWork(TAG_WORKMANAGERTRACKING, ExistingPeriodicWorkPolicy.REPLACE , wmRealtime);
        }
    }

    private boolean isWorkScheduled(String tag) {
        WorkManager instance = WorkManager.getInstance(getApplication().getApplicationContext());
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void DialodKonfirmasiUpdate(final String VersiTerbaru){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Update APK");
        builder.setIcon(R.drawable.sfa_info_2);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 5, 30, 0);

        final TextView labelQ = new TextView(this);
        labelQ.setText("Apakah anda yakin akan memperbaharui aplikasi eOrder?");

        final TextView labelVersi = new TextView(this);
        labelVersi.setText("Versi APK saat ini : " + appConfig.getVERSION_APK());

        final TextView labelVersiNew = new TextView(this);
        labelVersiNew.setText("Versi APK Update : "+VersiTerbaru);

        final TextView labelDesc =new TextView(this);
        labelDesc.setClickable(true);


        if (dpScreen>=600){
            labelQ.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            labelVersi.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            labelVersiNew.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            labelDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        }else{
            labelQ.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            labelVersi.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            labelVersiNew.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            labelDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        }

        layout.addView(labelQ, params);
        layout.addView(labelVersi, params);
        layout.addView(labelVersiNew,params);
        layout.addView(labelDesc,params);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=app.bcp.supervision"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public boolean checkPref(String KEY){
        boolean a = false;
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        a = SettingPref.contains(KEY);
        return  a;
    }

    public void setPrefLogin(String LastLogin, String SPVCode, String SPVName, String DownloadDate, String LoginTime, String BranchID, String BranchName, String Bearer, String Password, String WebService, String LeaderName){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_LASTLOGIN(),LastLogin);
        SettingPrefEditor.putString(appConfig.getTAG_SPVCODE(),SPVCode);
        SettingPrefEditor.putString(appConfig.getTAG_SPVNAME(),SPVName);
        SettingPrefEditor.putString(appConfig.getTAG_DOWNLOADDATE(),DownloadDate);
        SettingPrefEditor.putString(appConfig.getTAG_LOGINTIME(),LoginTime);
        SettingPrefEditor.putString(appConfig.getTAG_BRANCHID(),BranchID);
        SettingPrefEditor.putString(appConfig.getTAG_BRANCHNAME(),BranchName);
        SettingPrefEditor.putString(appConfig.getTAG_BEARER(),Bearer);
        SettingPrefEditor.putString(appConfig.getTAG_PASSWORD(),Password);
        SettingPrefEditor.putString(appConfig.getTAG_WEBSERVICE(),WebService);
        SettingPrefEditor.putString(appConfig.getTAG_DEVICEID(),deviceIMEI);
        SettingPrefEditor.putString(appConfig.getTAG_SESSION(),session);
        SettingPrefEditor.putString(appConfig.getTAG_LEADER(),LeaderName);
        SettingPrefEditor.commit();
    }
    public void setPrefSeller(String SellerCode, String SellerName){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_SELLERCODE(),SellerCode);
        SettingPrefEditor.putString(appConfig.getTAG_SELLERNAME(),SellerName);
        SettingPrefEditor.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Activity_Login.this.finish();
                            Activity_Login.this.finishAffinity();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Apakah anda yakin akan keluar eOrder?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        Boolean isActive = false;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            isActive = (Boolean)m.invoke(cm);

            if (!isActive){
                @SuppressLint("MissingPermission") NetworkInfo a = cm.getActiveNetworkInfo();

                if (a != null){
                    isActive = a.isConnected();
                }
            }

            return isActive;
        } catch (Exception e) {
            e.printStackTrace();
            return isActive;
        }
    }


    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getSession(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate+InputUserName.getText().toString().trim();
    }



    public void setPrefVersionNo(String VersionNo, String ForceUpdate){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(appConfig.getTAG_VERSION_UPDATE(),VersionNo);
        SettingPrefEditor.putString(appConfig.getTAG_FORCE_UPDATE(),ForceUpdate);
        SettingPrefEditor.commit();
    }
}
