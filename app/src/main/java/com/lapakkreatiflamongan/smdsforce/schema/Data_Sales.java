package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Sales {
    String Name,Code,NumStore,LastTrained;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getNumStore() {
        return NumStore;
    }

    public void setNumStore(String numStore) {
        NumStore = numStore;
    }

    public String getLastTrained() {
        return LastTrained;
    }

    public void setLastTrained(String lastTrained) {
        LastTrained = lastTrained;
    }

    public Data_Sales(String name, String code, String numStore, String lastTrained) {
        Name = name;
        Code = code;
        NumStore = numStore;
        LastTrained = lastTrained;
    }
}
