package com.lapakkreatiflamongan.ccp.intent;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lapakkreatiflamongan.ccp.R;
import com.lapakkreatiflamongan.ccp.api.API_SFA;
import com.lapakkreatiflamongan.ccp.schema.Col_StoreNOO;
import com.lapakkreatiflamongan.ccp.schema.Data_StoreNOO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class Activity_NOOForm extends AppCompatActivity {
    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_CUSTOMERID = "customer_id";
    private final String TAG_CUSTOMERNAME = "customer_name";
    int TAKE_PHOTO_CODE_EXT = 99;
    private final String BASE_URL = "http://kakikupos.com:8081/";
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);
    private String dir = null,dirImageFile = "", photoFile = "";
    ImageView imgRefresh;
    TextView txtSales,txtLocation,txtGeoReverse;
    EditText inputNotes;
    Button btnSave;
    int isValidPhoto = 0;

    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_504 = "504 Gateway Time Out";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_408 = "408 Request Time Out";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";
    String uploadedImgStore = "-";
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_GEOREVERSE = "georeverse";
    private final String TAG_SPVNAME = "username";

    List<Data_StoreNOO> dataStoreNOO = new ArrayList<Data_StoreNOO>();

    TextView TxtID;
    EditText InputName,InputAddress,InputCity,InputType,InputClass,InputContact,InputContactJob,InputContactLevel,InputNPWP,InputNIK,InputPhoneNo,InputWA,InputEmail,InputCreditLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_nooform);

        getSupportActionBar().setTitle("Form Pendaftaran");

        InputName = findViewById(R.id.NOOForm_Name);
        InputAddress = findViewById(R.id.NOOForm_Address);
        InputCity = findViewById(R.id.NOOForm_City);
        InputType = findViewById(R.id.NOOForm_Type);
        InputClass = findViewById(R.id.NOOForm_Clasification);
        InputContact = findViewById(R.id.NOOForm_ContactPerson);
        InputContactJob = findViewById(R.id.NOOForm_ContactPersonJobTitle);
        InputContactLevel = findViewById(R.id.NOOForm_ContactPersonLevel);
        InputNPWP = findViewById(R.id.NOOForm_NPWP);
        InputNIK = findViewById(R.id.NOOForm_CitizenID);
        InputPhoneNo = findViewById(R.id.NOOForm_PhoneNo);
        InputWA = findViewById(R.id.NOOForm_WhatsAppNo);
        InputEmail = findViewById(R.id.NOOForm_Email);
        InputCreditLimit = findViewById(R.id.NOOForm_CreditLimit);
        TxtID = findViewById(R.id.NOOForm_ID);

        dir = getApplicationContext().getFilesDir() + "/picFolder/";

        txtLocation = findViewById(R.id.NOOForm_Location);
        txtGeoReverse = findViewById(R.id.NOOForm_GeoReverse);
        txtSales = findViewById(R.id.NOOForm_Sales);
        txtSales.setText(getPref(TAG_SPVNAME));
        imgRefresh = findViewById(R.id.NOOForm_ImgRefresh);

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).duration(300).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
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
                                                            txtLocation.setText(getPref(TAG_LONGITUDE)+","+getPref(TAG_LATITUDE));
                                                            txtGeoReverse.setText(getPref(TAG_GEOREVERSE));
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(Activity_NOOForm.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).playOn(imgRefresh);

            }
        });

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
                                            txtLocation.setText(getPref(TAG_LONGITUDE)+","+getPref(TAG_LATITUDE));
                                            txtGeoReverse.setText(getPref(TAG_GEOREVERSE));
                                        }
                                    });
                        }else{
                            Toast.makeText(Activity_NOOForm.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnSave = findViewById(R.id.NOOForm_Save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputCreditLimit.getText().length()<=0){
                    InputCreditLimit.setText("0");
                }
                if (InputName.getText().length()<=0){
                    Toast.makeText(Activity_NOOForm.this,"Silahkan isi nama dahulu",Toast.LENGTH_SHORT).show();
                    InputName.requestFocus();
                }else if (InputAddress.getText().length()<=0){
                    Toast.makeText(Activity_NOOForm.this,"Silahkan isi alamat dahulu",Toast.LENGTH_SHORT).show();
                    InputAddress.requestFocus();
                }else{
                    new AlertDialog.Builder(Activity_NOOForm.this)
                            .setTitle("Information")
                            .setMessage("Apakah anda yakin akan simpan")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            Call<String> callInsert = myAPi.updateNOO(
                                                    getPref(TAG_CUSTOMERID),
                                                    getPref(TAG_SPVCODE),
                                                    InputName.getText().toString(),
                                                    InputAddress.getText().toString(),
                                                    InputPhoneNo.getText().toString(),
                                                    InputCity.getText().toString(),
                                                    InputCreditLimit.getText().toString(),
                                                    InputEmail.getText().toString(),
                                                    InputPhoneNo.getText().toString(),
                                                    InputWA.getText().toString(),
                                                    InputNIK.getText().toString(),
                                                    InputNPWP.getText().toString(),
                                                    InputContact.getText().toString(),
                                                    InputType.getText().toString(),
                                                    InputClass.getText().toString(),
                                                    InputContactJob.getText().toString(),
                                                    getPref(TAG_LONGITUDE),
                                                    getPref(TAG_LATITUDE),
                                                    InputContactLevel.getText().toString());

                                            callInsert.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful()){
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NOOForm.this);
                                                        builder.setTitle("Konfirmasi");
                                                        builder.setCancelable(false);
                                                        builder.setMessage("Data Pengajuan NOO "+getPref(TAG_CUSTOMERNAME)+" berhasil disimpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                Intent intent1 = new Intent(Activity_NOOForm.this, Activity_NOO.class);
                                                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent1);
                                                            }
                                                        }).show();
                                                    }else{
                                                        Log.e("Error","Gagal Upload ");
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NOOForm.this);
                                                        switch (response.code()) {
                                                            case 404:
                                                                builder.setTitle("Warning");
                                                                builder.setMessage("Ops, Gagal sinkron data "+ERROR_404+", Silahkan ulangi proses simpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }).show();
                                                                break;
                                                            case 500:
                                                                builder.setTitle("Warning");
                                                                builder.setMessage("Ops, Gagal sinkron data "+ERROR_500+", Silahkan ulangi proses simpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }).show();
                                                                break;
                                                            case 502:
                                                                builder.setTitle("Warning");
                                                                builder.setMessage("Ops, Gagal sinkron data "+ERROR_502+", Silahkan ulangi proses simpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }).show();
                                                                break;
                                                            default:
                                                                builder.setTitle("Warning");
                                                                builder.setMessage("Ops, Gagal sinkron data "+ERROR_500+", Silahkan ulangi proses simpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }).show();
                                                                break;
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {

                                                }
                                            });

                                        }
                                    })
                            .setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        getData();

    }


    public void getData(){
        Call<Col_StoreNOO> callData = myAPi.getStoreNOODetail(getPref(TAG_SPVCODE),getPref(TAG_CUSTOMERID));
        callData.enqueue(new Callback<Col_StoreNOO>() {
            @Override
            public void onResponse(Call<Col_StoreNOO> call, Response<Col_StoreNOO> response) {
                if (response.isSuccessful()){
                    Col_StoreNOO col = response.body();
                    if (col == null){
                        Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_StoreNOO> data = col.getData();
                            dataStoreNOO = data;

                            if (dataStoreNOO.size()>=1){
                                TxtID.setText(dataStoreNOO.get(0).getCustomer_id());
                                InputName.setText(dataStoreNOO.get(0).getCustomer_name());
                                InputAddress.setText(dataStoreNOO.get(0).getAddress());
                                InputCity.setText(dataStoreNOO.get(0).getCity());
                                InputType.setText(dataStoreNOO.get(0).getTypes());
                                InputClass.setText(dataStoreNOO.get(0).getClasification());
                                InputContact.setText(dataStoreNOO.get(0).getContact_person());
                                InputContactLevel.setText(dataStoreNOO.get(0).getContact_person_level());
                                InputNPWP.setText(dataStoreNOO.get(0).getTax_id());
                                InputNIK.setText(dataStoreNOO.get(0).getCitizen_id());
                                InputPhoneNo.setText(dataStoreNOO.get(0).getHandphone());
                                InputWA.setText(dataStoreNOO.get(0).getWhatsapp_no());
                                InputEmail.setText(dataStoreNOO.get(0).getEmail());
                                InputCreditLimit.setText(dataStoreNOO.get(0).getCredit_limit());
                            }else{
                                Toast.makeText(Activity_NOOForm.this, "Belum ada slot NOO, silahkan hubungi admin", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                        }
                    }
                }else{
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_StoreNOO> call, Throwable t) {
                Toast.makeText(Activity_NOOForm.this, "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getTodayNonSecond() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getTodaySecond() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getTodayDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
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
                            Intent in = new Intent(Activity_NOOForm.this, Activity_NOO.class);
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
            builder.setMessage("Apakah anda yakin akan keluar dari menu NOO?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
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