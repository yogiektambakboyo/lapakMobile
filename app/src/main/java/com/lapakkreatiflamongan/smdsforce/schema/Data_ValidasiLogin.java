package com.lapakkreatiflamongan.smdsforce.schema;

/**
 * Created by IT-SUPERMASTER on 23/11/2015.
 */
public class Data_ValidasiLogin {
    String spv;
    String logincode;
    String usercode;
    String username;
    String downloaddate;
    String branchid;
    String branchname;
    String password;
    String status;
    String data;
    String smod,stime,sdelay,sameposlimit,fjptarget;
    String versionno,forceupdate,weekno,lockautosync;

    public String getLockautosync() {
        return lockautosync;
    }

    public void setLockautosync(String lockautosync) {
        this.lockautosync = lockautosync;
    }

    public Data_ValidasiLogin(String spv, String logincode, String usercode, String username, String salestype, String downloaddate, String distributorid, String distributorname, String branchid, String branchname, String password, String status, String data, String stime, String sdelay, String smod, String sameposlimit, String fjptarget, String versionno, String forceupdate, String WeekNo, String Lockautosync) {
        this.spv = spv;
        this.logincode = logincode;
        this.usercode = usercode;
        this.username = username;
        this.downloaddate = downloaddate;

        this.branchid = branchid;
        this.branchname = branchname;
        this.password = password;
        this.status = status;
        this.data = data;
        this.smod = smod;
        this.stime = stime;
        this.sdelay = sdelay;
        this.sameposlimit = sameposlimit;
        this.fjptarget = fjptarget;
        this.versionno = versionno;
        this.forceupdate = forceupdate;
        this.weekno = WeekNo;
        this.lockautosync = Lockautosync;
    }


    public String getWeekno() {
        return weekno;
    }

    public void setWeekno(String weekno) {
        this.weekno = weekno;
    }

    public String getVersionno() {
        return versionno;
    }

    public void setVersionno(String versionno) {
        this.versionno = versionno;
    }

    public String getForceupdate() {
        return forceupdate;
    }

    public void setForceupdate(String forceupdate) {
        this.forceupdate = forceupdate;
    }

    public void setSmod(String smod) {
        this.smod = smod;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setSdelay(String sdelay) {
        this.sdelay = sdelay;
    }

    public String getSameposlimit() {
        return sameposlimit;
    }

    public void setSameposlimit(String sameposlimit) {
        this.sameposlimit = sameposlimit;
    }

    public String getFjptarget() {
        return fjptarget;
    }

    public void setFjptarget(String fjptarget) {
        this.fjptarget = fjptarget;
    }

    public String getSpv() {
        return spv;
    }

    public void setSpv(String spv) {
        this.spv = spv;
    }

    public String getLogincode() {
        return logincode;
    }

    public void setLogincode(String logincode) {
        this.logincode = logincode;
    }

    public String getSalescode() {
        return usercode;
    }

    public void setSalescode(String usercode) {
        this.usercode = usercode;
    }

    public String getSalesname() {
        return username;
    }

    public void setSalesname(String username) {
        this.username = username;
    }


    public String getDownloaddate() {
        return downloaddate;
    }

    public void setDownloaddate(String downloaddate) {
        this.downloaddate = downloaddate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSmod() {
        return smod;
    }

    public String getStime() {
        return stime;
    }

    public String getSdelay() {
        return sdelay;
    }
}
