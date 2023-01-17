package com.lapakkreatiflamongan.smdsforce.schema;

import java.util.List;

public class Col_VisitActive {
    String message,status;
    List<Data_VisitActive> data;

    public Col_VisitActive(String message, String status, List<Data_VisitActive> data) {
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

    public List<Data_VisitActive> getData() {
        return data;
    }

    public void setData(List<Data_VisitActive> data) {
        this.data = data;
    }
}
