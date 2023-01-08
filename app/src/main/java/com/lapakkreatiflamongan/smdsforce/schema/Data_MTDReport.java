package com.lapakkreatiflamongan.smdsforce.schema;

import org.jetbrains.annotations.NotNull;

/**
 * Created by handy on 28/01/2020.
 * contact : handikadwiputradev@gmail.com
 */

public class Data_MTDReport {

    private String sellercode;

    private String periodid;

    private String actsales;

    private String sellername;

    private String targetsales;

    private String porcent_rankspv_mtd;

    public Data_MTDReport(String sellercode, String periodid, String actsales, String sellername, String targetsales, String porcent_rankspv_mtd) {
        this.sellercode = sellercode;
        this.periodid = periodid;
        this.actsales = actsales;
        this.sellername = sellername;
        this.targetsales = targetsales;
        this.porcent_rankspv_mtd = porcent_rankspv_mtd;
    }

    public String getSellercode() {
        return sellercode;
    }

    public void setSellercode(String sellercode) {
        this.sellercode = sellercode;
    }

    public String getPeriodid() {
        return periodid;
    }

    public void setPeriodid(String periodid) {
        this.periodid = periodid;
    }

    public String getActsales() {
        return actsales;
    }

    public void setActsales(String actsales) {
        this.actsales = actsales;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getTargetsales() {
        return targetsales;
    }

    public void setTargetsales(String targetsales) {
        this.targetsales = targetsales;
    }

    public String getPorcent_rankspv_mtd() {
        return porcent_rankspv_mtd;
    }

    public void setPorcent_rankspv_mtd(String porcent_rankspv_mtd) {
        this.porcent_rankspv_mtd = porcent_rankspv_mtd;
    }

    @NotNull
    @Override
    public String toString() {
        return "Data_MTDReport{" +
                "sellercode='" + sellercode + '\'' +
                ", periodid='" + periodid + '\'' +
                ", actsales='" + actsales + '\'' +
                ", sellername='" + sellername + '\'' +
                ", targetsales='" + targetsales + '\'' +
                ", porcent_rankspv_mtd='" + porcent_rankspv_mtd + '\'' +
                '}';
    }
}
