package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by feder on 07/06/2018.
 */
//Specific class which rapresent a Customer User
public class Customer extends User implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public Customer() {
    }

    protected Customer(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
