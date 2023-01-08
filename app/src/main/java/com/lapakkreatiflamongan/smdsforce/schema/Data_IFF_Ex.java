package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_IFF_Ex extends Data_IFF{
    String Idx;

    public Data_IFF_Ex(String sellercode, String sellername, String iff0, String iff1, String iff2, String iff3, String iff4, String iff5, String iff6, String idx) {
        super(sellercode, sellername, iff0, iff1, iff2, iff3, iff4, iff5, iff6);
        Idx = idx;
    }

    public String getIdx() {
        return Idx;
    }

    public void setIdx(String idx) {
        Idx = idx;
    }
}
