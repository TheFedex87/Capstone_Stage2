package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 07/06/2018.
 */

//This is the base class for generic UserBase (Waiter, Customer)
public abstract class UserBase {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String email;
    protected String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
