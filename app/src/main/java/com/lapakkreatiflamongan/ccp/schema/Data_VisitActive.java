package com.lapakkreatiflamongan.ccp.schema;

public class Data_VisitActive {
    String customer_id,time_start,time_end,is_checkout,georeverse,latitude,longitude,id,dated,sales_vol;

    public String getSales_vol() {
        return sales_vol;
    }

    public void setSales_vol(String sales_vol) {
        this.sales_vol = sales_vol;
    }

    public Data_VisitActive(String customer_id, String time_start, String time_end, String is_checkout, String georeverse, String latitude, String longitude, String id, String dated, String sales_vol) {
        this.customer_id = customer_id;
        this.time_start = time_start;
        this.time_end = time_end;
        this.is_checkout = is_checkout;
        this.georeverse = georeverse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.dated = dated;
        this.sales_vol = sales_vol;
    }

    public Data_VisitActive(String customer_id, String time_start, String time_end, String is_checkout, String georeverse, String latitude, String longitude, String id, String dated) {
        this.customer_id = customer_id;
        this.time_start = time_start;
        this.time_end = time_end;
        this.is_checkout = is_checkout;
        this.georeverse = georeverse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.dated = dated;
    }

    public String getGeoreverse() {
        return georeverse;
    }

    public void setGeoreverse(String georeverse) {
        this.georeverse = georeverse;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getIs_checkout() {
        return is_checkout;
    }

    public void setIs_checkout(String is_checkout) {
        this.is_checkout = is_checkout;
    }
}
