package com.lapakkreatiflamongan.smdsforce.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppConfig {
    private String VERSION_APK = "1.1.1";
    private String BUILD_NO = "13";

    private String App_Name = "eOrder";

    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_PASSWORD = "password";
    private final String DB_MASTER = "MASTER";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";

    private final String TAG_LASTLOGIN = "lastlogin";
    private final String TAG_NOTIFTITLE = "notiftitle";
    private final String TAG_NOTIFMSG = "notifmsg";

    private final String TAG_ACTIVE = "active";
    private final String TAG_MESSAGE = "message";
    private final String TAG_SESSION = "session";
    private final String TAG_WEBSERVICE = "webservice";
    private final String TAG_DEVICEID = "deviceid";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_DOWNLOADDATE = "downloaddate";
    private final String TAG_BRANCHID = "branchid";
    private final String TAG_BRANCHNAME = "branchname";
    private final String TAG_WEEKNUMBER = "weekno";
    private final String TAG_BEARER = "bearer";
    private final String TAG_VERSION_UPDATE = "VERSION_UPD";
    private final String TAG_FORCE_UPDATE = "FORCE_UPD";
    private final String TAG_GEOREVERSE = "georeverse";
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_LEADER = "leader";
    private final String TAG_CUSTOMERID = "customer_id";
    private final String TAG_CUSTOMERNAME = "customer_name";

    private String BASE_URL = "http://lapakkreatif.com:8081/";

    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";

    public AppConfig() {
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getTodayDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getTAG_LATITUDE() {
        return TAG_LATITUDE;
    }

    public String getTAG_CUSTOMERID() {
        return TAG_CUSTOMERID;
    }

    public String getTAG_CUSTOMERNAME() {
        return TAG_CUSTOMERNAME;
    }

    public String getTAG_LONGITUDE() {
        return TAG_LONGITUDE;
    }

    public String getTAG_SELLERNAME() {
        return TAG_SELLERNAME;
    }

    public String getTAG_SELLERCODE() {
        return TAG_SELLERCODE;
    }

    public String getBUILD_NO() {
        return BUILD_NO;
    }

    public void setBUILD_NO(String BUILD_NO) {
        this.BUILD_NO = BUILD_NO;
    }

    public String getDB_MASTER() {
        return DB_MASTER;
    }


    public String getVERSION_APK() {
        return VERSION_APK;
    }

    public String getTAG_GEOREVERSE() {
        return TAG_GEOREVERSE;
    }

    public void setVERSION_APK(String VERSION_APK) {
        this.VERSION_APK = VERSION_APK;
    }

    public String getTAG_PREF() {
        return TAG_PREF;
    }

    public String getTAG_PASSWORD() {
        return TAG_PASSWORD;
    }

    public String getTAG_LASTLOGIN() {
        return TAG_LASTLOGIN;
    }

    public String getTAG_ACTIVE() {
        return TAG_ACTIVE;
    }

    public String getTAG_MESSAGE() {
        return TAG_MESSAGE;
    }

    public String getTAG_SESSION() {
        return TAG_SESSION;
    }

    public String getTAG_WEBSERVICE() {
        return TAG_WEBSERVICE;
    }

    public String getTAG_DEVICEID() {
        return TAG_DEVICEID;
    }

    public String getTAG_SPVNAME() {
        return TAG_SPVNAME;
    }

    public String getTAG_SPVCODE() {
        return TAG_SPVCODE;
    }

    public String getApp_Name() {
        return App_Name;
    }

    public void setApp_Name(String app_Name) {
        App_Name = app_Name;
    }

    public String getTAG_LOGINTIME() {
        return TAG_LOGINTIME;
    }

    public String getTAG_DOWNLOADDATE() {
        return TAG_DOWNLOADDATE;
    }

    public String getTAG_BRANCHID() {
        return TAG_BRANCHID;
    }

    public String getTAG_BRANCHNAME() {
        return TAG_BRANCHNAME;
    }

    public String getTAG_WEEKNUMBER() {
        return TAG_WEEKNUMBER;
    }

    public String getTAG_BEARER() {
        return TAG_BEARER;
    }

    public String getTAG_VERSION_UPDATE() {
        return TAG_VERSION_UPDATE;
    }

    public String getTAG_FORCE_UPDATE() {
        return TAG_FORCE_UPDATE;
    }

    public String getTAG_NOTIFTITLE() {
        return TAG_NOTIFTITLE;
    }

    public String getTAG_NOTIFMSG() {
        return TAG_NOTIFMSG;
    }

    public String getTAG_LEADER() {
        return TAG_LEADER;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getERROR_500() {
        return ERROR_500;
    }

    public String getERROR_404() {
        return ERROR_404;
    }

    public String getERROR_301() {
        return ERROR_301;
    }

    public String getERROR_400() {
        return ERROR_400;
    }

    public String getERROR_401() {
        return ERROR_401;
    }

    public String getERROR_502() {
        return ERROR_502;
    }
}
