package com.lapakkreatiflamongan.smdsforce.intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Channel;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Spinner;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreMaster;
import com.lapakkreatiflamongan.smdsforce.utils.Fn_DBHandler;
import cz.msebera.android.httpclient.Header;
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

public class Activity_Validation extends AppCompatActivity {
    Data_StoreMaster storeMaster;
    TextView txtQ1, txtQ2, txtQ3, txtQ4, txtQ5, txtQ6, txtStore, txtTitleNotes,txtQ4RemarkGeo, txtQ4GeoMst, txtQ4GeoAct, txtQ1Remark;
    RadioButton rBQ1T,rBQ1F,rBQ2T,rBQ2F,rBQ3T,rBQ3F,rBQ4T,rBQ4F,rBQ5T,rBQ5F,rBQ6T,rBQ6F;
    LinearLayout lyNotes,lyQ2,lyQ3,lyQ4,lyQ5,lyQ6,lyQ2Rev,lyQ3Rev,lyQ5Rev,lyQ6Rev;
    EditText inputNotes,inputQ2Rev,inputQ3Rev,inputQ5Rev,inputQ6Rev1,inputQ6Rev2;
    ImageView imgRefreshGeo,imgCamera;
    Button btnSave;

    private final String BASE_URL = "http://kakikupos.com:8081/";
    int TAKE_PHOTO_CODE_EXT = 99;
    private boolean isFill = false;
    private final String DB_ADDRESS="MASTER_ADDRESS";
    
    String LatMst = "0",LongMst = "0",LatAct = "0",LongAct = "0", startCheckInTime = "", stopCheckOutTime = "",checkDated = "";
    Spinner SpnKelurahan,SpnKota,SpnProvinsi,SpnDistrict;


    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVCODE = "usercode";
    private String BASE_URL_UPLOAD = "http://sfa.borwita.co.id:3000/api/upload/photo";

