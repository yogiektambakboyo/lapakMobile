package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_OrderDetail {
    String customer_name,order_no,total,product_name,qty,price,product_total;

    public Data_OrderDetail(String customer_name, String order_no, String total, String product_name, String qty, String price, String product_total) {
        this.customer_name = customer_name;
        this.order_no = order_no;
        this.total = total;
        this.product_name = product_name;
        this.qty = qty;
        this.price = price;
        this.product_total = product_total;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_total() {
        return product_total;
    }

    public void setProduct_total(String product_total) {
        this.product_total = product_total;
    }
}
