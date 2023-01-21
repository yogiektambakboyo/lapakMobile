package com.lapakkreatiflamongan.smdsforce.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.lapakkreatiflamongan.smdsforce.schema.Data_Position;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class Worker_RealtimeTracking extends Worker {
    private final String TAG_LASTLOGIN = "lastlogin";
    private final String TAG_SPVNAME = "username";
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_GEOREVERSE = "georeverse";
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_LEADER = "leader";

    public Worker_RealtimeTracking(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e( "doWork: ","Started Realtime "+getTodayHour() );
        if(Integer.parseInt(getTodayHour())>=7&&Integer.parseInt(getTodayHour())<17){
            SmartLocation.with(getApplicationContext()).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            Log.e( "doWork: ","Started Realtime Location Updated" );
                            if (location != null){
                                Log.e( "doWork: ","Started Realtime Location Updated Firebase" );
                                setPrefLocation(location.getLongitude()+"",location.getLatitude()+"","");
                            }
                        }
                    });
        }else{
            Log.e( "doWork: ","Clock Over" );
        }
        return Result.success();
    }


    public String getPref(String KEY){
        SharedPreferences SettingPref = getApplicationContext().getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public void setPrefLocation(String Longitude,String Latitude,String Georeverse){
        SharedPreferences SettingPref = getApplicationContext().getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor SettingPrefEditor = SettingPref.edit();
        SettingPrefEditor.putString(TAG_LONGITUDE,Longitude);
        SettingPrefEditor.putString(TAG_LATITUDE,Latitude);
        SettingPrefEditor.putString(TAG_GEOREVERSE,Georeverse);
        SettingPrefEditor.commit();
    }

    public String getTodayHour(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }
}
