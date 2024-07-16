package com.lapakkreatiflamongan.smdsforce.intent.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_Order;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_OrderDetail;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_TripDetail;
import com.lapakkreatiflamongan.smdsforce.api.API_SFA;
import com.lapakkreatiflamongan.smdsforce.databinding.FragmentHomeBinding;
import com.lapakkreatiflamongan.smdsforce.intent.Activity_Checkout;
import com.lapakkreatiflamongan.smdsforce.intent.Activity_Trip;
import com.lapakkreatiflamongan.smdsforce.intent.Activity_Visit;
import com.lapakkreatiflamongan.smdsforce.intent.Activity_VisitMenu;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Col_OrderDetail;
import com.lapakkreatiflamongan.smdsforce.schema.Col_OrderMaster;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreVisit;
import com.lapakkreatiflamongan.smdsforce.schema.Data_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Data_OrderDetail;
import com.lapakkreatiflamongan.smdsforce.schema.Data_OrderMaster;
import com.lapakkreatiflamongan.smdsforce.schema.Data_StoreVisit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    final Calendar myCalendar= Calendar.getInstance();


    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_CUSTOMERID = "customer_id";
    private final String TAG_CUSTOMERNAME = "customer_name";
    private final String TAG_SPVNAME = "username";
    private final String TAG_SPVCODE = "usercode";
    private final String TAG_LOGINTIME = "logintime";
    private final String TAG_SELLERCODE = "sellercode";
    private final String TAG_SELLERNAME = "sellername";
    private String VERSION_APK = "0.0.7";
    private String BASE_URL = "http://lapakkreatif.com:8081/";

    DecimalFormat formatter;
    DecimalFormatSymbols symbol;

    private final String TAG_LASTLOGIN = "lastlogin";
    private final String ERROR_500 = "500 Internal Server Error";
    private final String ERROR_404 = "404 Request Not Found";
    private final String ERROR_504 = "504 Gateway Time Out";
    private final String ERROR_408 = "408 Request Time Out";
    private final String ERROR_301 = "301 Moved Permanently";
    private final String ERROR_400 = "400 Bad Request";
    private final String ERROR_401 = "401 Unauthorized";
    private final String ERROR_502 = "502 Bad Gateway";
    private final String TAG_SESSION = "session";


    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    API_SFA myAPi = retrofit.create(API_SFA.class);


    ListView listViewTrip;
    Adapter_Order adapter;
    EditText InputDate;
    Button BtnSubmit;
    Dialog dialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        BtnSubmit = binding.ROrderBtnSubmit;
        listViewTrip = binding.ROrderList;
        InputDate = binding.ROrderDate;
        InputDate.setText(getToday());
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="YYYY-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                InputDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        InputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");

        getData();
        return root;
    }


    public String getPref(String KEY){
        SharedPreferences SettingPref = getActivity().getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }


    public void getData(){
        dialog.show();
        listViewTrip.setVisibility(View.GONE);
        Call<Col_OrderMaster> callData = myAPi.getOrderToday(getPref(TAG_SPVCODE),InputDate.getText().toString().trim());
        callData.enqueue(new Callback<Col_OrderMaster>() {
            @Override
            public void onResponse(Call<Col_OrderMaster> call, Response<Col_OrderMaster> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    Col_OrderMaster col = response.body();
                    if (col == null){
                        Toast.makeText(getActivity(), "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        if (col.getStatus().equals("1")){
                            List<Data_OrderMaster> data = col.getData();

                            adapter = new Adapter_Order(getActivity(), data);
                            listViewTrip.setAdapter(adapter);

                            if (data.size()>0) {
                                listViewTrip.setVisibility(View.VISIBLE);
                                listViewTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Dialog dialog1 = new Dialog(getActivity());
                                        dialog1.setContentView(R.layout.d_tripdetail);
                                        dialog1.setCancelable(true);

                                        TextView txtLabel = dialog1.findViewById(R.id.D_TripDetailLabel);
                                        ListView listView = dialog1.findViewById(R.id.D_TripDetailList);
                                        Button btnClose = dialog1.findViewById(R.id.D_TripDetailBtn);
                                        btnClose.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog1.dismiss();
                                            }
                                        });

                                        txtLabel.setText("Detail Order #"+adapter.getItem(i).getOrder_no());

                                        Call<Col_OrderDetail> callData = myAPi.getOrderDetail(adapter.getItem(i).getOrder_no());
                                        callData.enqueue(new Callback<Col_OrderDetail>() {
                                            @Override
                                            public void onResponse(Call<Col_OrderDetail> call, Response<Col_OrderDetail> response) {
                                                if (response.isSuccessful()){

                                                    dialog.dismiss();
                                                    Col_OrderDetail col = response.body();
                                                    if (col == null){
                                                        Toast.makeText(getActivity(), "Ambil Data Gagal : Respon Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        if (col.getStatus().equals("1")){
                                                            List<Data_OrderDetail> data = col.getData();
                                                            Adapter_OrderDetail adapterTripDetail = new Adapter_OrderDetail(getActivity(),data);
                                                            listView.setAdapter(adapterTripDetail);
                                                        }else{
                                                        }
                                                    }
                                                }else{
                                                    dialog.dismiss();
                                                    switch (response.code()) {
                                                        case 404:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                                                            break;
                                                        case 408:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                                                            break;
                                                        case 500:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                            break;
                                                        case 504:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                                                            break;
                                                        case 502:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                                                            break;
                                                        default:
                                                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                                            break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Col_OrderDetail> call, Throwable t) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        dialog1.show();
                                    }
                                });
                            }
                        }else{
                        }
                    }
                }else{
                    dialog.dismiss();
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_404, Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_408, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                        case 504:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_504, Toast.LENGTH_SHORT).show();
                            break;
                        case 502:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_502, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Col_OrderMaster> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Ambil Data Gagal : "+ERROR_500, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}