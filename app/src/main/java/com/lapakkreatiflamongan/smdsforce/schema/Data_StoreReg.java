package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_StoreReg {
    String  id,address,name,is_approved,created_at;

    public Data_StoreReg(String id, String address, String name, String is_approved, String created_at) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.is_approved = is_approved;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
