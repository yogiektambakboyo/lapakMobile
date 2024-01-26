package com.lapakkreatiflamongan.ccp.schema;

public class Data_Login {
    String username;
    String password;
    String deviceid;
    String version;
    String status;
    String name;
    String downloadDate;
    String branchid;
    String branchname;
    String code;
    String versionupdate;
    String forceupdate;
    String weekno;
    String bearer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersionupdate() {
        return versionupdate;
    }

    public void setVersionupdate(String versionupdate) {
        this.versionupdate = versionupdate;
    }

    public String getForceupdate() {
        return forceupdate;
    }

    public void setForceupdate(String forceupdate) {
        this.forceupdate = forceupdate;
    }

    public String getWeekno() {
        return weekno;
    }

    public void setWeekno(String weekno) {
        this.weekno = weekno;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public Data_Login(String username, String password, String deviceid, String version, String status, String name, String downloadDate, String branchid, String branchname, String code, String versionupdate, String forceupdate, String weekno, String bearer) {
        this.username = username;
        this.password = password;
        this.deviceid = deviceid;
        this.version = version;
        this.status = status;
        this.name = name;
        this.downloadDate = downloadDate;
        this.branchid = branchid;
        this.branchname = branchname;
        this.code = code;
        this.versionupdate = versionupdate;
        this.forceupdate = forceupdate;
        this.weekno = weekno;
        this.bearer = bearer;
    }
}
