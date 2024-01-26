package com.lapakkreatiflamongan.ccp.intent;

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

import com.lapakkreatiflamongan.ccp.BuildConfig;
import com.lapakkreatiflamongan.ccp.schema.Col_Login;
import com.lapakkreatiflamongan.ccp.service.Worker_RealtimeTracking;
import com.lapakkreatiflamongan.ccp.utils.Config;
import com.lapakkreatiflamongan.ccp.utils.Fn_DBHandler;
import com.lapakkreatiflamongan.ccp.R;
import com.lapakkreatiflamongan.ccp.utils.TelephonyInfo;
import com.lapakkreatiflamongan.ccp.api.API_SFA;
import com.lapakkreatiflamongan.ccp.schema.Data_Login;
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

    String Bearer="7",LastLogin = "", SPVCode = "", SPVName = "", Status = "0", Password = "", DownloadDate = "", BranchID = "", BranchName = "";
    String deviceID = "undefined", deviceIMEI = "undefined";
    Col_Login listValLogin = null;

    Button btnSubmit;
    ImageView btnSetting, btnUpdate;
    EditText InputUserName, InputPasword;
    TextView TxtVersion,TxtForgotPassword;

    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";

    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_ACTIVE = "active";
    private final String TAG_MESSAGE = "message";
    private final String TAG_SESSION = "session";
    private final String TAG_LASTLOGIN = "lastlogin";
    private final String TAG_WEBSERVICE = "webservice";
    private final String TAG_DEVICEID = "deviceid";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_PASSWORD = "password";
    private final String TAG_DOWNLOADDATE = "downloaddate";
    private final String TAG_BRANCHID = "branchid";
    private final String TAG_BRANCHNAME = "branchname";
    private final String TAG_WEEKNUMBER = "weekno";
    private final String TAG_BEARER = "bearer";
    private final String TAG_VERSION_UPDATE = "VERSION_UPD";
    private final String TAG_FORCE_UPDATE = "FORCE_UPD";
    private final String TAG_LEADER = "leader";
    private String VERSION_APK = "0.0.7";
    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolderSuperVision/";
    private final String DB_MASTER = "MASTER";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";

    String LeaderName = "",Force_Upd = "0", Version_Upd = "0.0.7", Link = "", Desc = "", ReadMeLink = "", LinkWeb = "http://sfa.borwita.co.id/supervision/", LinkUpload = "http://sfa.borwita.co.id:3000/api/upload/photo", LinkUploadPHP = "http://sfa.borwita.co.id/supervision/api/v1/uploadfile.php";

    String RegisteredLogin = "0", WeekNumber = "1";
    private int dpScreen = 0;
    int valid = 0;
    int isAutoDate = 0;
    int isAutoZonaTime = 0;
    int isAirPlaneMode = 0;

    private  String session = "";

    TelephonyInfo telephonyInfo;
    String operatorName = "-", androidVersion = "-", ramCapacity = "-", deviceTipe = "-";

    Config config;
    Retrofit retrofits;
    API_SFA myAPI;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_login);

        String buildNo = "-";
        VERSION_APK = BuildConfig.VERSION_NAME;
        buildNo = "" + BuildConfig.VERSION_CODE;


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        config = new Config();

        retrofits = new Retrofit.Builder()
                .baseUrl(config.getBASE_URL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPI = retrofits.create(API_SFA.class);

        TelephonyManager Telephonemanager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        operatorName = Telephonemanager.getNetworkOperatorName();

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

        valid = 0;

        if (operatorName.equals("")) {
            operatorName = "-";
        }
        androidVersion = Build.VERSION.RELEASE;

        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        ramCapacity = "" + ((memInfo.totalMem) / 0x100000);
        deviceTipe = "";


        if (!checkPref(TAG_VERSION_UPDATE)) {
            setPrefVersionNo(Version_Upd, "0");
        } else {
            Version_Upd = getPref(TAG_VERSION_UPDATE);
            Force_Upd = getPref(TAG_FORCE_UPDATE);
        }

        Configuration config = getResources().getConfiguration();
        dpScreen = config.screenWidthDp;

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
                } else {
                    Msg = "Setting mobile data tidak aktif, silahkan aktifkan dahulu!!!";
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
            } else if (1>2 &&(!Version_Upd.equals("0.0.0")) && (!Version_Upd.equals(VERSION_APK))) {
                if (Force_Upd.equals("1")) {
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage("Versi yang anda gunakan sudah kadaluarsa, silahkan update App anda ke versi " + Version_Upd + "!!!")
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
                            .setMessage("Versi yang anda gunakan sudah kadaluarsa, silahkan update App anda ke versi " + Version_Upd + "!!!")
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

        if (!checkPref(TAG_SELLERCODE)) {
            setPrefSeller("-", "-");
        }

        btnSubmit = (Button) findViewById(R.id.Login_Submit);
        btnSubmit.setText("Login");
        TxtForgotPassword = findViewById(R.id.Login_ResetPassword);

        InputUserName = (EditText) findViewById(R.id.Login_username);
        if (checkPref(TAG_LASTLOGIN)) {
            LastLogin = getPref(TAG_LASTLOGIN);
            InputUserName.setText(LastLogin);
        }
        InputPasword = (EditText) findViewById(R.id.Login_pass);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_Login.this, "login", Toast.LENGTH_SHORT).show();

                if (InputUserName.getText().toString().trim().length() <= 0) {
                    InputUserName.setError("Isi dahulu username!!!");
                } else if (InputPasword.getText().toString().trim().length() <= 0) {
                    InputPasword.setError("Isi dahulu password!!!");
                } else {
                    if (!Version_Upd.equals(VERSION_APK) && 1>2) {
                        Toast.makeText(Activity_Login.this, "test", Toast.LENGTH_SHORT).show();
                        DialodKonfirmasiUpdate(Version_Upd);
                    } else {
                        Toast.makeText(Activity_Login.this, "test login", Toast.LENGTH_SHORT).show();
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

                        Call<Col_Login> call2 = myAPI.Login(InputUserName.getText().toString().trim().toUpperCase(), InputPasword.getText().toString().trim(), VERSION_APK, deviceIMEI,session);
                        call2.enqueue(new Callback<Col_Login>() {
                            @Override
                            public void onResponse(Call<Col_Login> call, Response<Col_Login> response) {
                                TxtStatus.setText("Logging In. . ");
                                if (response.isSuccessful()) {
                                    listValLogin = response.body();

                                    Log.e( "onResponse: ", response.body().toString() );

                                    for (Data_Login d : listValLogin.getData()) {
                                        Status = d.getStatus();

                                        if (Status.equals("1"))  {
                                            SPVCode = d.getCode();
                                            SPVName = d.getName();
                                            Password = d.getPassword();
                                            DownloadDate = d.getDownloadDate();
                                            BranchID = d.getBranchid();
                                            BranchName = d.getBranchname();
                                            LeaderName = d.getVersionupdate();
                                            //Force_Upd = d.getForceUpdate();
                                            LastLogin = d.getUsername();
                                            Bearer = d.getBearer();

                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(Activity_Login.this, "Login Gagal (01) : Username/Password Salah!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if (Status.equals("1")) {
                                        setPrefLogin(InputUserName.getText().toString().toUpperCase().trim(), SPVCode, SPVName, DownloadDate, getToday(), BranchID, BranchName, Bearer, Password, "", LeaderName);
                                        setPrefVersionNo(Version_Upd, Force_Upd);

                                        Intent in = new Intent(Activity_Login.this, Activity_MainMenu.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(in);
                                    }

                                } else {
                                    dialog.dismiss();
                                    Log.e( "onResponse: ", response.body().toString() );
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                            break;
                                        case 502:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(Activity_Login.this, "Login Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<Col_Login> call, Throwable t) {
                                dialog.dismiss();
                                Log.e( "onResponse: ", "Failed" );
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
        TxtVersion.setText("Version " + VERSION_APK + " ( Build No " + buildNo + " )");

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
        labelQ.setText("Apakah anda yakin akan memperbaharui aplikasi ini?");

        final TextView labelVersi = new TextView(this);
        labelVersi.setText("Versi APK saat ini : " + VERSION_APK);

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
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public boolean checkPref(String KEY){
        boolean a = false;
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        a = SettingPref.contains(KEY);
        return  a;
    }

    public void setPrefLogin(String LastLogin, String SPVCode, String SPVName, String DownloadDate, String LoginTime, String BranchID, String BranchName, String Bearer, String Password, String WebService, String LeaderName){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_LASTLOGIN,LastLogin);
        SettingPrefEditor.putString(TAG_SPVCODE,SPVCode);
        SettingPrefEditor.putString(TAG_SPVNAME,SPVName);
        SettingPrefEditor.putString(TAG_DOWNLOADDATE,DownloadDate);
        SettingPrefEditor.putString(TAG_LOGINTIME,LoginTime);
        SettingPrefEditor.putString(TAG_BRANCHID,BranchID);
        SettingPrefEditor.putString(TAG_BRANCHNAME,BranchName);
        SettingPrefEditor.putString(TAG_BEARER,Bearer);
        SettingPrefEditor.putString(TAG_PASSWORD,Password);
        SettingPrefEditor.putString(TAG_WEBSERVICE,WebService);
        SettingPrefEditor.putString(TAG_DEVICEID,deviceIMEI);
        SettingPrefEditor.putString(TAG_SESSION,session);
        SettingPrefEditor.putString(TAG_LEADER,LeaderName);
        SettingPrefEditor.commit();
    }

    public void setPrefSeller(String SellerCode, String SellerName){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_SELLERCODE,SellerCode);
        SettingPrefEditor.putString(TAG_SELLERNAME,SellerName);
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
                            /*if(isMyServiceRunning(Service_Location.class)){
                                stopService(ServiceInt);
                            }
                            if(isMyServiceRunning(Service_LocationGFused.class)){
                                stopService(ServiceIntGFused);
                            }*/
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
            builder.setMessage("Apakah anda yakin akan keluar?").setPositiveButton("Ya", dialogClickListener)
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

    public String getDeviceID(String myIMEI){
        final String tmDevice, androidId;
        tmDevice = "" + myIMEI;
        //tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), (long)tmDevice.hashCode() << 32);
        String deviceId = deviceUuid.toString();
        return  deviceId;
    }

    public ArrayList<File> getAllFilesInDir(File dir) {
        if (dir == null)
            return null;

        ArrayList<File> files = new ArrayList<File>();

        Stack<File> dirlist = new Stack<File>();
        dirlist.clear();
        dirlist.push(dir);

        while (!dirlist.isEmpty()) {
            File dirCurrent = dirlist.pop();

            File[] fileList = dirCurrent.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory())
                    dirlist.push(aFileList);
                else
                    files.add(aFileList);
            }
        }

        return files;
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

    public String getTodayDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public void setPrefVersionNo(String VersionNo, String ForceUpdate){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_VERSION_UPDATE,VersionNo);
        SettingPrefEditor.putString(TAG_FORCE_UPDATE,ForceUpdate);
        SettingPrefEditor.commit();
    }

    public void setPrefWeekNo(String WeekNo){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_WEEKNUMBER,WeekNo);
        SettingPrefEditor.commit();
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
                    String Msg = "Aplikasi tidak akan berjalan jika anda tidak memberi izin untuk mengakses Telepon!!!";
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Login.this.finish();
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
                    String Msg = "Aplikasi tidak akan berjalan jika anda tidak memberi izin untuk mengakses Kamera!!! Aktifkan/Centang izin untuk Kamera di Menu Setting > Apps > Izin/Permission ";
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Login.this.finish();
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
                    String Msg = "Aplikasi tidak akan berjalan jika anda tidak memberi izin untuk mengakses Lokasi!!!";
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Login.this.finish();
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
                    String Msg = "Aplikasi tidak akan berjalan jika anda tidak memberi izin untuk mengakses Penyimpanan!!!";
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Login.this.finish();
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
                    String Msg = "Aplikasi tidak akan berjalan jika anda tidak memberi izin untuk mengakses Penyimpanan!!!";
                    new AlertDialog.Builder(Activity_Login.this)
                            .setTitle("Information")
                            .setMessage(Msg)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Activity_Login.this.finish();
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
                    return false;
                }
            }
        }
        return true;
    }

    public void ShowDialog(String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 15, 30, 15);

        final TextView TxtLblNama = new TextView(this);
        TxtLblNama.setText(""+Msg);
        TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


        layout.addView(TxtLblNama, params);

        builder.setView(layout);
        setPrefActive("0",Msg);

        // Set up the buttons
        builder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=app.bcp.supervision"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
                Activity_Login.this.finishAffinity();
            }
        });


        builder.show();
    }

    public void setPrefActive(String Active,String Msg){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_ACTIVE,Active);
        SettingPrefEditor.putString(TAG_MESSAGE,Msg);
        SettingPrefEditor.commit();
    }

}
