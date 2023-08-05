package com.lapakkreatiflamongan.ccp.schema;

import java.util.List;

public class Col_OrderDetail {
    String message,status;
    List<Data_OrderDetail> data;

    public Col_OrderDetail(String message, String status, List<Data_OrderDetail> data) {
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

    public List<Data_OrderDetail> getData() {
        return data;
    }

    public void setData(List<Data_OrderDetail> data) {
        this.data = data;
    }
}
