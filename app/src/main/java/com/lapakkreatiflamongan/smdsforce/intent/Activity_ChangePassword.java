package com.lapakkreatiflamongan.smdsforce.intent;

import static android.view.View.GONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ChangePassword;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_ChangePassword extends AppCompatActivity {
    long            counterTime = 60000;
    CountDownTimer  countDownTimer;
    TextView        txtCounter;
    Button          btnResendOTP,btnProcess,btnSave;
    int             resendCount = 0;
    EditText        inputOldPassword,inputNewPassword,inputNewPasswordAgain,inputOTP;
    String          randomOTP;
    private final String TAG_PREF       = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVCODE    = "usercode";
    LinearLayout    lyOTP;
    private String BASE_URL = "http://sfa.borwita.co.id/SFA_PG/";
    Col_ChangePassword colChangePassword;
    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_504 = "504 Gateway Time Out";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_408 = "408 Request Time Out";
    private final String ERROR_502 = "502 Bad Gateway";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_change_password);

        randomOTP = getRandomOTP(("0000"+getPref(TAG_SPVCODE).replaceAll("[^\\d]", "")).substring(getPref(TAG_SPVCODE).length()-4));

        txtCounter          = findViewById(R.id.ChangePassword_TxtCounter);
        btnResendOTP        = findViewById(R.id.ChangePassword_BtnResend);
        btnProcess          = findViewById(R.id.ChangePassword_BtnProcess);
        btnSave             = findViewById(R.id.ChangePassword_BtnSubmit);
        inputNewPassword    = findViewById(R.id.ChangePassword_NewPassword);
        inputNewPasswordAgain = findViewById(R.id.ChangePassword_NewPasswordAgain);
        inputOldPassword    = findViewById(R.id.ChangePassword_OldPassword);
        inputOTP            = findViewById(R.id.ChangePassword_InputOTP);
        lyOTP               = findViewById(R.id.ChangePassword_LyOTP);

        btnSave.setVisibility(GONE);
        btnProcess.setVisibility(View.VISIBLE);
        lyOTP.setVisibility(GONE);


        if (getPref(TAG_SPVCODE).contains("/01/")){
            BASE_URL = "http://sfa.borwita.co.id/SFA_PG/";
        }else if (getPref(TAG_SPVCODE).contains("/04/")){
            BASE_URL = "http://sfa.borwita.co.id/SFA_CERES/";
        }else {
            BASE_URL = "http://sfa.borwita.co.id/SFA_OMEGA/";
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .cache(null)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final API_SFA myAPI = retrofit.create(API_SFA.class);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputOldPassword.getText().toString().trim().length()<=0){
                    Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang lama", Toast.LENGTH_SHORT).show();
                    inputOldPassword.requestFocus();
                }else if (inputNewPassword.getText().toString().trim().length()<=0){
                    Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru", Toast.LENGTH_SHORT).show();
                    inputNewPassword.requestFocus();
                }else if (inputNewPassword.getText().toString().trim().length()<=5){
                    Toast.makeText(Activity_ChangePassword.this, "Silahkan isi panjang password 6 karakter atau lebih", Toast.LENGTH_SHORT).show();
                    inputNewPassword.requestFocus();
                } else if (inputNewPasswordAgain.getText().toString().trim().length()<=0){
                    Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru sekali lagi", Toast.LENGTH_SHORT).show();
                    inputNewPasswordAgain.requestFocus();
                }else if (!inputNewPasswordAgain.getText().toString().equals(inputNewPassword.getText().toString())){
                    Toast.makeText(Activity_ChangePassword.this, "Inputan password yang baru tidak sama", Toast.LENGTH_SHORT).show();
                    inputNewPasswordAgain.requestFocus();
                }else{

                    Call<Col_ChangePassword> call = myAPI.requestPassword(getPref(TAG_SPVCODE),"send_email",randomOTP,inputNewPassword.getText().toString().trim(),inputOldPassword.getText().toString().trim());
                    call.enqueue(new Callback<Col_ChangePassword>() {
                        @Override
                        public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                            if (response.isSuccessful()){
                                colChangePassword = response.body();

                                if (colChangePassword == null){
                                    new AlertDialog.Builder(Activity_ChangePassword.this)
                                            .setTitle("Warning")
                                            .setMessage("Opps, Response Null")
                                            .setCancelable(false)
                                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }else{
                                    if (colChangePassword.getStatus().equals("success")){
                                        new AlertDialog.Builder(Activity_ChangePassword.this)
                                                .setTitle("Information")
                                                .setCancelable(false)
                                                .setMessage(colChangePassword.getMessage())
                                                .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                        btnSave.setVisibility(View.VISIBLE);
                                                        btnProcess.setVisibility(GONE);
                                                        lyOTP.setVisibility(View.VISIBLE);
                                                        countDownTimer = new CountDownTimer(counterTime,1000) {
                                                            @Override
                                                            public void onTick(long millisUntilFinished) {
                                                                String counter = DateUtils.formatElapsedTime(millisUntilFinished/1000);
                                                                txtCounter.setText("Kirim ulang OTP setelah "+counter);
                                                                btnResendOTP.setVisibility(GONE);
                                                            }

                                                            @Override
                                                            public void onFinish() {
                                                                btnResendOTP.setVisibility(View.VISIBLE);
                                                                txtCounter.setVisibility(GONE);
                                                            }
                                                        }.start();

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .show();
                                    }else{
                                        new AlertDialog.Builder(Activity_ChangePassword.this)
                                                .setTitle("Warning")
                                                .setCancelable(false)
                                                .setMessage(""+colChangePassword.getMessage())
                                                .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .show();
                                    }
                                }

                            }else{
                                String msg;
                                switch (response.code()) {
                                    case 404:
                                        msg = "Ambil Data Gagal : "+ERROR_404;
                                        break;
                                    case 408:
                                        msg = "Ambil Data Gagal : "+ERROR_408;
                                        break;
                                    case 500:
                                        msg = "Ambil Data Gagal : "+ERROR_500;
                                        break;
                                    case 504:
                                        msg = "Ambil Data Gagal : "+ERROR_504;
                                        break;
                                    case 502:
                                        msg = "Ambil Data Gagal : "+ERROR_502;
                                        break;
                                    default:
                                        msg = "Ambil Data Gagal : "+ERROR_408;
                                        break;
                                }
                                new AlertDialog.Builder(Activity_ChangePassword.this)
                                        .setTitle("Warning")
                                        .setCancelable(false)
                                        .setMessage(msg)
                                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Col_ChangePassword> call, Throwable t) {
                            String msg = "Tidak ada koneksi internet, Silahkan cek kondisi paket data anda";
                            new AlertDialog.Builder(Activity_ChangePassword.this)
                                    .setTitle("Warning")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Activity_ChangePassword.this,Activity_MainMenu.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                    });
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (randomOTP.equals(inputOTP.getText().toString().trim())){
                    if (inputOldPassword.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang lama", Toast.LENGTH_SHORT).show();
                        inputOldPassword.requestFocus();
                    }else if (inputNewPassword.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru", Toast.LENGTH_SHORT).show();
                        inputNewPassword.requestFocus();
                    } else if (inputNewPasswordAgain.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru sekali lagi", Toast.LENGTH_SHORT).show();
                        inputNewPasswordAgain.requestFocus();
                    }else if (!inputNewPasswordAgain.getText().toString().equals(inputNewPassword.getText().toString())){
                        Toast.makeText(Activity_ChangePassword.this, "Inputan password yang baru tidak sama", Toast.LENGTH_SHORT).show();
                        inputNewPasswordAgain.requestFocus();
                    }else{

                        Call<Col_ChangePassword> call = myAPI.requestPassword(getPref(TAG_SPVCODE),"save",randomOTP,inputNewPassword.getText().toString().trim(),inputOldPassword.getText().toString().trim());
                        call.enqueue(new Callback<Col_ChangePassword>() {
                            @Override
                            public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                                if (response.isSuccessful()){
                                    colChangePassword = response.body();

                                    if (colChangePassword == null){
                                        new AlertDialog.Builder(Activity_ChangePassword.this)
                                                .setTitle("Warning")
                                                .setMessage("Opps, Response Null")
                                                .setCancelable(false)
                                                .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .show();
                                    }else{
                                        if (colChangePassword.getStatus().equals("success")){
                                            new AlertDialog.Builder(Activity_ChangePassword.this)
                                                    .setTitle("Information")
                                                    .setCancelable(false)
                                                    .setMessage(colChangePassword.getMessage()+"")
                                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(Activity_ChangePassword.this,Activity_Settings.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .show();
                                        }else{
                                            new AlertDialog.Builder(Activity_ChangePassword.this)
                                                    .setTitle("Warning")
                                                    .setCancelable(false)
                                                    .setMessage(""+colChangePassword.getMessage())
                                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .show();
                                        }
                                    }

                                }else{
                                    String msg;
                                    switch (response.code()) {
                                        case 404:
                                            msg = "Ambil Data Gagal : "+ERROR_404;
                                            break;
                                        case 408:
                                            msg = "Ambil Data Gagal : "+ERROR_408;
                                            break;
                                        case 500:
                                            msg = "Ambil Data Gagal : "+ERROR_500;
                                            break;
                                        case 504:
                                            msg = "Ambil Data Gagal : "+ERROR_504;
                                            break;
                                        case 502:
                                            msg = "Ambil Data Gagal : "+ERROR_502;
                                            break;
                                        default:
                                            msg = "Ambil Data Gagal : "+ERROR_408;
                                            break;
                                    }
                                    new AlertDialog.Builder(Activity_ChangePassword.this)
                                            .setTitle("Warning")
                                            .setCancelable(false)
                                            .setMessage(msg)
                                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Col_ChangePassword> call, Throwable t) {
                                String msg = "Tidak ada koneksi internet, Silahkan cek kondisi paket data anda";
                                new AlertDialog.Builder(Activity_ChangePassword.this)
                                        .setTitle("Warning")
                                        .setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Activity_ChangePassword.this,Activity_MainMenu.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        });
                    }
                }else{
                    String msg = "OTP yang anda inputkan tidak sesuai, silahkan cek email anda.";
                    new AlertDialog.Builder(Activity_ChangePassword.this)
                            .setTitle("Warning")
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            }
        });

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendCount<=3){
                    txtCounter.setVisibility(View.VISIBLE);
                    Toast.makeText(Activity_ChangePassword.this, "OTP telah dikirimkan ulang ke alamat email ", Toast.LENGTH_SHORT).show();
                    btnResendOTP.setVisibility(GONE);

                    resendCount = resendCount + 1;
                    counterTime = resendCount*300000;
                    countDownTimer = new CountDownTimer(counterTime,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String counter = DateUtils.formatElapsedTime(millisUntilFinished/1000);
                            txtCounter.setText("Kirim ulang OTP setelah "+counter);
                            btnResendOTP.setVisibility(GONE);
                        }

                        @Override
                        public void onFinish() {
                            btnResendOTP.setVisibility(View.VISIBLE);
                            txtCounter.setVisibility(GONE);
                        }
                    }.start();

                    if (inputOldPassword.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang lama", Toast.LENGTH_SHORT).show();
                        inputOldPassword.requestFocus();
                    }else if (inputNewPassword.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru", Toast.LENGTH_SHORT).show();
                        inputNewPassword.requestFocus();
                    } else if (inputNewPasswordAgain.getText().toString().trim().length()<=0){
                        Toast.makeText(Activity_ChangePassword.this, "Silahkan isi dahulu password yang baru sekali lagi", Toast.LENGTH_SHORT).show();
                        inputNewPasswordAgain.requestFocus();
                    }else if (!inputNewPasswordAgain.getText().toString().equals(inputNewPassword.getText().toString())){
                        Toast.makeText(Activity_ChangePassword.this, "Inputan password yang baru tidak sama", Toast.LENGTH_SHORT).show();
                        inputNewPasswordAgain.requestFocus();
                    }else{

                        Call<Col_ChangePassword> call = myAPI.requestPassword(getPref(TAG_SPVCODE),"send_email",randomOTP,inputNewPassword.getText().toString().trim(),inputOldPassword.getText().toString().trim());
                        call.enqueue(new Callback<Col_ChangePassword>() {
                            @Override
                            public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                                if (response.isSuccessful()){
                                    colChangePassword = response.body();

                                    if (colChangePassword == null){
                                        new AlertDialog.Builder(Activity_ChangePassword.this)
                                                .setTitle("Warning")
                                                .setMessage("Opps, Response Null")
                                                .setCancelable(false)
                                                .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .show();
                                    }else{
                                        if (colChangePassword.getStatus().equals("success")){
                                            new AlertDialog.Builder(Activity_ChangePassword.this)
                                                    .setTitle("Information")
                                                    .setCancelable(false)
                                                    .setMessage(colChangePassword.getMessage()+"")
                                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .show();
                                        }else{
                                            new AlertDialog.Builder(Activity_ChangePassword.this)
                                                    .setTitle("Warning")
                                                    .setCancelable(false)
                                                    .setMessage(""+colChangePassword.getMessage())
                                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .show();
                                        }
                                    }

                                }else{
                                    String msg;
                                    switch (response.code()) {
                                        case 404:
                                            msg = "Ambil Data Gagal : "+ERROR_404;
                                            break;
                                        case 408:
                                            msg = "Ambil Data Gagal : "+ERROR_408;
                                            break;
                                        case 500:
                                            msg = "Ambil Data Gagal : "+ERROR_500;
                                            break;
                                        case 504:
                                            msg = "Ambil Data Gagal : "+ERROR_504;
                                            break;
                                        case 502:
                                            msg = "Ambil Data Gagal : "+ERROR_502;
                                            break;
                                        default:
                                            msg = "Ambil Data Gagal : "+ERROR_408;
                                            break;
                                    }
                                    new AlertDialog.Builder(Activity_ChangePassword.this)
                                            .setTitle("Warning")
                                            .setCancelable(false)
                                            .setMessage(msg)
                                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Col_ChangePassword> call, Throwable t) {
                                String msg = "Tidak ada koneksi internet, Silahkan cek kondisi paket data anda";
                                new AlertDialog.Builder(Activity_ChangePassword.this)
                                        .setTitle("Warning")
                                        .setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Activity_ChangePassword.this,Activity_MainMenu.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        });
                    }

                }else{
                    Toast.makeText(Activity_ChangePassword.this, "Pengiriman ulang OTP maksimal hanya 3x ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getRandomOTP(String key){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");
        String formattedDate = df.format(c.getTime());
        int temp = Integer.parseInt(formattedDate)+Integer.parseInt(key);
        String result = (temp+"");
        return result.substring(result.length()-4);
    }

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }
}