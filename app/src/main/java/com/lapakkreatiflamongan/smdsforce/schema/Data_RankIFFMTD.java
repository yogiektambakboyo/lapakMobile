package com.lapakkreatiflamongan.smdsforce.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by handy on 06/02/2020.
 * contact : handikadwiputradev@gmail.com
 */

public class Data_RankIFFMTD {

    @SerializedName("sellername")
    @Expose
    private String sellername;

    @SerializedName("sellercode")
    @Expose
    private String sellercode;

    @SerializedName("iffname")
    @Expose
    private String iffname;

    @SerializedName("target")
    @Expose
    private String target;

    @SerializedName("actual")
    @Expose
    private String actual;

    public Data_RankIFFMTD(String sellername, String sellercode, String iffname, String target, String actual) {
        this.sellername = sellername;
        this.sellercode = sellercode;
        this.iffname = iffname;
        this.target = target;
        this.actual = actual;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getIffname() {
        return iffname;
    }

    public void setIffname(String iffname) {
        this.iffname = iffname;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
