package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by feder on 05/07/2018.
 */
//Model used to rapresents a call to a waiter made by a customer, this model is used to push a new call into the Firebase Realtime Database
public class WaiterCall implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableId);
        dest.writeString(this.userId);
        dest.writeString(this.id);
    }

    protected WaiterCall(Parcel in) {
        this.tableId = in.readString();
        this.userId = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<WaiterCall> CREATOR = new Parcelable.Creator<WaiterCall>() {
        @Override
        public WaiterCall createFromParcel(Parcel source) {
            return new WaiterCall(source);
        }

        @Override
        public WaiterCall[] newArray(int size) {
            return new WaiterCall[size];
        }
    };
}
