package com.navigps.models;

/**
 * Created by Barti on 23.08.13.
 */
public class User  {

    private int userId;
    private String userName;
    private String userPassword;
    private boolean isAdmin = false;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
