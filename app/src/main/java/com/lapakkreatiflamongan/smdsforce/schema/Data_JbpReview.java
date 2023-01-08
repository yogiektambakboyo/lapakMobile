package com.lapakkreatiflamongan.smdsforce.schema;

/**
 * Created by handy on 07/01/2020.
 * contact : Handikadwiputradev@gmail.com
 * Project : app.bcp.supervision
 */

public class Data_JbpReview {

    private String brandName;
    private String fYAgo;  // TODO : For Year
    private String fYNow;  // TODO : For Year
    private String p3;      // TODO PREDIKSI BULAN KE 6
    private String p6;      // TODO PREDIKSI BULAN KE 6
    private String IYA;     // TODO Index Versus Year Ago
    private String sal;     // TODO Sales accepted lead

    public Data_JbpReview(String brandName, String fYNow, String fYAgo, String p3, String p6, String IYA, String sal) {
        this.brandName = brandName;
        this.fYNow = fYNow;
        this.fYAgo = fYAgo;
        this.p3 = p3;
        this.p6 = p6;
        this.IYA = IYA;
        this.sal = sal;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getfYAgo() {
        return fYAgo;
    }

    public void setfYAgo(String fYAgo) {
        this.fYAgo = fYAgo;
    }

    public String getfYNow() {
        return fYNow;
    }

    public void setfYNow(String fYNow) {
        this.fYNow = fYNow;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP6() {
        return p6;
    }

    public void setP6(String p6) {
        this.p6 = p6;
    }

    public String getIYA() {
        return IYA;
    }

    public void setIYA(String IYA) {
        this.IYA = IYA;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }
}