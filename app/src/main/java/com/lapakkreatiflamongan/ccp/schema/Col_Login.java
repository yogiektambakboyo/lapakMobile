package com.lapakkreatiflamongan.ccp.schema;

import java.util.List;

public class Col_Login {
    String message,status;
    List<Data_Login> data;

    public Col_Login(String message, String status, List<Data_Login> data) {
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

    public List<Data_Login> getData() {
        return data;
    }

    public void setData(List<Data_Login> data) {
        this.data = data;
    }
}
