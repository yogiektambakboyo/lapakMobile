package com.lapakkreatiflamongan.ccp.schema;

public class Data_ActiveTrip {
    String id,dated,time_start,time_end,georeverse,duration,longitude,latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getGeoreverse() {
        return georeverse;
    }

    public void setGeoreverse(String georeverse) {
        this.georeverse = georeverse;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Data_ActiveTrip(String id, String dated, String time_start, String time_end, String georeverse, String duration, String longitude, String latitude) {
        this.id = id;
        this.dated = dated;
        this.time_start = time_start;
        this.time_end = time_end;
        this.georeverse = georeverse;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
