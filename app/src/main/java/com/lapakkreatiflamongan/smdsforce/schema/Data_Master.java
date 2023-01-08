package com.lapakkreatiflamongan.smdsforce.schema;

import java.util.List;

public class Data_Master {
    List<Data_Sales> SalesMaster;
    List<Data_Store> StoreMaster;


    public Data_Master(List<Data_Sales> salesMaster, List<Data_Store> storeMaster) {
        SalesMaster = salesMaster;
        StoreMaster = storeMaster;
    }

    public List<Data_Sales> getSalesMaster() {
        return SalesMaster;
    }
    public void setSalesMaster(List<Data_Sales> salesMaster) {
        SalesMaster = salesMaster;
    }
    public List<Data_Store> getStoreMaster() {
        return StoreMaster;
    }
    public void setStoreMaster(List<Data_Store> storeMaster) {
        StoreMaster = storeMaster;
    }
}
