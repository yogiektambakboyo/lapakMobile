package com.lapakkreatiflamongan.smdsforce.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Login;
import com.lapakkreatiflamongan.smdsforce.utils.AppConfig;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_Loading extends AppCompatActivity {

    AppConfig appConfig = new AppConfig();
    ImageView ImgLoading;
    String passStored = "", userStored = "";
    private  String session = "";
    Retrofit retrofits;
    API_SFA myAPI;
    List<Data_Login> listValLogin = null;
    String Bearer="7",LastLogin = "", SPVCode = "", SPVName = "", Status = "0", Password = "", DownloadDate = "", BranchID = "", BranchName = "";
    String  deviceIMEI = "undefined";
    int isAirPlaneMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_loading);

        ImgLoading = (ImageView) findViewById(R.id.Loading_Image);

        retrofits = new Retrofit.Builder()
                .baseUrl(appConfig.getBASE_URL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = retrofits.create(API_SFA.class);

        YoYo.with(Techniques.FadeIn).duration(3000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (checkPref(appConfig.getTAG_PASSWORD())){
                    passStored = getPref(appConfig.getTAG_PASSWORD());
                    userStored = getPref(appConfig.getTAG_LASTLOGIN());

                    if(!passStored.equals("") && !userStored.equals("")){
                        // Download
                        final Dialog dialog = new Dialog(Activity_Loading.this);
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

                        Call<List<Data_Login>> call2 = myAPI.Login(userStored,passStored, appConfig.getVERSION_APK(), "",session);
                        call2.enqueue(new Callback<List<Data_Login>>() {
                            @Override
                            public void onResponse(Call<List<Data_Login>> call, Response<List<Data_Login>> response) {
                                TxtStatus.setText("Logging In. . ");
                                if (response.isSuccessful()) {
                                    listValLogin = response.body();

                                    for (Data_Login d : listValLogin) {
                                        Status = d.getStatus();

                                        if (Status.equals("1")) {
                                            SPVCode = d.getCode();
                                            SPVName = d.getName();
                                            Password = d.getPassword();
                                            DownloadDate = d.getDownloadDate();
                                            BranchID = d.getBranchID();
                                            BranchName = d.getBranchName();
                                            LastLogin = d.getUsername();
                                            Bearer = d.getBearer();

                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(Activity_Loading.this, "Login Gagal (01) : Username/Password Salah!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if (Status.equals("1")) {
                                        setPrefLogin(userStored, SPVCode, SPVName, DownloadDate, getToday(), BranchID, BranchName, Bearer, Password, appConfig.getBASE_URL(), "");
                                        //setPrefVersionNo(Version_Upd, Force_Upd);

                                        Intent in = new Intent(Activity_Loading.this, Activity_MainMenu.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(in);
                                    }

                                } else {
                                    dialog.dismiss();
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(Activity_Loading.this, "Login Gagal : " + appConfig.getERROR_404(), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(Activity_Loading.this, "Login Gagal : " + appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 502:
                                            Toast.makeText(Activity_Loading.this, "Login Gagal : " + appConfig.getERROR_502(), Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(Activity_Loading.this, "Login Gagal : " + appConfig.getERROR_500(), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<Data_Login>> call, Throwable t) {
                                dialog.dismiss();
                                if ((isAirPlaneMode == 1) || (!isMobileDataEnabled())) {
                                    Toast.makeText(Activity_Loading.this, "Gagal 101 : Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Activity_Loading.this, "Gagal 500 : Internal Server Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else{

                    }


                }else{
                    Intent intent = new Intent(Activity_Loading.this,Activity_Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(ImgLoading);


    }

    public boolean checkPref(String KEY){
        boolean a = false;
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        a = SettingPref.contains(KEY);
        return  a;
    }


    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
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

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(appConfig.getTAG_PREF(), Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public String getSession(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate+userStored;
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
}
