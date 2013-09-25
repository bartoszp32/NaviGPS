package com.navigps.models;


public class UserModel {
   private User user;

    public UserModel() {
        user = new User();
        user.setUserName("admin");
        user.setUserPassword("admin");
        user.setUserId(99);
        user.setAdmin(true);
    }
    public UserModel(String userName, String userPassword, int userId) {
        user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setUserId(userId);
        user.setAdmin(false);
    }
    public User getUser()
    {
        return user;
    }

}
