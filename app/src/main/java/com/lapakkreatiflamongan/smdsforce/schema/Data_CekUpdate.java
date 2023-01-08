package com.lapakkreatiflamongan.smdsforce.schema;

/**
 * Created by IT-SUPERMASTER on 28/03/2016.
 */
public class Data_CekUpdate {
    String version,link,description,readmelink;

    public Data_CekUpdate(String version, String link, String Desc, String ReadmeLink) {
        this.version = version;
        this.link = link;
        this.description = Desc;
        this.readmelink = ReadmeLink;
    }

    public String getDesc() {
        return description;
    }

    public String getReadmelink() {
        return readmelink;
    }

    public void setReadmelink(String readmelink) {
        this.readmelink = readmelink;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
