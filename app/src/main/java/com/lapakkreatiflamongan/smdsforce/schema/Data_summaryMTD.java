package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_summaryMTD {
    String periodid, spvname, targetsales, actsales,targetcall, actcall, actpcall,targetgdp, actgdp, targetiff, actiff;

    public Data_summaryMTD(String periodid, String spvname, String targetsales, String actsales, String targetcall, String actcall, String actpcall, String targetgdp, String actgdp, String targetiff, String actiff) {
        this.periodid = periodid;
        this.spvname = spvname;
        this.targetsales = targetsales;
        this.actsales = actsales;
        this.targetcall = targetcall;
        this.actcall = actcall;
        this.actpcall = actpcall;
        this.targetgdp = targetgdp;
        this.actgdp = actgdp;
        this.targetiff = targetiff;
        this.actiff = actiff;
    }

    public String getPeriodid() {
        return periodid;
    }

    public void setPeriodid(String periodid) {
        this.periodid = periodid;
    }

    public String getSpvname() {
        return spvname;
    }

    public void setSpvname(String spvname) {
        this.spvname = spvname;
    }

    public String getTargetsales() {
        return targetsales;
    }

    public void setTargetsales(String targetsales) {
        this.targetsales = targetsales;
    }

    public String getactsales() {
        return actsales;
    }

    public void setactsales(String actsales) {
        this.actsales = actsales;
    }

    public String getTargetcall() {
        return targetcall;
    }

    public void setTargetcall(String targetcall) {
        this.targetcall = targetcall;
    }

    public String getactcall() {
        return actcall;
    }

    public void setactcall(String actcall) {
        this.actcall = actcall;
    }

    public String getactpcall() {
        return actpcall;
    }

    public void setactpcall(String actpcall) {
        this.actpcall = actpcall;
    }

    public String getTargetgdp() {
        return targetgdp;
    }

    public void setTargetgdp(String targetgdp) {
        this.targetgdp = targetgdp;
    }

    public String getactgdp() {
        return actgdp;
    }

    public void setactgdp(String actgdp) {
        this.actgdp = actgdp;
    }

    public String getTargetiff() {
        return targetiff;
    }

    public void setTargetiff(String targetiff) {
        this.targetiff = targetiff;
    }

    public String getactiff() {
        return actiff;
    }

    public void setactiff(String actiff) {
        this.actiff = actiff;
    }

    @Override
    public String toString() {
        return "Data_summaryMTD{" +
                "periodid='" + periodid + '\'' +
                ", spvname='" + spvname + '\'' +
                ", targetsales='" + targetsales + '\'' +
                ", actsales='" + actsales + '\'' +
                ", targetcall='" + targetcall + '\'' +
                ", actcall='" + actcall + '\'' +
                ", actpcall='" + actpcall + '\'' +
                ", targetgdp='" + targetgdp + '\'' +
                ", actgdp='" + actgdp + '\'' +
                ", targetiff='" + targetiff + '\'' +
                ", actiff='" + actiff + '\'' +
                '}';
    }
}
