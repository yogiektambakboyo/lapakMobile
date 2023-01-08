package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Complaint {
    String storecode,storename, sellercode, sellername, longitude, latitude, longitudecall, latitudecall;

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

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
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

    public String getLongitudecall() {
        return longitudecall;
    }

    public void setLongitudecall(String longitudecall) {
        this.longitudecall = longitudecall;
    }

    public String getLatitudecall() {
        return latitudecall;
    }

    public void setLatitudecall(String latitudecall) {
        this.latitudecall = latitudecall;
    }

    public Data_Complaint(String storecode, String storename, String sellercode, String sellername, String longitude, String latitude, String longitudecall, String latitudecall) {
        this.storecode = storecode;
        this.storename = storename;
        this.sellercode = sellercode;
        this.sellername = sellername;
        this.longitude = longitude;
        this.latitude = latitude;
        this.longitudecall = longitudecall;
        this.latitudecall = latitudecall;
    }
}
