package com.lapakkreatiflamongan.smdsforce.intent;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

public class Activity_NewVisit extends AppCompatActivity {
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
    ImageView imgCamera,imgRefresh;
    TextView txtSales,txtLocation,txtGeoReverse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_newvisit);

        getSupportActionBar().setTitle(getPref(TAG_CUSTOMERNAME));

        dir = getApplicationContext().getFilesDir() + "/picFolder/";

        imgCamera  = findViewById(R.id.NewVisit_Photo);
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
                            photoFile = "PicVisit_" + getPref(TAG_SPVCODE).trim().replaceAll("/", "") + "_" + date + ".jpg";
                            dirImageFile = dir + photoFile;

                            ImagePicker.with(Activity_NewVisit.this)
                                    .crop()
                                    .cameraOnly()//Final image size will be less than 1 MB(Optional)
                                    .compress(400)
                                    .maxResultSize(1080, 1080)
                                    .saveDir(dir)//Final image resolution will be less than 1080 x 1080(Optional)
                                    .start(TAKE_PHOTO_CODE_EXT);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NewVisit.this);
                            builder.setTitle("Detail Capture");

                            LinearLayout layout = new LinearLayout(Activity_NewVisit.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20, 15, 30, 5);

                            final TextView TxtLblNama = new TextView(Activity_NewVisit.this);
                            TxtLblNama.setText("Sales : " + getPref(TAG_SPVNAME));
                            TxtLblNama.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                            TxtLblNama.setTextColor(Color.BLACK);

                            final ImageView Img = new ImageView(Activity_NewVisit.this);
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
                                    photoFile = "PicVisit_" + getPref(TAG_SPVCODE).trim().replaceAll("/", "") + "_" + date + ".jpg";
                                    dirImageFile = dir + photoFile;

                                    ImagePicker.with(Activity_NewVisit.this)
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

        txtLocation = findViewById(R.id.NewVisit_Location);
        txtGeoReverse = findViewById(R.id.NewVisit_GeoReverse);
        txtSales = findViewById(R.id.NewVisit_Sales);
        txtSales.setText(getPref(TAG_SPVNAME));
        imgRefresh = findViewById(R.id.NewVisit_ImgRefresh);

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
                                            Toast.makeText(Activity_NewVisit.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Activity_NewVisit.this, "Silahkan cek kembali paket internet dan pastikan GPS anda menyala", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnSave = findViewById(R.id.NewVisit_Save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidPhoto<=0){
                    Toast.makeText(Activity_NewVisit.this,"Silahkan ambil foto dahulu",Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(Activity_NewVisit.this)
                            .setTitle("Information")
                            .setMessage("Apakah anda yakin akan simpan")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            Call<String> callInsert = myAPi.insertVisit(
                                                    getPref(TAG_SPVCODE),
                                                    getPref(TAG_CUSTOMERID),
                                                    getPref(TAG_LONGITUDE),
                                                    getPref(TAG_LATITUDE),
                                                    getPref(TAG_GEOREVERSE),
                                                    photoFile
                                            );

                                            callInsert.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful()){
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NewVisit.this);
                                                        builder.setTitle("Konfirmasi");
                                                        builder.setCancelable(false);
                                                        builder.setMessage("Data kunjungan "+getPref(TAG_SPVCODE)+" berhasil disimpan").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                Intent intent1 = new Intent(Activity_NewVisit.this, Activity_VisitMenu.class);
                                                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent1);
                                                            }
                                                        }).show();
                                                    }else{
                                                        Log.e("Error","Gagal Upload ");
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NewVisit.this);
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
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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

                    canvas.drawText(getTodayNonSecond(), x, y-4, paint);

                    // save image
                    try {
                        FileOutputStream out = new FileOutputStream(dirImageFile);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

                        final Dialog dialog2 = new Dialog(Activity_NewVisit.this);
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
                            client.post(BASE_URL+"uploadPhoto", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                                    Log.e("Sukses", "Sukses Force Upload Foto Toko");
                                    uploadedImgStore = photoFile;
                                    Picasso.get().load(new File(dirImageFile)).into(imgCamera);
                                    dialog2.dismiss();
                                    isValidPhoto = 1;
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.e("Error","Gagal Upload ");
                                    dialog2.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_NewVisit.this);
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
                            Intent in = new Intent(Activity_NewVisit.this, Activity_VisitMenu.class);
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
            builder.setMessage("Apakah anda yakin akan keluar dari menu New Trip?").setPositiveButton("Ya", dialogClickListener)
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