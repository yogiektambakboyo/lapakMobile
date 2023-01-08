package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Channel {
    String channelcode,channeldesc;

    public Data_Channel(String channelcode, String channeldesc) {
        this.channelcode = channelcode;
        this.channeldesc = channeldesc;
    }

    public String getChannelcode() {
        return channelcode;
    }

    public void setChannelcode(String channelcode) {
        this.channelcode = channelcode;
    }

    public String getChanneldesc() {
        return channeldesc;
    }

    public void setChanneldesc(String channeldesc) {
        this.channeldesc = channeldesc;
    }
}
