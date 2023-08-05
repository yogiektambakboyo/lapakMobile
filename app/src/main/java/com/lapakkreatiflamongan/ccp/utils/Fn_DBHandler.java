package com.lapakkreatiflamongan.ccp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by IT-SUPERMASTER on 24/11/2015.
 */
public class Fn_DBHandler extends SQLiteOpenHelper {
    private final String TAG_SELLERMASTER = "SellerMaster";
    private final String TAG_STOREMASTER = "StoreMaster";
    private final String TAG_MASTERSURVEY = "MasterSurvey";
    private final String TAG_MASTERANSWER = "MasterAnswer";
    private final String TAG_STATUS = "status";

    public Fn_DBHandler(Context context, String DB_NAME) {
        super(context, context.getFilesDir()+ File.separator +DB_NAME, null, 1);
    }


    public JSONObject GetAddressProvinsi() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select distinct idprovinsi,provinsi from addressmaster order by provinsi";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray  jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("idprovinsi", cursor.getString(cursor.getColumnIndex("idprovinsi")));
                JData.put("provinsi", cursor.getString(cursor.getColumnIndex("provinsi")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put("ADDRESS",jArray);
        }
        else{
            jResult.put("ADDRESS",0);
        }
        cursor.close();
        db.close();
        return jResult;
    }

    public JSONObject GetAddressKabupaten(String IdProvinsi) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select distinct idkabupaten,kabupaten from addressmaster where idprovinsi='"+IdProvinsi+"' order by kabupaten";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray  jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("idkabupaten", cursor.getString(cursor.getColumnIndex("idkabupaten")));
                JData.put("kabupaten", cursor.getString(cursor.getColumnIndex("kabupaten")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put("ADDRESS",jArray);
        }
        else{
            jResult.put("ADDRESS",0);
        }
        cursor.close();
        db.close();
        return jResult;
    }


    public JSONObject GetAddressKecamatan(String IdProvinsi,String IdKabupaten) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select distinct idkecamatan,kecamatan from addressmaster where idprovinsi='"+IdProvinsi+"' and idkabupaten='"+IdKabupaten+"' order by kecamatan";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray  jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("idkecamatan", cursor.getString(cursor.getColumnIndex("idkecamatan")));
                JData.put("kecamatan", cursor.getString(cursor.getColumnIndex("kecamatan")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put("ADDRESS",jArray);
        }
        else{
            jResult.put("ADDRESS",0);
        }
        cursor.close();
        db.close();
        return jResult;
    }


    public JSONObject GetAddressDesa(String IdProvinsi,String IdKabupaten,String IdKecamatan) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select distinct iddesa,desa from addressmaster where idprovinsi='"+IdProvinsi+"' and idkabupaten='"+IdKabupaten+"' and idkecamatan='"+IdKecamatan+"' order by desa";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray  jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("iddesa", cursor.getString(cursor.getColumnIndex("iddesa")));
                JData.put("desa", cursor.getString(cursor.getColumnIndex("desa")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put("ADDRESS",jArray);
        }
        else{
            jResult.put("ADDRESS",0);
        }
        cursor.close();
        db.close();
        return jResult;
    }



    public String getTodayNoSeparator(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedTime = df.format(c.getTime());
        return  formattedTime;
    }

    // End

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //---------------------- Start Here -----------------------------//

