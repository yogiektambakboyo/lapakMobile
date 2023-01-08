package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_General {
    String Status,Description,Id;

    public Data_General(String status, String description, String id) {
        Status = status;
        Description = description;
        Id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
