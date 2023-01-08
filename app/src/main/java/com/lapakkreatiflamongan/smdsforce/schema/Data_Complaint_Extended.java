package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Complaint_Extended extends Data_Complaint {
    String complaint;


    public String getComplaint() {
        return complaint;
    }

    public Data_Complaint_Extended(String storecode, String storename, String sellercode, String sellername, String longitude, String latitude, String longitudecall, String latitudecall, String complaint) {
        super(storecode, storename, sellercode, sellername, longitude, latitude, longitudecall, latitudecall);
        this.complaint = complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
