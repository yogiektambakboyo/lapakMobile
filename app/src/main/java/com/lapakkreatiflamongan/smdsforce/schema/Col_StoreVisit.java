package com.lapakkreatiflamongan.smdsforce.schema;

import java.util.List;

public class Col_StoreVisit {
    String message,status;
    List<Data_StoreVisit> data;

    public Col_StoreVisit(String message, String status, List<Data_StoreVisit> data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data_StoreVisit> getData() {
        return data;
    }

    public void setData(List<Data_StoreVisit> data) {
        this.data = data;
    }
}
