package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_OrderMaster {
    String customer_name,order_no,total;

    public Data_OrderMaster(String customer_name, String order_no, String total) {
        this.customer_name = customer_name;
        this.order_no = order_no;
        this.total = total;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
