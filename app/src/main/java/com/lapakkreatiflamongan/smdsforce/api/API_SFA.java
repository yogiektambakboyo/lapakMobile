package com.lapakkreatiflamongan.smdsforce.api;


import java.util.List;

import com.lapakkreatiflamongan.smdsforce.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.smdsforce.schema.Col_ChangePassword;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreMaster;
import com.lapakkreatiflamongan.smdsforce.schema.Col_StoreReg;
import com.lapakkreatiflamongan.smdsforce.schema.DataTimeCall;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Channel;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Complaint;
import com.lapakkreatiflamongan.smdsforce.schema.Data_IFF;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Login;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Sales_Spinner;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Time;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_IFF;
import com.lapakkreatiflamongan.smdsforce.schema.Data_summaryMTD;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface API_SFA {
    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("login")
    Call<List<Data_Login>> Login(@Field("username") String username, @Field("password") String password, @Field("version") String version, @Field("deviceid") String deviceid, @Field("session") String session);

    @Headers({ "User-Agent:SFA_Borwita_Android","Cache-Control: no-cache" })
    @GET("api/v1/sqlite/{file}")
    @Streaming
    Call<ResponseBody> getFile(@Path("file") String file);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getWeekNo")
    Call<String> getWeekNo(@Field("spvcode") String spvcode,@Field("sellercode") String sellercode);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getActiveTrip")
    Call<Col_ActiveTrip> getActiveTrip(@Field("sales_id") String sales_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getStoreRegAll")
    Call<Col_StoreReg> getStoreRegAll(@Field("sales_id") String sales_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getActiveTripAll")
    Call<Col_ActiveTrip> getActiveTripAll(@Field("sales_id") String sales_id);


    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getActiveTripDetail")
    Call<Col_ActiveTrip> getActiveTripDetail(@Field("sales_id") String sales_id,@Field("trip_id") String trip_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertActiveTripDetail")
    Call<Col_ActiveTrip> insertActiveTripDetail(@Field("sales_id") String sales_id,@Field("longitude") String longitude,@Field("latitude") String latitude,@Field("trip_id") String trip_id,@Field("georeverse") String georeverse);


    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertStopActiveTrip")
    Call<Col_ActiveTrip> insertStopActiveTrip(@Field("sales_id") String sales_id,@Field("longitude") String longitude,@Field("latitude") String latitude,@Field("trip_id") String trip_id,@Field("georeverse") String georeverse);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("logout")
    Call<String> Logout(@Field("sellercode") String sellercode, @Field("session") String session);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("tracing")
    Call<String> Tracing(@Field("sellercode") String sellercode, @Field("session") String session, @Field("description") String description);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertValidationFalse")
    Call<String> insertValidationFalse(@Field("spvcode") String spvcode,@Field("storecode") String storecode,@Field("time_in") String time_in,@Field("time_out") String time_out,@Field("latitude") String latitude,@Field("longitude") String longitude,@Field("notes") String notes,@Field("dated") String dated,@Field("filephoto") String file_photo,@Field("isvalid_exist") String isvalid_exist);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertTrip")
    Call<String> insertTrip(
            @Field("sales_id") String sales_id,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("georeverse") String georeverse,
            @Field("photo") String photo,
            @Field("notes") String notes
    );

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertReg")
    //name, address, phone_no, sales_id, city, notes, credit_limit, longitude, latitude, email, handphone, whatsapp_no, citizen_id, tax_id, contact_person, type, clasification, contact_person_job_position, contact_person_level,photo
    Call<String> insertReg(
            @Field("sales_id") String sales_id,
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone_no") String phone_no,
            @Field("city") String city,
            @Field("notes") String notes,
            @Field("credit_limit") String credit_limit,
            @Field("email") String email,
            @Field("handphone") String handphone,
            @Field("whatsapp_no") String whatsapp_no,
            @Field("citizen_id") String citizen_id,
            @Field("tax_id") String tax_id,
            @Field("contact_person") String contact_person,
            @Field("type") String type,
            @Field("clasification") String clasification,
            @Field("contact_person_job_position") String contact_person_job_position,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("contact_person_level") String contact_person_level,
            @Field("photo") String photo
    );

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertValidationKTP")
    Call<String> insertValidationKTP(
            @Field("spvcode") String spvcode,
            @Field("storecode") String storecode,
            @Field("netizenid") String netizenid,
            @Field("netizenname") String netizenname,
            @Field("netizenaddress") String netizenaddress,
            @Field("photo") String photo,
            @Field("reason") String reason
    );


    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("insertValidation_v2")
    Call<String> insertValidation(
            @Field("spvcode") String spvcode,
            @Field("storecode") String storecode,
            @Field("time_in") String time_in,
            @Field("time_out") String time_out,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("notes") String notes,
            @Field("dated") String dated,
            @Field("filephoto") String file_photo,
            @Field("isvalid_exist") String isvalid_exist,
            @Field("isvalid_name") String isvalid_name,
            @Field("isvalid_address") String isvalid_address,
            @Field("isvalid_geolocation") String isvalid_geolocation,
            @Field("isvalid_channel") String isvalid_channel,
            @Field("longitude_mst") String longitude_mst,
            @Field("latitude_mst") String latitude_mst,
            @Field("request_name") String request_name,
            @Field("request_address") String request_address,
            @Field("request_address_province") String request_address_province,
            @Field("request_address_city") String request_address_city,
            @Field("request_address_district") String request_address_district,
            @Field("request_address_village") String request_address_village,
            @Field("request_channel") String request_channel,
            @Field("request_handphone") String request_handphone,
            @Field("request_whatsapp") String request_whatsapp,
            @Field("isvalid_handphone") String isvalid_handphone
    );

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getStoreMasterFull_v3")
    Call<Col_StoreMaster> getStoreMasterFull(@Field("spvcode") String spvcode, @Field("sellercode") String sellercode);


    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("api/v1/php/api_sv_send_email.php")
    Call<Col_ChangePassword> requestPassword(@Field("spvcode") String spvcode, @Field("type") String type, @Field("otpcode") String otpcode, @Field("password") String password, @Field("password_old") String password_old);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("api/v1/php/api_sv_send_email_reset.php")
    Call<Col_ChangePassword> requestResetPassword(@Field("spvcode") String spvcode, @Field("type") String type, @Field("otpcode") String otpcode, @Field("email") String email);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getHistoryEdit")
    Call<List<Data_Sales_Spinner>> getHistoryEdit(@Field("spvcode") String spvcode);


}
