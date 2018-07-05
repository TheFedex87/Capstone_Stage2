package com.udacity.thefedex87.takemyorder.models.GooglePlaceDetailModel;

/**
 * Created by feder on 05/07/2018.
 */

public class WaiterCall {
    private String tableId;
    private String userId;

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
}
