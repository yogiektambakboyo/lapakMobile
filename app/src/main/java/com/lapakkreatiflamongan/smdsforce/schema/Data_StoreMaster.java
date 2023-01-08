package com.lapakkreatiflamongan.smdsforce.schema;

import java.io.Serializable;

public class Data_StoreMaster implements Serializable {
    String sellercode,storecode,storename,address,channeldesc,longitude,latitude,phoneno,whatsappno,netizenid,fjp,city,isvalidated,targetvalidation,ismtd;

    public Data_StoreMaster(String sellercode, String storecode, String storename, String address, String channeldesc, String longitude, String latitude, String phoneno, String whatsappno, String netizenid, String fjp, String city, String isvalidated, String targetvalidation) {
        this.sellercode = sellercode;
        this.storecode = storecode;
        this.storename = storename;
        this.address = address;
        this.channeldesc = channeldesc;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneno = phoneno;
        this.whatsappno = whatsappno;
        this.netizenid = netizenid;
        this.fjp = fjp;
        this.city = city;
        this.isvalidated = isvalidated;
        this.targetvalidation = targetvalidation;
    }

    public Data_StoreMaster(String sellercode, String storecode, String storename, String address, String channeldesc, String longitude, String latitude, String phoneno, String whatsappno, String netizenid, String fjp, String city, String isvalidated, String targetvalidation, String ismtd) {
        this.sellercode = sellercode;
        this.storecode = storecode;
        this.storename = storename;
        this.address = address;
        this.channeldesc = channeldesc;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneno = phoneno;
        this.whatsappno = whatsappno;
        this.netizenid = netizenid;
        this.fjp = fjp;
        this.city = city;
        this.isvalidated = isvalidated;
        this.targetvalidation = targetvalidation;
        this.ismtd = ismtd;
    }

    public String getIsmtd() {
        return ismtd;
    }

    public void setIsmtd(String ismtd) {
        this.ismtd = ismtd;
    }

    public String getTargetvalidation() {
        return targetvalidation;
    }

    public void setTargetvalidation(String targetvalidation) {
        this.targetvalidation = targetvalidation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getStorecode() {
        return storecode;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChanneldesc() {
        return channeldesc;
    }

    public void setChanneldesc(String channeldesc) {
        this.channeldesc = channeldesc;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getWhatsappno() {
        return whatsappno;
    }

    public void setWhatsappno(String whatsappno) {
        this.whatsappno = whatsappno;
    }

    public String getNetizenid() {
        return netizenid;
    }

    public void setNetizenid(String netizenid) {
        this.netizenid = netizenid;
    }

    public String getIsvalidated() {
        return isvalidated;
    }

    public void setIsvalidated(String isvalidated) {
        this.isvalidated = isvalidated;
    }

    public String getFjp() {
        return fjp;
    }

    public void setFjp(String fjp) {
        this.fjp = fjp;
    }
}