    public void CreateMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS StoreMaster");
        db.execSQL("DROP TABLE IF EXISTS BusinessMaster");
        db.execSQL("DROP TABLE IF EXISTS SkillMaster");
        db.execSQL("DROP TABLE IF EXISTS TrainingMaster");
        db.execSQL("DROP TABLE IF EXISTS SellerMaster");
        db.execSQL("DROP TABLE IF EXISTS SurveyMaster");
        db.execSQL("DROP TABLE IF EXISTS SurveyMasterClosing");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailPreparation");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailReview");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailReviewCapture");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailReviewClosing");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailBusiness");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailBusinessClosing");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailSkill");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailSkillClosing");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailTraining");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailTrainingClosing");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailStorePerformance");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailStorePerformanceVol");
        db.execSQL("DROP TABLE IF EXISTS SurveyDetailStore");
        db.execSQL("DROP TABLE IF EXISTS MasterSurvey");
        db.execSQL("DROP TABLE IF EXISTS MasterAnswer");
        db.execSQL("DROP TABLE IF EXISTS MasterTrainingStep");
        db.execSQL("DROP TABLE IF EXISTS ValidationVisit");
        db.execSQL("CREATE TABLE IF NOT EXISTS StoreMaster(sellercode TEXT,storecode TEXT,storename TEXT,channeldesc TEXT, address TEXT,longitude TEXT,latitude TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS BusinessMaster(id TEXT,question_id TEXT,description TEXT,skill_id TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SkillMaster(id TEXT,training_id TEXT,description TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrainingMaster(id TEXT,description TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SellerMaster(sellercode TEXT,sellername TEXT,numstore TEXT,lasttrained TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyMaster(survey_code TEXT,survey_id TEXT,dated date,sellercode TEXT,sellername TEXT,spvcode TEXT,deviceid TEXT,isclosed TEXT,note TEXT,note_result TEXT,note_nextstep TEXT,rating TEXT,schedule TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyMasterClosing(survey_code TEXT,survey_id TEXT,dated date,note TEXT,note_result TEXT,note_nextstep TEXT,rating TEXT,schedule TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailPreparation(survey_code TEXT,survey_id TEXT,question_id TEXT,value TEXT,remark TEXT,longitude TEXT,latitude TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailStorePerformance(survey_code TEXT,survey_id TEXT,storecode TEXT,question_id TEXT,value TEXT,remark TEXT,createddate TEXT,longitude TEXT,latitude TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailStorePerformanceVol(survey_code TEXT,survey_id TEXT,storecode TEXT,objective TEXT,actual TEXT,createddate TEXT,idx TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailReview(survey_code TEXT,survey_id TEXT,question_id TEXT,objective TEXT,actual TEXT,remark TEXT,createddate TEXT,idx TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailReviewCapture(survey_code TEXT,survey_id TEXT,storecode TEXT,question_id TEXT,value TEXT,remark TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailReviewClosing(survey_code TEXT,survey_id TEXT,question_id TEXT,objective TEXT,actual TEXT,remark TEXT,createddate TEXT,idx TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailBusiness(survey_code TEXT,survey_id TEXT,business_id TEXT,sequence TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailBusinessClosing(survey_code TEXT,survey_id TEXT,business_id TEXT,sequence TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailSkill(survey_code TEXT,survey_id TEXT,skill_id TEXT,sequence TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailSkillClosing(survey_code TEXT,survey_id TEXT,skill_id TEXT,sequence TEXT,createddate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailTraining(survey_code TEXT,survey_id TEXT,training_id TEXT,note TEXT,createdate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailTrainingClosing(survey_code TEXT,survey_id TEXT,training_id TEXT,createdate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailStore(survey_code TEXT,survey_id TEXT,storecode TEXT,question_id TEXT,value TEXT,remark TEXT,createdate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SurveyDetailDetailStoreReview(survey_code TEXT,survey_id TEXT,description TEXT,createdate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MasterSurvey(id TEXT,survey_id TEXT,question_id TEXT,question TEXT,type_id TEXT,description TEXT,answer_type_id TEXT,answer_type TEXT,sequence TEXT,parent TEXT,category TEXT,idx_green TEXT,idx_yellow TEXT,shortdesc TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MasterAnswer(id TEXT,answer_type_id TEXT,description TEXT,description_long TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MasterTrainingStep(id TEXT,skill_id TEXT,question_id TEXT,question TEXT,answer_type_id TEXT,answer_type TEXT,sequence TEXT,shortdesc TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ValidationVisit(dated TEXT,validationkey TEXT)");
    }

    public void insertValidationVisit(String dated, String validationkey){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO ValidationVisit(dated,validationkey) values ('"+dated+"','"+validationkey+"')");
    }

    public void deleteSurveyDetailBusiness(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailBusiness");
    }

    public void deleteSurveyDetailBusinessClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailBusinessClosing");
    }

    public void deleteSurveyDetailReviewClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailReviewClosing");
    }

    public void insertSurveyDetailBusiness(String survey_code, String survey_id, String business_id, String sequence, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailBusiness(survey_code,survey_id,business_id,sequence,createddate) values ('"+survey_code+"','"+survey_id+"','"+business_id+"','"+sequence+"','"+createddate+"')");
    }

    public void insertSurveyDetailBusinessClosing(String survey_code, String survey_id, String business_id, String sequence, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailBusinessClosing(survey_code,survey_id,business_id,sequence,createddate) values ('"+survey_code+"','"+survey_id+"','"+business_id+"','"+sequence+"','"+createddate+"')");
    }

    public void insertSurveyDetailSkill(String survey_code, String survey_id, String skill_id, String sequence, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailSkill(survey_code,survey_id,skill_id,sequence,createddate) values ('"+survey_code+"','"+survey_id+"','"+skill_id+"','"+sequence+"','"+createddate+"')");
    }

    public void insertSurveyDetailSkillClosing(String survey_code, String survey_id, String skill_id, String sequence, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailSkillClosing(survey_code,survey_id,skill_id,sequence,createddate) values ('"+survey_code+"','"+survey_id+"','"+skill_id+"','"+sequence+"','"+createddate+"')");
    }

    public void insertSurveyDetailTraining(String survey_code, String survey_id, String training_id, String note, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailTraining(survey_code,survey_id,training_id,note,createdate) values ('"+survey_code+"','"+survey_id+"','"+training_id+"','"+note+"',datetime('now','localtime'))");
    }

    public void insertSurveyDetailTrainingClose(String survey_code, String survey_id, String training_id, String note, String createddate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailTrainingClosing(survey_code,survey_id,training_id,createdate) values ('"+survey_code+"','"+survey_id+"','"+training_id+"',datetime('now','localtime'))");
    }

    public void insertSurveyDetailTrainingCloseCopy(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailTrainingClosing(survey_code,survey_id,training_id,createdate) select survey_code,survey_id,training_id,datetime('now','localtime') as createdate from SurveyDetailTraining ");
    }

    public void updateSurveyDetailTrainingClose(String training_id, String dateNow){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE SurveyDetailTrainingClosing set training_id='"+training_id+"',createdate='"+dateNow+"' ");
    }

    public void deleteSurveyDetailSkill(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailSkill");
    }

    public void deleteSurveyDetailSkillClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailSkillClosing");
    }

    public void deleteSurveyDetailTraining(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailTraining");
    }

    public void deleteSurveyDetailTrainingClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailTrainingClosing");
    }

    public void deleteValidationVisit(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ValidationVisit where dated!='"+getToday()+"' ");
    }



    public void InsertSurveyDetailStorePerformanceVol(String survey_code, String survey_id, String storecode, String objective, String actual, String createddate, String idx){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailStorePerformanceVol(survey_code,survey_id,storecode,objective,actual,createddate,idx) values ('"+survey_code+"','"+survey_id+"','"+storecode+"','"+objective+"','"+actual+"','"+createddate+"','"+idx+"')");
    }

    public void DeleteSurveyDetailStorePerformance(String storecode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailStorePerformance WHERE storecode='"+storecode+"'");
    }

    public void DeleteSurveyDetailStorePerformanceAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailStorePerformance");
    }




    public void DeleteSurveyDetailStorePerformanceVol(String storecode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailStorePerformanceVol WHERE storecode='"+storecode+"'");
    }

    public void DeleteSurveyDetailStorePerformanceVolAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailStorePerformanceVol");
    }

    public void InsertSurveyDetailStorePerformance(String survey_code, String survey_id, String storecode, String question_id, String value, String createddate, String longitude, String latitude){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailStorePerformance(survey_code,survey_id,storecode,question_id,value,createddate,longitude,latitude) values ('"+survey_code+"','"+survey_id+"','"+storecode+"','"+question_id+"','"+value+"',datetime('now','localtime'),'"+longitude+"','"+latitude+"')");
    }

    public void InsertSurveyDetailReviewCapture(String Comment, String dateNow){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailReviewCapture(survey_code,survey_id,storecode,question_id,value,remark,createddate) select survey_code,survey_id,storecode,question_id,value,'"+Comment+"','"+dateNow+"' from SurveyDetailStorePerformance ");
    }

    public void InsertSurveyDetailStore(String survey_code, String survey_id, String storecode, String question_id, String value, String remark, String createdate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailStore(survey_code,survey_id,storecode,question_id,value,remark,createdate) values ('"+survey_code+"','"+survey_id+"','"+storecode+"','"+question_id+"','"+value+"','"+remark+"',datetime('now','localtime'))");
    }

    public void DeleteSurveyDetailStore(String storecode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailStore WHERE storecode='"+storecode+"'");
    }


    //id,skill_id,question_id,question,answer_type,sequence
    public void DeleteBusinessMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM BusinessMaster");
    }


    public void DeleteSkillMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SkillMaster");
    }

    public void DeleteTrainingStepMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM MasterTrainingStep");
    }

    public void InsertTrainingStepMaster(String id, String skill_id, String question_id, String question, String answer_type_id, String answer_type, String sequence, String shortdesc){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO MasterTrainingStep(id,skill_id,question_id,question,answer_type_id,answer_type,sequence,shortdesc) values ('"+id+"','"+skill_id+"','"+question_id+"','"+question+"','"+answer_type_id+"','"+answer_type+"','"+sequence+"','"+shortdesc+"')");
    }

    //grpuo_concat


    public void DeleteTrainingMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TrainingMaster");
    }

    public void InsertTrainingMaster(String id, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO TrainingMaster(id,description) values ('"+id+"','"+description+"')");
    }

    public void InsertSkillMaster(String id, String description, String training_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SkillMaster(id,description,training_id) values ('"+id+"','"+description+"','"+training_id+"')");
    }


    public void InsertBusinessMaster(String id, String description, String skill_id, String Question_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO BusinessMaster(id,description,skill_id,question_id) values ('"+id+"','"+description+"','"+skill_id+"','"+Question_id+"')");
    }

    public void DeleteSellerMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SellerMaster");
    }

    public void DeleteMasterAnswer(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM MasterAnswer");
    }

    public void DeleteSurveyDetailReview(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailReview");
    }

    public void DeleteMasterSurvey(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM MasterSurvey");
    }

    public void InsertSurveyDetailReview(String survey_code, String survey_id, String question_id, String objective, String actual, String remark, String createddate, String idx){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailReview(survey_code,survey_id,question_id,objective,actual,remark,createddate,idx) values ('"+survey_code+"','"+survey_id+"','"+question_id+"','"+objective+"','"+actual+"','"+remark+"','"+createddate+"','"+idx+"')");
    }


    public void InsertSurveyDetailReviewClosing(String survey_code, String survey_id, String question_id, String objective, String actual, String remark, String createddate, String idx){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailReviewClosing(survey_code,survey_id,question_id,objective,actual,remark,createddate,idx) values ('"+survey_code+"','"+survey_id+"','"+question_id+"','"+objective+"','"+actual+"','"+remark+"','"+createddate+"','"+idx+"')");
    }

    public void InsertMasterAnswer(String id, String answer_type_id, String description, String description_long){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO MasterAnswer(id,answer_type_id,description,description_long) values ('"+id+"','"+answer_type_id+"','"+description+"','"+description_long+"')");
    }

    public void InsertMasterSurvey(String id, String survey_id, String question_id, String question, String type_id, String description, String answer_type_id, String answer_type, String sequence, String parent, String category, String idx_green, String idx_yellow, String shortdesc){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO MasterSurvey(id,survey_id,question_id,question,type_id,description,answer_type_id,answer_type,sequence,parent,category,idx_green,idx_yellow,shortdesc) values ('"+id+"','"+survey_id+"','"+question_id+"','"+question+"','"+type_id+"','"+description+"','"+answer_type_id+"','"+answer_type+"','"+sequence+"','"+parent+"','"+category+"','"+idx_green+"','"+idx_yellow+"','"+shortdesc+"')");
    }

    public void DeleteSurveyDetailPreparation(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyDetailPreparation");
    }

    public void InsertSurveyDetailPreparation(String survey_code, String survey_id, String question_id, String value, String remark, String longitude, String latitude){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SurveyDetailPreparation(survey_code,survey_id,question_id,value,remark,longitude,latitude,createddate) values ('"+survey_code+"','"+survey_id+"','"+question_id+"','"+value+"','"+remark+"','"+longitude+"','"+latitude+"',datetime('now','localtime'))");
    }

    public void InsertSellerMaster(String SellerCode, String SellerName, String NumStore, String LastTrained){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO SellerMaster(sellercode,sellername,numstore,lasttrained) values ('"+SellerCode+"','"+SellerName+"','"+NumStore+"','"+LastTrained+"')");
    }

    public JSONObject GetTrainingMaster() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT id,description from TrainingMaster ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_SELLERMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }


    public JSONObject GetReview() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT distinct remark,createddate from SurveyDetailReviewCapture order by createddate desc";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("createddate", cursor.getString(cursor.getColumnIndex("createddate")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }


    public JSONObject GetSalesVol(String storecode) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT survey_code,survey_id,storecode,objective,actual,createddate,idx from SurveyDetailStorePerformanceVol where storecode='"+storecode+"' ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("survey_code", cursor.getString(cursor.getColumnIndex("survey_code")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("storecode", cursor.getString(cursor.getColumnIndex("storecode")));
                JData.put("objective", cursor.getString(cursor.getColumnIndex("objective")));
                JData.put("actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("createddate", cursor.getString(cursor.getColumnIndex("createddate")));
                JData.put("idx", cursor.getString(cursor.getColumnIndex("idx")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetLegend() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT distinct description,description_long FROM MasterAnswer where answer_type_id=4 ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("description_long", cursor.getString(cursor.getColumnIndex("description_long")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }



    public JSONObject GetSkillMaster() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT id,description,training_id from SkillMaster ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("training_id", cursor.getString(cursor.getColumnIndex("training_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_SELLERMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetBusinessMaster() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT id,description,training_id,question_id from BusinessMaster ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("training_id", cursor.getString(cursor.getColumnIndex("training_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_SELLERMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetSellerMaster() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT sellercode,sellername,numstore,lasttrained from SellerMaster ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("sellercode", cursor.getString(cursor.getColumnIndex("sellercode")));
                JData.put("sellername", cursor.getString(cursor.getColumnIndex("sellername")));
                JData.put("numstore", cursor.getString(cursor.getColumnIndex("numstore")));
                JData.put("lasttrained", cursor.getString(cursor.getColumnIndex("lasttrained")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_SELLERMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterAnswers() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT id,answer_type_id,description from MasterAnswer ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERANSWER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurvey(String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT COALESCE(m.shortdesc,' ') shortdesc,m.idx_green,m.idx_yellow,COALESCE(p.value,'') as value,COALESCE(p.remark,'') as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " left join SurveyDetailPreparation p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                " where m.category='"+Category+"' " +
                " order by CAST(m.sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyPerformance(String Storecode, String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT m.shortdesc,m.idx_green,m.idx_yellow,COALESCE(p.value,'') as value,COALESCE(p.remark,'') as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " left join SurveyDetailStorePerformance p on p.survey_id=m.survey_id and p.question_id=m.question_id and p.storecode='"+Storecode+"' " +
                " where m.category='"+Category+"' " +
                " order by CAST(m.sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetSummaryStorePerformance() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" select call,pcall,ifast1,ifast2,goldenpoint,startselling,stopsellling,timecall,salesobj,salesact,salesidx from ( " +
                " " +
                " select count(distinct storecode) as call,0 as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=55 and value='YES' " +
                " union " +
                " select 0 as call,count(distinct storecode) as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=11 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,count(distinct storecode) as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=12 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,count(distinct storecode) as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=13 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,0 as ifast2,count(distinct storecode) as goldepoint,0 as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=14 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,count(distinct storecode) as startselling,0 as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=15 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,count(distinct storecode) as stopselling,0 as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=16 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,count(distinct storecode) as timecall,0 as salesobj,0 as salesact,0 as salesidx from SurveyDetailStore where question_id=59 and value='YES' " +
                " union " +
                " select 0 as call,0 as pcall,0 as ifast1,0 as ifast2,0 as goldepoint,0 as startselling,0 as stopselling,0 as timecall,sum(coalesce(objective,0)) as salesobj,sum(coalesce(actual,0)) as salesact,case when sum(coalesce(objective,0))=0 then (sum(coalesce(actual,0))*100) else (sum(coalesce(actual,0))*100)/sum(coalesce(objective,0)) end  as salesidx from SurveyDetailStorePerformanceVol " +
                ") a ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("call", cursor.getString(cursor.getColumnIndex("call")));
                JData.put("pcall", cursor.getString(cursor.getColumnIndex("pcall")));
                JData.put("ifast1", cursor.getString(cursor.getColumnIndex("ifast1")));
                JData.put("ifast2", cursor.getString(cursor.getColumnIndex("ifast2")));
                JData.put("goldepoint", cursor.getString(cursor.getColumnIndex("goldepoint")));
                JData.put("startselling", cursor.getString(cursor.getColumnIndex("startselling")));
                JData.put("stopselling", cursor.getString(cursor.getColumnIndex("stopselling")));
                JData.put("timecall", cursor.getString(cursor.getColumnIndex("timecall")));
                JData.put("salesobj", cursor.getString(cursor.getColumnIndex("salesobj")));
                JData.put("salesact", cursor.getString(cursor.getColumnIndex("salesact")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("salesidx", cursor.getString(cursor.getColumnIndex("salesidx")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetDetailStorePerformance(String Sellercode) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" select distinct m.storecode,m.storename,coalesce(call.value,'-')  as call,coalesce(pcall.value,'-') as pcall,coalesce(ifast1.value,'-') as ifast1,coalesce(ifast2.value,'-') as ifast2,  " +
                "coalesce(goldenpoint.value,'-') as goldenpoint,coalesce(startselling.value,'-') as startselling,coalesce(stopselling.value,'-') as stopselling,coalesce(timecall.value,'-') as timecall,  " +
                "coalesce(v.objective,'-') as salesobj,coalesce(actual,'-') as salesact,coalesce(v.idx,'-') as salesidx  " +
                "from StoreMaster m " +
                "join SurveyDetailStore o on o.storecode=m.storecode " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=55) call on call.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=11) pcall on pcall.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=12) ifast1 on ifast1.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=13) ifast2 on ifast2.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=57) goldenpoint on goldenpoint.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=17) startselling on startselling.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=18) stopselling on stopselling.storecode=m.storecode  " +
                "left join (select storecode,coalesce(value,'-') as value from SurveyDetailStorePerformance where question_id=58) timecall on timecall.storecode=m.storecode  " +
                "left join SurveyDetailStorePerformanceVol v on v.storecode=m.storecode where m.sellercode='"+Sellercode+"' ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("call", cursor.getString(cursor.getColumnIndex("call")));
                JData.put("pcall", cursor.getString(cursor.getColumnIndex("pcall")));
                JData.put("ifast1", cursor.getString(cursor.getColumnIndex("ifast1")));
                JData.put("ifast2", cursor.getString(cursor.getColumnIndex("ifast2")));
                JData.put("goldenpoint", cursor.getString(cursor.getColumnIndex("goldenpoint")));
                JData.put("startselling", cursor.getString(cursor.getColumnIndex("startselling")));
                JData.put("stopselling", cursor.getString(cursor.getColumnIndex("stopselling")));
                JData.put("timecall", cursor.getString(cursor.getColumnIndex("timecall")));
                JData.put("salesobj", cursor.getString(cursor.getColumnIndex("salesobj")));
                JData.put("salesact", cursor.getString(cursor.getColumnIndex("salesact")));
                JData.put("salesidx", cursor.getString(cursor.getColumnIndex("salesidx")));
                JData.put("storecode", cursor.getString(cursor.getColumnIndex("storecode")));
                JData.put("storename", cursor.getString(cursor.getColumnIndex("storename")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyStore(String Category, String Storecode) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" select * from (SELECT m.shortdesc,m.idx_green,m.idx_yellow,COALESCE(p.value,'') as value,COALESCE(p.remark,'') as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " left join SurveyDetailStore p on p.survey_id=m.survey_id and p.question_id=m.question_id and p.storecode='"+ Storecode +"' " +
                " where m.category='"+Category+"' " +
                " union " +
                " select '' as shortdesc,90 as idx_green,70 as idx_yellow,COALESCE(p.value,'') as value,COALESCE(p.remark,'') as remark,s.id,y.survey_id,s.question_id,s.question,COALESCE(y.type_id,'') type_id,COALESCE(y.description,'') description,COALESCE(s.answer_type_id,'') as answer_type_id,s.answer_type,s.sequence,'99999999',COALESCE(y.category,'') as category from MasterTrainingStep s " +
                " join SkillMaster m on m.id=s.skill_id " +
                " join StoreMaster v on v.storecode='" +Storecode+"'" +
                " join SurveyDetailTrainingClosing n on n.training_id=m.training_id " +
                " left join (select survey_id,type_id,description,answer_type_id,category from MasterSurvey where category='3' limit 1) y on 1=1 "+
                " left join SurveyDetailStore p on p.survey_id=y.survey_id and p.question_id=s.question_id  and p.storecode='"+ Storecode +"'" +
                " ) aw order by CAST(sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyStoreASM(String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" select * from (SELECT m.shortdesc,m.idx_green,m.idx_yellow,'' as value,'' as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " where m.category='"+Category+"' " +
                " ) aw order by CAST(sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyStoreASMValue(String Category, String survey_code) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" select * from (SELECT m.shortdesc,m.idx_green,m.idx_yellow,'' as value,'' as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " join surveyedetailstore s on s.surveyid=m.surveyid and s.question_id=m.question_id and s.survey_code='"+survey_code+"' " +
                " where m.category='"+Category+"' " +
                " ) aw order by CAST(sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyReview(String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT * FROM ( SELECT m.shortdesc,m.idx_green,m.idx_yellow,group_concat(COALESCE(l.description,'')) as skill,COALESCE(p.actual,'') as actual,COALESCE(p.objective,'') as value,COALESCE(p.remark,'') as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " from MasterSurvey m " +
                " left join SurveyDetailReview p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                " left join BusinessMaster b on b.question_id=m.question_id " +
                " left join SkillMaster l on l.id=b.skill_id " +
                " where m.category='"+Category+"' group by m.shortdesc,m.idx_green,m.idx_yellow,COALESCE(p.actual,''),COALESCE(p.objective,''),COALESCE(p.remark,''),m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                " ) a order by CAST(sequence as integer) asc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyClosingPerformanceSummary(String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT * FROM ( SELECT m.shortdesc,m.idx_green,m.idx_yellow,group_concat(COALESCE(l.description,'')) as skill,COALESCE(z.actual,'') as actual,COALESCE(p.objective,'') as value,COALESCE(p.remark,'') as remark,m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                "                 from MasterSurvey m " +
                "                 left join SurveyDetailReviewClosing p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                "                 left join BusinessMaster b on b.question_id=m.question_id " +
                "                 left join SkillMaster l on l.id=b.skill_id " +
                "                 left join (select survey_id,question_id,count(storecode) as actual from SurveyDetailStorePerformance where value='YES'  group by survey_id,question_id) z on z.survey_id=m.survey_id and z.question_id=m.question_id " +
                "                 where m.category='"+Category+"' group by m.shortdesc,m.idx_green,m.idx_yellow,COALESCE(p.actual,''),COALESCE(p.objective,''),COALESCE(p.remark,''),m.id,m.survey_id,m.question_id,m.question,type_id,m.description,m.answer_type_id,m.answer_type,m.sequence,m.parent,m.category " +
                "                 ) a order by CAST(sequence as integer) asc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("type_id", cursor.getString(cursor.getColumnIndex("type_id")));
                JData.put("answer_type_id", cursor.getString(cursor.getColumnIndex("answer_type_id")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("answer_type", cursor.getString(cursor.getColumnIndex("answer_type")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("parent", cursor.getString(cursor.getColumnIndex("parent")));
                JData.put("category", cursor.getString(cursor.getColumnIndex("category")));
                JData.put("value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
                JData.put("actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("idx_green", cursor.getString(cursor.getColumnIndex("idx_green")));
                JData.put("idx_yellow", cursor.getString(cursor.getColumnIndex("idx_yellow")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyClosing() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT * FROM ( SELECT  group_concat(COALESCE(l.id,'')) as skill_id,'0' as isgap,'0' as ishighlight, p.idx,group_concat(COALESCE(l.description,'')) as skill,COALESCE(p.actual,'0') as actual,COALESCE(p.objective,'0') as value,m.id,m.survey_id,m.question_id,m.question " +
                " from MasterSurvey m " +
                " join SurveyDetailReview p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                " join BusinessMaster b on b.question_id=m.question_id " +
                " join SkillMaster l on l.id=b.skill_id " +
                " where m.category='2' group by COALESCE(p.actual,'0'),COALESCE(p.objective,'0'),m.id,m.survey_id,m.question_id,m.question " +
                " ) a order by CAST(COALESCE(idx,0) as INT) desc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("Survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("Question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("Question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("Value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("Actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("Skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("Idx", cursor.getString(cursor.getColumnIndex("idx")));
                JData.put("IsGap", cursor.getString(cursor.getColumnIndex("isgap")));
                JData.put("IsHighlight", cursor.getString(cursor.getColumnIndex("ishighlight")));
                JData.put("Skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }



    public JSONObject GetMasterSurveyPerformanceClosing() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT * FROM ( SELECT  group_concat(COALESCE(l.id,'')) as skill_id,'0' as isgap,'0' as ishighlight, p.idx,group_concat(COALESCE(l.description,'')) as skill,COALESCE(p.actual,'0') as actual,COALESCE(p.objective,'0') as value,m.id,m.survey_id,m.question_id,m.question " +
                " from MasterSurvey m " +
                " join BusinessMaster b on b.question_id=m.question_id " +
                " join SkillMaster l on l.id=b.skill_id " +
                " left join SurveyDetailReviewClosing p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                " where m.category='5' group by COALESCE(p.actual,'0'),COALESCE(p.objective,'0'),m.id,m.survey_id,m.question_id,m.question " +
                " ) a order by CAST(COALESCE(idx,0) as INT) desc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("Survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("Question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("Question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("Value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("Actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("Skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("Idx", cursor.getString(cursor.getColumnIndex("idx")));
                JData.put("IsGap", cursor.getString(cursor.getColumnIndex("isgap")));
                JData.put("IsHighlight", cursor.getString(cursor.getColumnIndex("ishighlight")));
                JData.put("Skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetMasterSurveyPerformanceClosingASM() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT * FROM ( SELECT  group_concat(COALESCE(l.id,'')) as skill_id,'0' as isgap,'0' as ishighlight, 0 as idx,group_concat(COALESCE(l.description,'')) as skill,COALESCE(p.actual,'0') as actual,COALESCE(p.objective,'0') as value,m.id,m.survey_id,m.question_id,m.question " +
                " from MasterSurvey m " +
                " join BusinessMaster b on b.question_id=m.question_id " +
                " join SkillMaster l on l.id=b.skill_id " +
                " left join SurveyDetailReviewClosing p on p.survey_id=m.survey_id and p.question_id=m.question_id " +
                " where m.category='2' group by COALESCE(p.actual,'0'),COALESCE(p.objective,'0'),m.id,m.survey_id,m.question_id,m.question " +
                " ) a order by CAST(COALESCE(idx,0) as INT) desc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("Survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("Question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("Question", cursor.getString(cursor.getColumnIndex("question")));
                JData.put("Value", cursor.getString(cursor.getColumnIndex("value")));
                JData.put("Actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("Skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("Idx", cursor.getString(cursor.getColumnIndex("idx")));
                JData.put("IsGap", cursor.getString(cursor.getColumnIndex("isgap")));
                JData.put("IsHighlight", cursor.getString(cursor.getColumnIndex("ishighlight")));
                JData.put("Skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetTraining(String wherein) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT m.id as training_id,m.description,l.id as skill_id FROM SkillMaster l" +
                " JOIN TrainingMaster m on m.id=l.training_id " +
                " WHERE l.id in ("+wherein+") order by m.id";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Training_id", cursor.getString(cursor.getColumnIndex("training_id")));
                JData.put("Description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("Skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetTrainingFix(String wherein) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT distinct m.id as training_id,m.description,'1' as skill_id FROM  TrainingMaster m " +
                " join SurveyDetailTrainingClosing l on l.training_id=m.id " +
                " order by m.id";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Training_id", cursor.getString(cursor.getColumnIndex("training_id")));
                JData.put("Description", cursor.getString(cursor.getColumnIndex("description")));
                JData.put("Skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetTrainingAll() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql=" SELECT m.question_idsurvey_id,question_id,objective,actual,remark,createddate FROM SurveyDetailReview ";
        String sql=" SELECT m.id as training_id,m.description FROM TrainingMaster m ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("Id", cursor.getString(cursor.getColumnIndex("training_id")));
                JData.put("Description", cursor.getString(cursor.getColumnIndex("description")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetSellerMasterActive() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT s.sellercode,s.sellername,CASE WHEN m.isclosed='1' THEN 'Closed' WHEN f.survey_code is not null then 'In Store Training' else  'Preparation' end  as numstore,s.lasttrained from SellerMaster s " +
                " join SurveyMaster m on m.sellercode=s.sellercode  " +
                " left join SurveyDetailStorePerformance f on f.survey_code=m.survey_code and f.survey_id=m.survey_id" +
                " limit 1  " ;

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("sellercode", cursor.getString(cursor.getColumnIndex("sellercode")));
                JData.put("sellername", cursor.getString(cursor.getColumnIndex("sellername")));
                JData.put("numstore", cursor.getString(cursor.getColumnIndex("numstore")));
                JData.put("lasttrained", cursor.getString(cursor.getColumnIndex("lasttrained")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_SELLERMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public void DeleteSurveyMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SurveyMaster");
    }

    public int CheckExistSurvey(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyMaster";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int CheckExistSurveyPerformanceClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailReviewClosing";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }



    public int CheckExistSurveyDetailTrainingClosing(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailTrainingClosing";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }


    public void InsertSurveyMaster(String survey_code, String survey_id, String dated, String sellercode, String sellername, String spvcode, String deviceid, String isclosed, String note, String note_result, String note_nextstep, String rating, String schedule){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" INSERT INTO SurveyMaster(survey_code,survey_id,dated,sellercode,sellername,spvcode,deviceid,isclosed,note,note_result,note_nextstep,rating,schedule,createddate) values ('"+survey_code+"','"+survey_id+"','"+dated+"','"+sellercode+"','"+sellername+"','"+spvcode+"','"+deviceid+"','"+isclosed+"','"+note+"','"+note_result+"','"+note_nextstep+"','"+rating+"','"+schedule+"',datetime('now','localtime'))");
    }

    public void DeleteStoreMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM StoreMaster");
    }

    public void DeleteStoreMasterBySeller(String SellerCode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM StoreMaster where sellercode='"+SellerCode+"'");
    }

    public void InsertStoreMaster(String sellercode, String storecode, String storename, String channeldesc, String address, String longitude, String latitude){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" INSERT INTO StoreMaster(sellercode,storecode,storename,channeldesc,address,longitude,latitude) values ('"+sellercode+"','"+storecode+"','"+storename+"','"+channeldesc+"','"+address+"','"+longitude+"','"+latitude+"')");
    }

    public JSONObject GetStoreBySeller(String SellerCode) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT distinct m.sellercode,m.storecode,m.storename,m.channeldesc, m.address,coalesce(s.storecode,'0') as longitude,m.latitude from StoreMaster m " +
                "   join SurveyMaster s on s.sellercode=m.sellercode " +
                "   left join SurveyDetailStore s on s.storecode=m.storecode" +
                "   where m.sellercode='"+SellerCode+"' order by m.storename " ;

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("sellercode", cursor.getString(cursor.getColumnIndex("sellercode")));
                JData.put("storecode", cursor.getString(cursor.getColumnIndex("storecode")));
                JData.put("storename", cursor.getString(cursor.getColumnIndex("storename")));
                JData.put("channeldesc", cursor.getString(cursor.getColumnIndex("channeldesc")));
                JData.put("address", cursor.getString(cursor.getColumnIndex("address")));
                JData.put("longitude", cursor.getString(cursor.getColumnIndex("longitude")));
                JData.put("latitude", cursor.getString(cursor.getColumnIndex("latitude")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_STOREMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }


    public JSONObject GetStore() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = " select distinct m.sellercode,m.storecode,m.storename,m.channeldesc, m.address,m.longitude,m.latitude  " +
                " from SurveyDetailStorePerformance s " +
                " join StoreMaster m on m.storecode=s.storecode " +
                " order by COALESCE(s.createddate,'2050-12-31 23:00:00') asc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("sellercode", cursor.getString(cursor.getColumnIndex("sellercode")));
                JData.put("storecode", cursor.getString(cursor.getColumnIndex("storecode")));
                JData.put("storename", cursor.getString(cursor.getColumnIndex("storename")));
                JData.put("channeldesc", cursor.getString(cursor.getColumnIndex("channeldesc")));
                JData.put("address", cursor.getString(cursor.getColumnIndex("address")));
                JData.put("longitude", cursor.getString(cursor.getColumnIndex("longitude")));
                JData.put("latitude", cursor.getString(cursor.getColumnIndex("latitude")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_STOREMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetStoreASM() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = " select distinct '-' as sellercode,s.storecode,'-' as storename,'-' as channeldesc, '-' as address,'0' as longitude,'0' as latitude  " +
                " from SurveyDetailStore s " +
                " order by COALESCE(s.createdate,'2050-12-31 23:00:00') asc ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("sellercode", cursor.getString(cursor.getColumnIndex("sellercode")));
                JData.put("storecode", cursor.getString(cursor.getColumnIndex("storecode")));
                JData.put("storename", cursor.getString(cursor.getColumnIndex("storename")));
                JData.put("channeldesc", cursor.getString(cursor.getColumnIndex("channeldesc")));
                JData.put("address", cursor.getString(cursor.getColumnIndex("address")));
                JData.put("longitude", cursor.getString(cursor.getColumnIndex("longitude")));
                JData.put("latitude", cursor.getString(cursor.getColumnIndex("latitude")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_STOREMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    //---------------------- End Here -----------------------------//

    public int getExistSurvey(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT distinct storecode from SurveyDetailStore";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistSurveyMasterClosed(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyMaster where isclosed='1' ";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }



    public int getExistSurveyMaster(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyMaster  ";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public String getSurveyCode() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT survey_code from SurveyMaster ";

        Cursor cursor = db.rawQuery(sql, null);
        String result = "0";

        if (cursor.moveToFirst()){
            do {
                result = cursor.getString(cursor.getColumnIndex("survey_code"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public int getExistStore(String Sellercode){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT storecode from StoreMaster where sellercode='"+Sellercode+"'";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistStorePerformance(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT storecode from SurveyDetailStorePerformance ";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistPreparation(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailPreparation";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistReview(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailReview";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistSummary(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailBusiness";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistTraining(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT survey_id from SurveyDetailTraining";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getExistValidationVisit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT dated from ValidationVisit where dated='"+getToday()+"' ";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount();
    }

    public JSONObject getSurveyDetailTraining() throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT t.survey_code,t.survey_id,t.training_id,t.note,t.createdate,m.description from SurveyDetailTraining t" +
                "  join TrainingMaster m on m.id=t.training_id ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("survey_code", cursor.getString(cursor.getColumnIndex("survey_code")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("training_id", cursor.getString(cursor.getColumnIndex("training_id")));
                JData.put("note", cursor.getString(cursor.getColumnIndex("note")));
                JData.put("createdate", cursor.getString(cursor.getColumnIndex("createdate")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject getSurveyDetailSkill(String sequence) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT m.description,t.survey_code,t.survey_id,t.skill_id,t.sequence,t.createddate from SurveyDetailSkill t " +
                " join SkillMaster m on m.id=t.skill_id" +
                " where t.sequence like '"+sequence+"' ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("survey_code", cursor.getString(cursor.getColumnIndex("survey_code")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("createddate", cursor.getString(cursor.getColumnIndex("createddate")));
                JData.put("description", cursor.getString(cursor.getColumnIndex("description")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject getSurveyDetailBusiness(String sequence) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT r.idx,j.question,r.objective,r.actual,'' as skill,t.survey_id,r.question_id as id,'' as skill_id,r.question_id,t.survey_code,t.business_id,t.sequence,t.createddate from SurveyDetailBusiness t " +
                " join SurveyDetailReview r on r.survey_id=t.survey_id and r.survey_code=t.survey_code and r.question_id=t.business_id " +
                " join (select distinct question_id, question from MasterSurvey ) j on j.question_id=r.question_id " +
                " where t.sequence like '"+sequence+"' ";

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("survey_code", cursor.getString(cursor.getColumnIndex("survey_code")));
                JData.put("survey_id", cursor.getString(cursor.getColumnIndex("survey_id")));
                JData.put("business_id", cursor.getString(cursor.getColumnIndex("business_id")));
                JData.put("sequence", cursor.getString(cursor.getColumnIndex("sequence")));
                JData.put("createddate", cursor.getString(cursor.getColumnIndex("createddate")));
                JData.put("objective", cursor.getString(cursor.getColumnIndex("objective")));
                JData.put("actual", cursor.getString(cursor.getColumnIndex("actual")));
                JData.put("skill", cursor.getString(cursor.getColumnIndex("skill")));
                JData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JData.put("idx", cursor.getString(cursor.getColumnIndex("idx")));
                JData.put("skill_id", cursor.getString(cursor.getColumnIndex("skill_id")));
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("question", cursor.getString(cursor.getColumnIndex("question")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public void updateSurveyMaster(String survey_code, String note, String note_result, String note_nextstep, String rating, String schedule){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE SurveyMaster set isclosed='1',note='"+note+"',note_result='"+note_result+"',note_nextstep='"+note_nextstep+"',rating='"+rating+"',schedule='"+schedule+"' where survey_code='"+survey_code+"'");
    }

    public JSONObject GetMasterSurveyPerformanceQuestion(String Category) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=" SELECT m.shortdesc,m.question_id " +
                " from MasterSurvey m " +
                " where m.category='"+Category+"' " +
                " order by CAST(m.sequence as integer) asc ";


        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_MASTERSURVEY,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public JSONObject GetReportReviewStorePerformance(String SQL, int Counter) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = SQL;

        Cursor cursor = db.rawQuery(sql, null);
        JSONObject jResult = new JSONObject();
        JSONArray jArray  = new JSONArray();

        if (cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put("question_id", cursor.getString(cursor.getColumnIndex("question_id")));
                JData.put("shortdesc", cursor.getString(cursor.getColumnIndex("shortdesc")));
                for (int i = 0; i < Counter; i++) {
                    JData.put("val"+i, cursor.getString(cursor.getColumnIndex("val"+i)));
                }
                jArray.put(JData);
            } while (cursor.moveToNext());
            jResult.put(TAG_STOREMASTER,jArray);
        }
        else{
            jResult.put(TAG_STATUS,0);
        }
        cursor.close();
        return jResult;
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void addColumnTabel(String TabelName, String ColumnName, String TipeData){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE " + TabelName + " ADD " + ColumnName + " " + TipeData);
    }

    public String cekColumnExistTabel(String TabelName, String ColumnName){
        String result = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "PRAGMA table_info("+TabelName+")";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                if ((cursor.getString(cursor.getColumnIndex("name"))).equals(ColumnName)){
                    result="1";
                }
            } while (cursor.moveToNext());
        }
        else{
            result = "0";
        }
        cursor.close();
        return result;
    }

}
