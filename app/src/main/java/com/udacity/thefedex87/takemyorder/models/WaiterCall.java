package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 05/07/2018.
 */

public class WaiterCall {
    private String tableId;
    private String userId;
    private String id;

    public WaiterCall(){

    }

    public WaiterCall(String tableId, String userId) {
        this.tableId = tableId;
        this.userId = userId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
