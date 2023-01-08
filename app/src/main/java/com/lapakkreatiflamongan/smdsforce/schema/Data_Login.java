package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Login {
    String Username;
    String Password;
    String Deviceid;
    String Version;
    String Status;
    String Name;
    String DownloadDate;
    String BranchID;
    String BranchName;
    String Code;
    String VersionUpdate;
    String ForceUpdate;
    String WeekNo;
    String Bearer;

    public Data_Login(String username, String password, String deviceid, String version, String status, String name, String downloadDate, String branchID, String branchName, String code, String versionUpdate, String forceUpdate, String weekNo, String bearer) {
        Username = username;
        Password = password;
        Deviceid = deviceid;
        Version = version;
        Status = status;
        Name = name;
        DownloadDate = downloadDate;
        BranchID = branchID;
        BranchName = branchName;
        Code = code;
        VersionUpdate = versionUpdate;
        ForceUpdate = forceUpdate;
        WeekNo = weekNo;
        Bearer = bearer;
    }

    public String getBearer() {
        return Bearer;
    }

    public void setBearer(String bearer) {
        Bearer = bearer;
    }

    public String getWeekNo() {
        return WeekNo;
    }

    public void setWeekNo(String weekNo) {
        WeekNo = weekNo;
    }

    public String getVersionUpdate() {
        return VersionUpdate;
    }

    public void setVersionUpdate(String versionUpdate) {
        VersionUpdate = versionUpdate;
    }

    public String getForceUpdate() {
        return ForceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        ForceUpdate = forceUpdate;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDeviceid() {
        return Deviceid;
    }

    public void setDeviceid(String deviceid) {
        Deviceid = deviceid;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDownloadDate() {
        return DownloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        DownloadDate = downloadDate;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}
