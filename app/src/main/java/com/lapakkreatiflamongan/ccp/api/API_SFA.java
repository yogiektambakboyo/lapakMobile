package com.lapakkreatiflamongan.ccp.api;


import java.util.List;

import com.lapakkreatiflamongan.ccp.schema.Col_ActiveTrip;
import com.lapakkreatiflamongan.ccp.schema.Col_Login;
import com.lapakkreatiflamongan.ccp.schema.Col_OrderDetail;
import com.lapakkreatiflamongan.ccp.schema.Col_OrderMaster;
import com.lapakkreatiflamongan.ccp.schema.Col_Product;
import com.lapakkreatiflamongan.ccp.schema.Col_StoreNOO;
import com.lapakkreatiflamongan.ccp.schema.Col_StoreReg;
import com.lapakkreatiflamongan.ccp.schema.Col_StoreVisit;
import com.lapakkreatiflamongan.ccp.schema.Col_VisitActive;
import com.lapakkreatiflamongan.ccp.schema.Data_Login;
import com.lapakkreatiflamongan.ccp.schema.Data_Product;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface API_SFA {
    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("login")
    Call<Col_Login> Login(@Field("username") String username, @Field("password") String password, @Field("version") String version, @Field("deviceid") String deviceid, @Field("session") String session);

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
    @POST("getStoreVisitToday")
    Call<Col_StoreVisit> getStoreVisitToday(@Field("sales_id") String sales_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getStoreNOO")
    Call<Col_StoreVisit> getStoreNOO(@Field("sales_id") String sales_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getStoreNOODetail")
    Call<Col_StoreNOO> getStoreNOODetail(@Field("sales_id") String sales_id, @Field("customer_id") String customer_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getVisitActive")
    Call<Col_VisitActive> getVisitActive(@Field("sales_id") String sales_id, @Field("customer_id") String customer_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getProductOrder")
    Call<Col_Product> getProductOrder(@Field("sales_id") String sales_id, @Field("customer_id") String customer_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getOrderToday")
    Call<Col_OrderMaster> getOrderToday(@Field("sales_id") String sales_id, @Field("dated") String dated);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getOrderDetail")
    Call<Col_OrderDetail> getOrderDetail(@Field("order_no") String order_no);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("getProductOrderCheckout")
    Call<Col_Product> getProductOrderCheckout(@Field("sales_id") String sales_id, @Field("customer_id") String customer_id);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("updateVisitActive")
    Call<String> updateVisitActive(@Field("sales_id") String sales_id, @Field("customer_id") String customer_id, @Field("is_checkout") String is_checkout);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @FormUrlEncoded
    @POST("confirmOrder")
    Call<String> confirmOrder(@Field("sales_id") String sales_id, @Field("customers_id") String customers_id, @Field("order_no") String order_no, @Field("notes") String notes, @Field("delivery_date") String delivery_date);

    @Headers({ "User-Agent:5uPErV1sIon_8CP_m0biL3" })
    @POST("insertOrder")
    Call<Col_ActiveTrip> insertOrder(@Body List<Data_Product> orderList);

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
    @POST("insertStoreVisit")
    Call<String> insertVisit(
            @Field("sales_id") String sales_id,
            @Field("customer_id") String customer_id,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("georeverse") String georeverse,
            @Field("photo") String photo
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
    @POST("updateNOO")
    Call<String> updateNOO(
            @Field("customer_id") String customer_id,
            @Field("sales_id") String sales_id,
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone_no") String phone_no,
            @Field("city") String city,
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
            @Field("contact_person_level") String contact_person_level
    );



}
