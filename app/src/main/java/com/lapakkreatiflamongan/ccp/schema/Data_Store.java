package com.lapakkreatiflamongan.ccp.schema;

public class Data_Store {
    String Sellercode,Storecode,Storename,Address,Channeldesc,Longitude,Latitude;

    public Data_Store(String sellercode, String storecode, String storename, String address, String channeldesc, String longitude, String latitude) {
        Sellercode = sellercode;
        Storecode = storecode;
        Storename = storename;
        Address = address;
        Channeldesc = channeldesc;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getSellercode() {
        return Sellercode;
    }

    public void setSellercode(String sellercode) {
        Sellercode = sellercode;
    }

    public String getStorecode() {
        return Storecode;
    }

    public void setStorecode(String storecode) {
        Storecode = storecode;
    }

    public String getStorename() {
        return Storename;
    }

    public void setStorename(String storename) {
        Storename = storename;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getChanneldesc() {
        return Channeldesc;
    }

    public void setChanneldesc(String channeldesc) {
        Channeldesc = channeldesc;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }
}
