package com.lapakkreatiflamongan.ccp.schema;

import java.util.List;

public class Col_OrderMaster {
    String message,status;
    List<Data_OrderMaster> data;

    public Col_OrderMaster(String message, String status, List<Data_OrderMaster> data) {
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

    public List<Data_OrderMaster> getData() {
        return data;
    }

    public void setData(List<Data_OrderMaster> data) {
        this.data = data;
    }
}
