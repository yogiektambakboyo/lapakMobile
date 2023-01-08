package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Sales_Spinner {
    String sellercode,sellername;

    public Data_Sales_Spinner(String sellercode, String sellername) {
        this.sellercode = sellercode;
        this.sellername = sellername;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }
}