    String geoReverseAct = "-",uploadedImgStore = "-";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);

    List<Data_Channel> dataChannels = new ArrayList<Data_Channel>();
    String[] arrChannel = new String[1];

    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";

    String[] IdProvinsi,Provinsi,IdKabupaten,Kabupaten,IdKecamatan,Kecamatan,IdDesa,Desa,IdSegment,Segment;
    ArrayList<Data_Spinner> listProvince,listCity,listDistrict,listVillage;
    Spinner spnQ5Rev;


    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    private String dir = null,dirImageFile = "", photoFile = "";
    Fn_DBHandler dbaddress;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public ArrayList<File> getAllFilesInDir(File dir) {
        if (dir == null)
            return null;

        ArrayList<File> files = new ArrayList<>();

        Stack<File> dirlist = new Stack<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_validation);

        txtQ1       = findViewById(R.id.Validation_Q1);
        txtQ2       = findViewById(R.id.Validation_Q2);
        txtQ3       = findViewById(R.id.Validation_Q3);
        txtQ4       = findViewById(R.id.Validation_Q4);
        txtQ5       = findViewById(R.id.Validation_Q5);
        txtQ6       = findViewById(R.id.Validation_Q6);
        txtStore    = findViewById(R.id.Validation_Store);
        rBQ1F       = findViewById(R.id.Validation_Q1F);
        rBQ1T       = findViewById(R.id.Validation_Q1T);
        rBQ2F       = findViewById(R.id.Validation_Q2F);
        rBQ2T       = findViewById(R.id.Validation_Q2T);
        rBQ3F       = findViewById(R.id.Validation_Q3F);
        rBQ3T       = findViewById(R.id.Validation_Q3T);
        rBQ4F       = findViewById(R.id.Validation_Q4F);
        rBQ4T       = findViewById(R.id.Validation_Q4T);
        rBQ5F       = findViewById(R.id.Validation_Q5F);
        rBQ5T       = findViewById(R.id.Validation_Q5T);
        rBQ6F       = findViewById(R.id.Validation_Q6F);
        rBQ6T       = findViewById(R.id.Validation_Q6T);
        lyNotes     = findViewById(R.id.Validation_LyNotes);
        lyQ2     = findViewById(R.id.Validation_LyQ2);
        lyQ3     = findViewById(R.id.Validation_LyQ3);
        lyQ4     = findViewById(R.id.Validation_LyQ4);
        lyQ5     = findViewById(R.id.Validation_LyQ5);
        lyQ6     = findViewById(R.id.Validation_LyQ6);
        lyQ2Rev     = findViewById(R.id.Validation_LyQ2Rev);
        lyQ3Rev     = findViewById(R.id.Validation_LyQ3Rev);
        lyQ5Rev     = findViewById(R.id.Validation_LyQ5Rev);
        lyQ6Rev     = findViewById(R.id.Validation_LyQ6Rev);
        inputNotes  = findViewById(R.id.Validation_Notes);
        inputQ2Rev  = findViewById(R.id.Validation_Q2Rev);
        inputQ3Rev  = findViewById(R.id.Validation_Q3Rev);
        inputQ5Rev  = findViewById(R.id.Validation_Q5Rev);
        inputQ6Rev1  = findViewById(R.id.Validation_Q6Rev1);
        inputQ6Rev2  = findViewById(R.id.Validation_Q6Rev2);
        spnQ5Rev  = findViewById(R.id.Validation_Q5Spn);
        imgCamera  = findViewById(R.id.Validation_Q1Pic);
        txtTitleNotes = findViewById(R.id.Validation_TitleNotes);
        txtQ4RemarkGeo = findViewById(R.id.Validation_Q4Remark);
        imgRefreshGeo = findViewById(R.id.Validation_GeoRefresh);
        txtQ1Remark = findViewById(R.id.Validation_Q1Remark);
        btnSave = findViewById(R.id.Validation_Save);
        txtQ4GeoMst = findViewById(R.id.Validation_Q4GeoMst);
        txtQ4GeoAct = findViewById(R.id.Validation_Q4GeoAct);
        SpnProvinsi = findViewById(R.id.Validation_Q3RevSpn1);
        SpnKota = findViewById(R.id.Validation_Q3RevSpn2);
        SpnDistrict = findViewById(R.id.Validation_Q3RevSpn3);
        SpnKelurahan = findViewById(R.id.Validation_Q3RevSpn4);

        // First Initial
        Intent intent = getIntent();
        storeMaster = (Data_StoreMaster) intent.getSerializableExtra("data");
        startCheckInTime = getTodaySecond();
        checkDated = getTodayDate();
        getSupportActionBar().setTitle("Validasi Coverage");
        dir = getApplicationContext().getFilesDir() + "/picFolder/";
        symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");
        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);
        isFill = false;
        txtQ1Remark.setVisibility(View.GONE);
        imgCamera.setVisibility(View.GONE);
        lyQ2Rev.setVisibility(View.GONE);
        lyQ3Rev.setVisibility(View.GONE);
        lyQ5Rev.setVisibility(View.GONE);
        lyQ6Rev.setVisibility(View.GONE);
        inputNotes.setText(inputNotes.getText().toString().replaceAll("\\\\n", " "));
        txtTitleNotes.setText("Catatan :");
        if (storeMaster == null){

        }else{
            if (!storeMaster.getLatitude().contains(",")&&storeMaster.getLatitude().contains(".")){
                LatMst = storeMaster.getLatitude().trim();
            }
            if (!storeMaster.getLongitude().contains(",")&&storeMaster.getLongitude().contains(".")){
                LongMst = storeMaster.getLongitude().trim();
            }

            if (storeMaster.getStorecode().contains("/01/")){
                BASE_URL_UPLOAD = "http://sfa.borwita.co.id:3000/api/upload/photo";
            }else if(storeMaster.getStorecode().contains("/04/")){
                BASE_URL_UPLOAD = "http://sfa.borwita.co.id:3001/api/upload/photo";
            }else{
                BASE_URL_UPLOAD = "http://sfa.borwita.co.id:3003/api/upload/photo";
            }
        }

        txtStore.setText(storeMaster.getStorename()+" ("+storeMaster.getStorecode()+")");

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada).duration(300).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (uploadedImgStore.equals("-")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                            String date = dateFormat.format(new Date());
                            photoFile = "PicSPV_" + storeMaster.getStorecode().trim().replaceAll("/", "") + "_" + date + ".jpg";
                            dirImageFile = dir + photoFile;

                            ImagePicker.with(Activity_Validation.this)
                                    .crop()
                                    .cameraOnly()//Final image size will be less than 1 MB(Optional)
                                    .compress(400)
                                    .maxResultSize(1080, 1080)
                                    .saveDir(dir)//Final image resolution will be less than 1080 x 1080(Optional)
                                    .start(TAKE_PHOTO_CODE_EXT);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                            builder.setTitle("Detail Capture");

                            LinearLayout layout = new LinearLayout(Activity_Validation.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20, 15, 30, 5);

                            final TextView TxtLblNama = new TextView(Activity_Validation.this);
                            TxtLblNama.setText("Toko : " + storeMaster.getStorename());
                            TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                            TxtLblNama.setTextColor(Color.BLACK);

                            final ImageView Img = new ImageView(Activity_Validation.this);
                            Picasso.get().load(new File(dir+uploadedImgStore)).into(Img);
                            layout.addView(TxtLblNama, params);
                            layout.addView(Img, params);

                            builder.setView(layout);

                            // Set up the buttons
                            builder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.setNeutralButton("Foto Ulang", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                                    String date = dateFormat.format(new Date());
                                    photoFile = "PicSPV_" + storeMaster.getStorecode().trim().replaceAll("/", "") + "_" + date + ".jpg";
                                    dirImageFile = dir + photoFile;

                                    ImagePicker.with(Activity_Validation.this)
                                            .crop()
                                            .cameraOnly()//Final image size will be less than 1 MB(Optional)
                                            .compress(400)
                                            .maxResultSize(1080, 1080)
                                            .saveDir(dir)//Final image resolution will be less than 1080 x 1080(Optional)
                                            .start(TAKE_PHOTO_CODE_EXT);
                                }
                            });

                            builder.show();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(imgCamera);
            }
        });
        imgRefreshGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtQ4RemarkGeo.setText("Silahkan Tunggu, Sedang memperbarui lokasi. . .");
                txtQ4GeoAct.setText("Silahkan Tunggu, Sedang memperbarui lokasi. .");
                YoYo.with(Techniques.Tada).duration(300).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                        if (ActivityCompat.checkSelfPermission(Activity_Validation.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            String Msg = "Aplikasi Super Vision tidak bisa berjalan jika tidak bisa akses perizinan Telepon, Silahkan aktifkan izin untuk akses telepon";
                            new AlertDialog.Builder(Activity_Validation.this)
                                    .setTitle("Information")
                                    .setMessage(Msg)
                                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            Activity_Validation.this.finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                            String Msg = "GPS tidak aktif, silahkan aktifkan dahulu di setting!!!";
                            new AlertDialog.Builder(Activity_Validation.this)
                                    .setTitle("Information")
                                    .setMessage(Msg)
                                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                            Activity_Validation.this.finish();
                                            Activity_Validation.this.finishAffinity();
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            SmartLocation.with(Activity_Validation.this).location().oneFix().start(new OnLocationUpdatedListener() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onLocationUpdated(Location location) {
                                    // Add a marker in Sydney and move the camera
                                    if (location != null){
                                        LatAct = location.getLatitude()+"";
                                        LongAct = location.getLongitude()+"";
                                        int distance = (int) calcDistanceMeter(location.getLatitude()+"", location.getLongitude()+"");
                                        SmartLocation.with(Activity_Validation.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                                            @Override
                                            public void onAddressResolved(Location location, List<Address> list) {
                                                if (list.size()>0){
                                                    txtQ4GeoAct.setText("Actual Call : "+list.get(0).getAddressLine(0));
                                                    geoReverseAct = list.get(0).getAddressLine(0);
                                                }

                                                Location locMaster = new Location("");
                                                locMaster.setLongitude(Double.parseDouble(LongMst));
                                                locMaster.setLatitude(Double.parseDouble(LatMst));

                                                SmartLocation.with(Activity_Validation.this).geocoding().reverse(locMaster, new OnReverseGeocodingListener() {
                                                    @Override
                                                    public void onAddressResolved(Location location, List<Address> list) {
                                                        if (list.size()>0){
                                                            txtQ4GeoMst.setText("Master : "+list.get(0).getAddressLine(0));
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        txtQ4RemarkGeo.setText("Posisi anda saat ini berjarak "+formatter.format(distance)+" meter dari master geolocation toko");
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                                        builder.setTitle("Konfirmasi");
                                        builder.setMessage("Ops, Proses ambil lokasi gagal, klik icon refresh untuk mencoba lagi").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }

                                }
                            });
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(imgRefreshGeo);
            }
        });
        rBQ1F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                txtQ1Remark.setVisibility(View.VISIBLE);
                imgCamera.setVisibility(View.VISIBLE);
                isFill = true;
                if (uploadedImgStore.equals("-")){
                    YoYo.with(Techniques.Tada).duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(imgCamera);
                }
                if (isChecked){
                    lyQ2.setVisibility(View.GONE);
                    lyQ3.setVisibility(View.GONE);
                    lyQ4.setVisibility(View.GONE);
                    lyQ5.setVisibility(View.GONE);
                    lyQ6.setVisibility(View.GONE);
                    txtTitleNotes.setText("Catatan * (Wajib Diisi) :");
                    txtTitleNotes.setTextColor(getResources().getColor(R.color.colorRed));
                }else{
                    lyQ2.setVisibility(View.VISIBLE);
                    lyQ3.setVisibility(View.VISIBLE);
                    lyQ4.setVisibility(View.VISIBLE);
                    lyQ5.setVisibility(View.VISIBLE);
                    lyQ6.setVisibility(View.VISIBLE);
                    txtTitleNotes.setText("Catatan :");
                    txtTitleNotes.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });
        rBQ1T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFill = true;
                stopCheckOutTime = getTodaySecond();
                txtQ1Remark.setVisibility(View.VISIBLE);
                imgCamera.setVisibility(View.VISIBLE);
                if (uploadedImgStore.equals("-")){
                    YoYo.with(Techniques.Tada).duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(imgCamera);
                }
            }
        });
        rBQ2T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFill = true;
                stopCheckOutTime = getTodaySecond();
            }
        });
        rBQ2F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFill = true;
                stopCheckOutTime = getTodaySecond();
                if (isChecked){
                    lyQ2Rev.setVisibility(View.VISIBLE);
                }else{
                    lyQ2Rev.setVisibility(View.GONE);
                    inputQ2Rev.setText("");
                }
            }
        });
        rBQ3F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                if (isChecked){
                    lyQ3Rev.setVisibility(View.VISIBLE);
                }else{
                    lyQ3Rev.setVisibility(View.GONE);
                    inputQ3Rev.setText("");
                }
            }
        });
        rBQ3T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                isFill = true;
            }
        });
        rBQ4T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                isFill = true;
                inputQ2Rev.setText(inputQ2Rev.getText().toString().toUpperCase(Locale.getDefault()));
                inputQ3Rev.setText(inputQ3Rev.getText().toString().toUpperCase(Locale.getDefault()));
            }
        });
        rBQ4F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                isFill = true;
                inputQ2Rev.setText(inputQ2Rev.getText().toString().toUpperCase(Locale.getDefault()));
                inputQ3Rev.setText(inputQ3Rev.getText().toString().toUpperCase(Locale.getDefault()));
            }
        });
        rBQ5F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                if (isChecked){
                    lyQ5Rev.setVisibility(View.VISIBLE);
                    inputQ5Rev.setText(arrChannel[spnQ5Rev.getSelectedItemPosition()]+"");
                }else{
                    lyQ5Rev.setVisibility(View.GONE);
                    inputQ5Rev.setText("");
                }
            }
        });
        rBQ5T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                isFill = true;
            }
        });

        spnQ5Rev.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputQ5Rev.setText(arrChannel[spnQ5Rev.getSelectedItemPosition()]+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rBQ6F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                if (isChecked){
                    lyQ6Rev.setVisibility(View.VISIBLE);
                }else{
                    lyQ6Rev.setVisibility(View.GONE);
                    inputQ6Rev1.setText("");
                    inputQ6Rev2.setText("");
                }
            }
        });
        rBQ6T.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopCheckOutTime = getTodaySecond();
                isFill = true;
            }
        });

        SpannableStringBuilder strQ2 = new SpannableStringBuilder("Apakah nama toko "+storeMaster.getStorename()+" sudah sesuai dengan aktual?");
        strQ2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 17, storeMaster.getStorename().length()+17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQ2.setText(strQ2);

        SpannableStringBuilder strQ3 = new SpannableStringBuilder("Apakah alamat toko di "+storeMaster.getAddress()+" sudah sesuai dengan aktual?");
        strQ3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 22, storeMaster.getAddress().length()+22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQ3.setText(strQ3);

        SpannableStringBuilder strQ4 = new SpannableStringBuilder("Apakah posisi geolocation(latlong) toko pada "+storeMaster.getLatitude()+","+storeMaster.getLongitude()+" kurang dari 300 meter dari titik call anda?");
        strQ4.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 45, (storeMaster.getLatitude()+","+storeMaster.getLongitude()).toString().length()+45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQ4.setText(strQ4);

        SpannableStringBuilder strQ5 = new SpannableStringBuilder("Apakah segment toko "+storeMaster.getChanneldesc()+" sudah sesuai?");
        strQ5.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 20, storeMaster.getChanneldesc().length()+20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQ5.setText(strQ5);

        SpannableStringBuilder strQ6 = new SpannableStringBuilder("Apakah no telepon toko "+(storeMaster.getPhoneno().length()==0?'0':storeMaster.getPhoneno())+" dan no. whatsapp "+storeMaster.getWhatsappno()+" sudah sesuai?");
        strQ6.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 23, ((storeMaster.getPhoneno().length()==0?1:storeMaster.getPhoneno().length())+storeMaster.getWhatsappno().length())+40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQ6.setText(strQ6);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFill){
                    Toast.makeText(Activity_Validation.this, "Belum ada data yang disimpan, silahkan jawab dahulu pertanyaan diatas", Toast.LENGTH_SHORT).show();
                }else if (rBQ1F.isChecked()){
                    if(uploadedImgStore.equals("-")){
                        Toast.makeText(Activity_Validation.this, "Silahkan ambil 1 foto toko dahulu", Toast.LENGTH_SHORT).show();
                        YoYo.with(Techniques.Tada).duration(300).playOn(imgCamera);
                    }else if(inputNotes.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_Validation.this, "Mohon isi dahulu Catatan", Toast.LENGTH_SHORT).show();
                    }else{
                        new AlertDialog.Builder(Activity_Validation.this)
                                .setTitle("Information")
                                .setMessage("Apakah anda yakin akan simpan")
                                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Call<String> callInsertFalse = myAPi.insertValidationFalse(
                                                getPref(TAG_SPVCODE),storeMaster.getStorecode(),
                                                startCheckInTime,
                                                stopCheckOutTime,
                                                LatAct,
                                                LongAct,
                                                inputNotes.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                checkDated,
                                                uploadedImgStore,"0"
                                        );

                                        callInsertFalse.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if (response.isSuccessful()){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                                                    builder.setTitle("Konfirmasi");
                                                    builder.setCancelable(false);
                                                    builder.setMessage("Data validasi anda untuk toko "+storeMaster.getStorename()+" berhasil disimpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent intent1 = new Intent(Activity_Validation.this, Activity_Trip.class);
                                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent1);
                                                        }
                                                    }).show();
                                                }else{
                                                    Log.e("Error","Gagal Upload ");
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
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
                                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }else{
                    if(!rBQ1T.isChecked()&&!rBQ1F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 1", Toast.LENGTH_SHORT).show();
                    }else if(!rBQ2T.isChecked()&&!rBQ2F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 2", Toast.LENGTH_SHORT).show();
                    }else if(!rBQ3T.isChecked()&&!rBQ3F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 3", Toast.LENGTH_SHORT).show();
                    }else if(!rBQ4T.isChecked()&&!rBQ4F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 4", Toast.LENGTH_SHORT).show();
                    }else if(!rBQ5T.isChecked()&&!rBQ5F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 5", Toast.LENGTH_SHORT).show();
                    }else if(!rBQ6T.isChecked()&&!rBQ6F.isChecked()){
                        Toast.makeText(Activity_Validation.this, "Silahkan jawab dahulu pertanyaan nomor 6", Toast.LENGTH_SHORT).show();
                    }else if(rBQ2F.isChecked()&&inputQ2Rev.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu nama toko yang sesuai di nomor 2", Toast.LENGTH_SHORT).show();
                    }else if(rBQ3F.isChecked()&&inputQ3Rev.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu alamat toko yang sesuai di nomor 3", Toast.LENGTH_SHORT).show();
                    }else if(rBQ6F.isChecked()&&inputQ6Rev1.getText().toString().trim().length()<=5){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu nomor handphone yang sesuai dan lebih dari 6 digit", Toast.LENGTH_SHORT).show();
                    }else if(rBQ6F.isChecked()&&inputQ6Rev2.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu nomor whatsapp yang sesuai dan lebih dari 9 digit", Toast.LENGTH_SHORT).show();
                    }else if(rBQ6F.isChecked()&&!inputQ6Rev2.getText().toString().trim().substring(0,2).equals("62")){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu nomor whatsapp yang sesuai dan harus berawalan 62", Toast.LENGTH_SHORT).show();
                    }else if(rBQ6F.isChecked()&&(!inputQ6Rev1.getText().toString().trim().substring(0,1).equals("0")&&!inputQ6Rev1.getText().toString().trim().substring(0,1).equals("6")&&!inputQ6Rev1.getText().toString().trim().substring(0,1).equals("8"))){
                        Toast.makeText(Activity_Validation.this, "Silahkan isi dahulu nomor telepon yang sesuai dan harus berawalan 62,8 atau 0", Toast.LENGTH_SHORT).show();
                    }else if(uploadedImgStore.equals("-")) {
                        YoYo.with(Techniques.Tada).duration(300).playOn(imgCamera);
                        Toast.makeText(Activity_Validation.this, "Silahkan ambil 1 foto toko dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        inputQ2Rev.setText(inputQ2Rev.getText().toString().toUpperCase(Locale.getDefault()));
                        inputQ3Rev.setText(inputQ3Rev.getText().toString().toUpperCase(Locale.getDefault()));
                        new AlertDialog.Builder(Activity_Validation.this)
                                .setTitle("Information")
                                .setMessage("Apakah anda yakin akan simpan")
                                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        String isValid_Name = rBQ2T.isChecked()?"1":"0";
                                        String isValid_Address = rBQ3T.isChecked()?"1":"0";
                                        String isValid_Geolocation = rBQ4T.isChecked()?"1":"0";
                                        String isValid_Channel = rBQ5T.isChecked()?"1":"0";
                                        String isValid_Handphone = rBQ6T.isChecked()?"1":"0";

                                        Call<String> callInsertTrue = myAPi.insertValidation(
                                                getPref(TAG_SPVCODE),storeMaster.getStorecode(),
                                                startCheckInTime,
                                                stopCheckOutTime,
                                                LatAct,
                                                LongAct,
                                                inputNotes.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                checkDated,
                                                uploadedImgStore,"1",
                                                isValid_Name,
                                                isValid_Address,
                                                isValid_Geolocation,
                                                isValid_Channel,
                                                LongMst,LatMst,
                                                inputQ2Rev.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                inputQ3Rev.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                rBQ3T.isChecked()?"":SpnProvinsi.getSelectedItem().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                rBQ3T.isChecked()?"":SpnKota.getSelectedItem().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                rBQ3T.isChecked()?"":SpnDistrict.getSelectedItem().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                rBQ3T.isChecked()?"":SpnKelurahan.getSelectedItem().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                inputQ5Rev.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                inputQ6Rev1.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                inputQ6Rev2.getText().toString().replaceAll("\\\\n", " ").replace("'", "").replaceAll("\"", ""),
                                                isValid_Handphone
                                        );

                                        callInsertTrue.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if (response.isSuccessful()){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                                                    builder.setTitle("Konfirmasi");
                                                    builder.setCancelable(false);
                                                    builder.setMessage("Data validasi anda untuk toko "+storeMaster.getStorename()+" berhasil disimpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent intent1 = new Intent(Activity_Validation.this, Activity_Trip.class);
                                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent1);
                                                        }
                                                    }).show();
                                                }else{
                                                    Log.e("Error","Gagal Upload ");
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
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
                                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });

        //Logic
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String Msg = "Aplikasi Super Vision tidak bisa berjalan jika tidak bisa akses perizinan Telepon, Silahkan aktifkan izin untuk akses telepon";
            new AlertDialog.Builder(Activity_Validation.this)
                    .setTitle("Information")
                    .setMessage(Msg)
                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Activity_Validation.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            String Msg = "GPS tidak aktif, silahkan aktifkan dahulu di setting!!!";
            new AlertDialog.Builder(Activity_Validation.this)
                    .setTitle("Information")
                    .setMessage(Msg)
                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            Activity_Validation.this.finish();
                            Activity_Validation.this.finishAffinity();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            SmartLocation.with(Activity_Validation.this).location().oneFix().start(new OnLocationUpdatedListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onLocationUpdated(Location location) {
                    // Add a marker in Sydney and move the camera
                    if (location != null){
                        LatAct = location.getLatitude()+"";
                        LongAct = location.getLongitude()+"";
                        int distance = (int) calcDistanceMeter(location.getLatitude()+"", location.getLongitude()+"");
                        SmartLocation.with(Activity_Validation.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                            @Override
                            public void onAddressResolved(Location location, List<Address> list) {
                                if (list.size()>0){
                                    txtQ4GeoAct.setText("Actual Call : "+list.get(0).getAddressLine(0));
                                    geoReverseAct = list.get(0).getAddressLine(0);
                                }

                                Location locMaster = new Location("");
                                locMaster.setLongitude(Double.parseDouble(LongMst));
                                locMaster.setLatitude(Double.parseDouble(LatMst));

                                SmartLocation.with(Activity_Validation.this).geocoding().reverse(locMaster, new OnReverseGeocodingListener() {
                                    @Override
                                    public void onAddressResolved(Location location, List<Address> list) {
                                        if (list.size()>0){
                                            txtQ4GeoMst.setText("Master : "+list.get(0).getAddressLine(0));
                                        }
                                    }
                                });
                                txtQ4RemarkGeo.setText("Posisi anda saat ini berjarak "+formatter.format(distance)+" meter dari master geolocation toko");
                            }
                        });

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                        builder.setTitle("Konfirmasi");
                        builder.setMessage("Ops, Proses ambil lokasi gagal, klik icon refresh untuk mencoba lagi?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                }
            });
        }

        if (new File(dir).canRead() && new File(dir).canWrite()) {
            ArrayList<File> ListFile = getAllFilesInDir(new File(dir));
            for (int ii = 0; ii < ListFile.size(); ii++) {
                File file = new File(dir + ListFile.get(ii).getName());
                if (file.exists()) {
                    Calendar time = Calendar.getInstance();
                    time.add(Calendar.DAY_OF_YEAR, -2);
                    //I store the required attributes here and delete them
                    Date lastModified = new Date(file.lastModified());
                    if (lastModified.before(time.getTime())) {
                        file.delete();
                    }
                }
            }
        }

        dbaddress = new Fn_DBHandler(getApplicationContext(), DB_ADDRESS);
        final File dbFileAddress = new File(getApplicationContext().getFilesDir()+"/"+DB_ADDRESS);

        if(dbFileAddress.exists()){
            try{

                JSONObject JsNoo = null;
                JSONArray jNoo;

                // Get Provinsi
                JsNoo = dbaddress.GetAddressProvinsi();
                jNoo = JsNoo.getJSONArray("ADDRESS");

                IdProvinsi = new String[jNoo.length()];
                Provinsi = new String[jNoo.length()];

                for (int i=0;i<jNoo.length();i++){
                    JSONObject c = jNoo.getJSONObject(i);

                    IdProvinsi[i] = c.getString("idprovinsi");
                    Provinsi[i] = c.getString("provinsi");
                }

                listProvince = new ArrayList<Data_Spinner>();
                listCity = new ArrayList<Data_Spinner>();
                listDistrict = new ArrayList<Data_Spinner>();
                listVillage = new ArrayList<Data_Spinner>();
                for (int j=0;j<IdProvinsi.length;j++){
                    listProvince.add(new Data_Spinner(IdProvinsi[j],Provinsi[j]));
                }

                ArrayAdapter adapterProv = new ArrayAdapter(Activity_Validation.this,android.R.layout.simple_list_item_1,Provinsi);
                adapterProv.setDropDownViewResource(android.R.layout.simple_list_item_1);
                SpnProvinsi.setAdapter(adapterProv);

                SpnProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            JSONObject JsNoo = null;
                            JsNoo = dbaddress.GetAddressKabupaten(IdProvinsi[i]);
                            JSONArray jNoo = JsNoo.getJSONArray("ADDRESS");

                            IdKabupaten = new String[jNoo.length()];
                            Kabupaten = new String[jNoo.length()];

                            for (int i2 = 0; i2 < jNoo.length(); i2++) {
                                JSONObject c2 = jNoo.getJSONObject(i2);

                                IdKabupaten[i2] = c2.getString("idkabupaten");
                                Kabupaten[i2] = c2.getString("kabupaten");
                            }

                            for (int j = 0; j < IdKabupaten.length; j++) {
                                listCity.add(new Data_Spinner(IdKabupaten[j], Kabupaten[j]));
                            }

                            ArrayAdapter adapterCity = new ArrayAdapter(Activity_Validation.this,android.R.layout.simple_list_item_1,Kabupaten);
                            adapterCity.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            SpnKota.setAdapter(adapterCity);

                            SpnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        JSONObject JsNoo = null;
                                        JsNoo = dbaddress.GetAddressKecamatan(IdProvinsi[SpnProvinsi.getSelectedItemPosition()],IdKabupaten[i]);
                                        JSONArray jNoo = JsNoo.getJSONArray("ADDRESS");

                                        IdKecamatan = new String[jNoo.length()];
                                        Kecamatan = new String[jNoo.length()];

                                        for (int i2 = 0; i2 < jNoo.length(); i2++) {
                                            JSONObject c2 = jNoo.getJSONObject(i2);

                                            IdKecamatan[i2] = c2.getString("idkecamatan");
                                            Kecamatan[i2] = c2.getString("kecamatan");
                                        }

                                        for (int j = 0; j < IdKecamatan.length; j++) {
                                            listDistrict.add(new Data_Spinner(IdKecamatan[j], Kecamatan[j]));
                                        }

                                        ArrayAdapter adapterDistrict = new ArrayAdapter(Activity_Validation.this,android.R.layout.simple_list_item_1,Kecamatan);
                                        adapterDistrict.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                        SpnDistrict.setAdapter(adapterDistrict);

                                        SpnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                try {

                                                    JSONObject JsNoo = null;

                                                    JsNoo = dbaddress.GetAddressDesa(IdProvinsi[SpnProvinsi.getSelectedItemPosition()],IdKabupaten[SpnKota.getSelectedItemPosition()],IdKecamatan[i]);

                                                    JSONArray jNoo = JsNoo.getJSONArray("ADDRESS");

                                                    IdDesa = new String[jNoo.length()];
                                                    Desa = new String[jNoo.length()];

                                                    for (int i2 = 0; i2 < jNoo.length(); i2++) {
                                                        JSONObject c2 = jNoo.getJSONObject(i2);

                                                        IdDesa[i2] = c2.getString("iddesa");
                                                        Desa[i2] = c2.getString("desa");
                                                    }

                                                    for (int j = 0; j < IdDesa.length; j++) {
                                                        listVillage.add(new Data_Spinner(IdDesa[j], Desa[j]));
                                                    }

                                                    ArrayAdapter adapterVillage = new ArrayAdapter(Activity_Validation.this,android.R.layout.simple_list_item_1,Desa);
                                                    adapterVillage.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                                    SpnKelurahan.setAdapter(adapterVillage);

                                                }catch (JSONException e){
                                                    Toast.makeText(getApplicationContext(), "Error DB " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });

                                    }catch (JSONException e){
                                        Toast.makeText(getApplicationContext(), "Error DB " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "Error DB " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            catch (JSONException e){
                Toast.makeText(getApplicationContext(), "Error DB " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    public float calcDistanceMeter(String LatAct, String LongAct){
        Location master = new Location("");

        master.setLatitude(Float.parseFloat(storeMaster.getLatitude()==""?"0":storeMaster.getLatitude()));
        master.setLongitude(Float.parseFloat(storeMaster.getLongitude()==""?"0":storeMaster.getLongitude()));

        Location call = new Location("");
        call.setLatitude(Float.parseFloat(LatAct));
        call.setLongitude(Float.parseFloat(LongAct));

        float distanceInMeters = master.distanceTo(call);
        return distanceInMeters;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE_EXT) {
            if (resultCode == RESULT_OK) {
                String filePath = data.getData().toString();
                String[] fileNameArr = filePath.split("/");
                String fileName = "-";
                if (fileNameArr.length>0){
                    fileName = fileNameArr[fileNameArr.length-1];

                    File fileSource = new File(dir+fileName);
                    File fileDest = new File(dirImageFile);
                    fileSource.renameTo(fileDest);
                }


                try {
                    int inWidth = 800;
                    int inHeight = 600;
                    int dstWidth = 800, dstHeight = 600;

                    // decode image size (decode metadata only, not the whole image)
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    // decode full image pre-resized
                    InputStream inputFileStream = new FileInputStream(dirImageFile);
                    options = new BitmapFactory.Options();
                    // calc rought re-size (this is no exact resize)
                    options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
                    //compress
                    Bitmap roughBitmap = BitmapFactory.decodeStream(inputFileStream, null, options);

                    Bitmap mutableBitmap = roughBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.YELLOW);
                    paint.setTextSize(16f);

                    Rect rectText = new Rect();
                    paint.getTextBounds(getTodayNonSecond(), 0, 11, rectText);

                    int x = 10;
                    int y = (mutableBitmap.getHeight() - (rectText.height() + 10));

                    canvas.drawText(geoReverseAct, x, y-35, paint);
                    canvas.drawText(LatAct+", " +LongAct, x, y-20, paint);
                    canvas.drawText(getTodayNonSecond(), x, y-4, paint);

                    // save image
                    try {
                        FileOutputStream out = new FileOutputStream(dirImageFile);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

                        final Dialog dialog2 = new Dialog(Activity_Validation.this);
                        dialog2.setContentView(R.layout.d_logindownload);
                        dialog2.setCancelable(false);

                        final TextView TxtStatus = (TextView) dialog2.findViewById(R.id.Login_DStatus);
                        TxtStatus.setText("Mohon Tunggu. . ");

                        dialog2.show();
                        //
                        //Upload Image
                        File myFile = new File(dirImageFile);
                        RequestParams params = new RequestParams();
                        try {
                            params.put("uploaded_file", myFile);
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.post(BASE_URL_UPLOAD, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                                    Log.e("Sukses", "Sukses Force Upload Foto Toko");
                                    uploadedImgStore = photoFile;
                                    Picasso.get().load(new File(dirImageFile)).into(imgCamera);
                                    dialog2.dismiss();
                                    txtQ1Remark.setText("Anda bisa melakukan foto ulang dgn klik foto disamping");
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.e("Error","Gagal Upload ");
                                    dialog2.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Validation.this);
                                    switch (statusCode) {
                                        case 404:
                                            builder.setTitle("Konfirmasi");
                                            builder.setMessage("Ops, Gagal upload Foto "+ERROR_404+", Silahkan ambil ulang foto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                            break;
                                        case 500:
                                            builder.setTitle("Konfirmasi");
                                            builder.setMessage("Ops, Gagal upload Foto "+ERROR_500+", Silahkan ambil ulang foto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                            break;
                                        case 502:
                                            builder.setTitle("Konfirmasi");
                                            builder.setMessage("Ops, Gagal upload Foto "+ERROR_502+", Silahkan ambil ulang foto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                            break;
                                        default:
                                            builder.setTitle("Konfirmasi");
                                            builder.setMessage("Ops, Gagal upload Foto "+ERROR_500+", Silahkan ambil ulang foto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                            break;
                                    }
                                }
                            });

                        } catch(FileNotFoundException e) {
                            Log.e("Error","File Not Found");
                            dialog2.dismiss();
                        }

                    } catch (Exception e) {
                        Log.e("Hello", "Error Get File"+e.getMessage(), e);
                    }
                } catch (IOException e) {
                    Log.e("Hello", "Error Proseccing"+e.getMessage(), e);
                }
            }
            else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent in = new Intent(Activity_Validation.this, Activity_Trip.class);
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
            builder.setMessage("Apakah anda yakin akan keluar dari validasi toko?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
    }
}