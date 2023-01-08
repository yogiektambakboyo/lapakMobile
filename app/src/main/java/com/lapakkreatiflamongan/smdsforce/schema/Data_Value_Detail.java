package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Value_Detail extends Data_Value {
    String procent,sellercode;

    public Data_Value_Detail(String name, String target, String actual, String procent,String sellercode) {
        super(name, target, actual);
        this.procent = procent;
        this.sellercode = sellercode;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getProcent() {
        return procent;
    }

    public void setProcent(String procent) {
        this.procent = procent;
    }
}
