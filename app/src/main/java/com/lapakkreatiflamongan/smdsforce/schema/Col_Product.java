package com.lapakkreatiflamongan.smdsforce.schema;

import java.util.List;

public class Col_Product {
    String message,status;
    List<Data_Product> data;

    public Col_Product(String message, String status, List<Data_Product> data) {
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

    public List<Data_Product> getData() {
        return data;
    }

    public void setData(List<Data_Product> data) {
        this.data = data;
    }
}
