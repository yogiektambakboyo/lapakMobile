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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ChangePassword;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_ResetPassword extends AppCompatActivity {
    long            counterTime = 60000;
    CountDownTimer  countDownTimer;
    TextView        txtCounter;
    Button          btnResendOTP,btnProcess,btnSave;
    int             resendCount = 0;
    EditText        inputSPVCode,inputEmail,inputOTP;
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
        setContentView(R.layout.p_reset_password);

        txtCounter          = findViewById(R.id.ResetPassword_TxtCounter);
        btnResendOTP        = findViewById(R.id.ResetPassword_BtnResend);
        btnProcess          = findViewById(R.id.ResetPassword_BtnProcess);
        btnSave             = findViewById(R.id.ResetPassword_BtnSubmit);
        inputEmail    = findViewById(R.id.ResetPassword_Email);
        inputSPVCode    = findViewById(R.id.ResetPassword_SPVCode);
        inputOTP            = findViewById(R.id.ResetPassword_InputOTP);
        lyOTP               = findViewById(R.id.ResetPassword_LyOTP);

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
                if (inputSPVCode.getText().toString().trim().length()<=4){
                    Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu kode SPV/ASM/RSM/HoS yang valid", Toast.LENGTH_SHORT).show();
                    inputSPVCode.requestFocus();
                }else if (inputEmail.getText().toString().trim().length()<=0||!inputEmail.getText().toString().trim().contains("@")){
                    Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu email yang valid", Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                }else{

                    randomOTP = getRandomOTP(("0000"+inputSPVCode.getText().toString().replaceAll("[^\\d]", "")).substring(inputSPVCode.getText().toString().replaceAll("[^\\d]", "").length()-4));

                    Call<Col_ChangePassword> call = myAPI.requestResetPassword(inputSPVCode.getText().toString(),"send_email",randomOTP,inputEmail.getText().toString().trim());
                    call.enqueue(new Callback<Col_ChangePassword>() {
                        @Override
                        public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                            if (response.isSuccessful()){
                                colChangePassword = response.body();

                                if (colChangePassword == null){
                                    new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                        new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                        new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                new AlertDialog.Builder(Activity_ResetPassword.this)
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
                            new AlertDialog.Builder(Activity_ResetPassword.this)
                                    .setTitle("Warning")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Activity_ResetPassword.this,Activity_Login.class);
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
                    if (inputSPVCode.getText().toString().trim().length()<=4){
                        Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu kode SPV/ASM/RSM/HoS yang valid", Toast.LENGTH_SHORT).show();
                        inputSPVCode.requestFocus();
                    }else if (inputEmail.getText().toString().trim().length()<=0||!inputEmail.getText().toString().trim().contains("@")){
                        Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu email yang valid", Toast.LENGTH_SHORT).show();
                        inputEmail.requestFocus();
                    }else{

                        Call<Col_ChangePassword> call = myAPI.requestResetPassword(inputSPVCode.getText().toString(),"save",randomOTP,inputEmail.getText().toString().trim());
                        call.enqueue(new Callback<Col_ChangePassword>() {
                            @Override
                            public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                                if (response.isSuccessful()){
                                    colChangePassword = response.body();

                                    if (colChangePassword == null){
                                        new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                            new AlertDialog.Builder(Activity_ResetPassword.this)
                                                    .setTitle("Information")
                                                    .setCancelable(false)
                                                    .setMessage(colChangePassword.getMessage()+"")
                                                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(Activity_ResetPassword.this,Activity_Login.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .show();
                                        }else{
                                            new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                    new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                new AlertDialog.Builder(Activity_ResetPassword.this)
                                        .setTitle("Warning")
                                        .setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Activity_ResetPassword.this,Activity_Login.class);
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
                    new AlertDialog.Builder(Activity_ResetPassword.this)
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
                    Toast.makeText(Activity_ResetPassword.this, "OTP telah dikirimkan ulang ke alamat email ", Toast.LENGTH_SHORT).show();
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

                    if (inputSPVCode.getText().toString().trim().length()<=4){
                        Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu kode SPV/ASM/RSM/HoS yang valid", Toast.LENGTH_SHORT).show();
                        inputSPVCode.requestFocus();
                    }else if (inputEmail.getText().toString().trim().length()<=0||!inputEmail.getText().toString().trim().contains("@")){
                        Toast.makeText(Activity_ResetPassword.this, "Silahkan isi dahulu email yang valid", Toast.LENGTH_SHORT).show();
                        inputEmail.requestFocus();
                    }else{
                        Call<Col_ChangePassword> call = myAPI.requestResetPassword(inputSPVCode.getText().toString(),"send_email",randomOTP,inputEmail.getText().toString().trim());
                        call.enqueue(new Callback<Col_ChangePassword>() {
                            @Override
                            public void onResponse(Call<Col_ChangePassword> call, Response<Col_ChangePassword> response) {
                                if (response.isSuccessful()){
                                    colChangePassword = response.body();

                                    if (colChangePassword == null){
                                        new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                            new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                            new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                    new AlertDialog.Builder(Activity_ResetPassword.this)
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
                                new AlertDialog.Builder(Activity_ResetPassword.this)
                                        .setTitle("Warning")
                                        .setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Activity_ResetPassword.this,Activity_Login.class);
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
                    Toast.makeText(Activity_ResetPassword.this, "Pengiriman ulang OTP maksimal hanya 3x ", Toast.LENGTH_SHORT).show();
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