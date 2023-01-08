package com.lapakkreatiflamongan.smdsforce.schema;

/**
 * Created by IT-SUPERMASTER on 23/12/2016.
 */

public class Data_Spinner {
    String kode,keterangan;

    public Data_Spinner(String kode, String keterangan) {
        this.kode = kode;
        this.keterangan = keterangan;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
