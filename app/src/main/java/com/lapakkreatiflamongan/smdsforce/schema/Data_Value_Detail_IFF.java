package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Value_Detail_IFF extends Data_Value_Detail {
    String sellername;

    public Data_Value_Detail_IFF(String name, String target, String actual, String procent, String sellercode, String sellername) {
        super(name, target, actual, procent, sellercode);
        this.sellername = sellername;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }
}
