package com.navigps.models;


public class AdminModel {
   private User admin;

    public AdminModel() {
        admin = new User();
        admin.setUserName("admin");
        admin.setUserPassword("admin");
        admin.setUserId(3);
        admin.setAdmin(true);
    }
    public User getAdmin()
    {
        return admin;
    }
}
