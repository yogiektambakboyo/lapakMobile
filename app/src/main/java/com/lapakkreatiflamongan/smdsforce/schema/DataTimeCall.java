package com.lapakkreatiflamongan.smdsforce.schema;

public class DataTimeCall {

    String sellercode,sellername,timein,timeclock;

    public DataTimeCall(String sellercode, String sellername, String timein, String timeclock) {
        this.sellercode = sellercode;
        this.sellername = sellername;
        this.timein = timein;
        this.timeclock = timeclock;
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

    public String getTimein() {
        return timein;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public String getTimeclock() {
        return timeclock;
    }

    public void setTimeclock(String timeclock) {
        this.timeclock = timeclock;
    }
}
