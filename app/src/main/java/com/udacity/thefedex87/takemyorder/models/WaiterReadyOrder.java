package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by feder on 12/07/2018.
 */
//Model used to rapresents a ready order sent to a waiter made by a customer, this model is used to push a new call into the Firebase Realtime Database
public class WaiterReadyOrder implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableId);
        dest.writeString(this.id);
    }

    protected WaiterReadyOrder(Parcel in) {
        this.tableId = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<WaiterReadyOrder> CREATOR = new Parcelable.Creator<WaiterReadyOrder>() {
        @Override
        public WaiterReadyOrder createFromParcel(Parcel source) {
            return new WaiterReadyOrder(source);
        }

        @Override
        public WaiterReadyOrder[] newArray(int size) {
            return new WaiterReadyOrder[size];
        }
    };
}
