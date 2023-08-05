package com.lapakkreatiflamongan.ccp.schema;

public class Data_Position {
    String latitude;
    String longitude;
    String sellercode;
    String createdate;
    String spvcode;
    String sellername;

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public Data_Position(String latitude, String longitude, String sellercode, String createdate, String spvcode, String sellername) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sellercode = sellercode;
        this.createdate = createdate;
        this.spvcode = spvcode;
        this.sellername = sellername;
    }

    public String getSpvcode() {
        return spvcode;
    }

    public void setSpvcode(String spvcode) {
        this.spvcode = spvcode;
    }

    public Data_Position() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
