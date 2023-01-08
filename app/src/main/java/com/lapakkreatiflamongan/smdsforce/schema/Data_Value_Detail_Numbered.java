package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Value_Detail_Numbered extends Data_Value_Detail {
    String no;

    public Data_Value_Detail_Numbered(String name, String target, String actual, String procent, String no, String sellercode) {
        super(name, target, actual, procent,sellercode);
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
