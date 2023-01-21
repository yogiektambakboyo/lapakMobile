package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Product {
    String product_name,brand_name,uom,price,qty,id,order_no,sales_id,customers_id,total,seq;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getCustomers_id() {
        return customers_id;
    }

    public void setCustomers_id(String customers_id) {
        this.customers_id = customers_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Data_Product(String product_name, String brand_name, String uom, String price, String qty, String id, String order_no, String sales_id, String customers_id, String total, String seq) {
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.uom = uom;
        this.price = price;
        this.qty = qty;
        this.id = id;
        this.order_no = order_no;
        this.sales_id = sales_id;
        this.customers_id = customers_id;
        this.total = total;
        this.seq = seq;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
