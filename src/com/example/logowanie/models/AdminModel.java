package com.example.logowanie.models;

/**
 * Created by Barti on 23.08.13.
 */
public class AdminModel {
   private User admin;

    public AdminModel() {
        admin = new User();
        admin.setUserName("admin");
        admin.setUserPassword("admin");
        admin.setUserId(-1);
        admin.setAdmin(true);
    }
    public User getAdmin()
    {
        return admin;
    }
}
