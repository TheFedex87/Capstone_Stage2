package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 12/07/2018.
 */

public class WaiterReadyOrder {
    private String tableId;
    private String id;

    public WaiterReadyOrder(){

    }

    public WaiterReadyOrder(String tableId) {
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
