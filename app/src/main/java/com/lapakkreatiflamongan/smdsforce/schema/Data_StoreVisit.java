package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_StoreVisit {
    String branch_name,branch_id,sales_id,sales_name,customer_id,customer_name,address,visit_day,visit_week,isvisit;

    public Data_StoreVisit(String branch_name, String branch_id, String sales_id, String sales_name, String customer_id, String customer_name, String address, String visit_day, String visit_week, String isvisit) {
        this.branch_name = branch_name;
        this.branch_id = branch_id;
        this.sales_id = sales_id;
        this.sales_name = sales_name;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.address = address;
        this.visit_day = visit_day;
        this.visit_week = visit_week;
        this.isvisit = isvisit;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVisit_day() {
        return visit_day;
    }

    public void setVisit_day(String visit_day) {
        this.visit_day = visit_day;
    }

    public String getVisit_week() {
        return visit_week;
    }

    public void setVisit_week(String visit_week) {
        this.visit_week = visit_week;
    }

    public String getIsvisit() {
        return isvisit;
    }

    public void setIsvisit(String isvisit) {
        this.isvisit = isvisit;
    }
}
