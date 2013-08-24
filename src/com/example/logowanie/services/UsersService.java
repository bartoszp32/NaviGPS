package com.example.logowanie.services;

import com.example.logowanie.models.User;

/**
 * Created by Barti on 23.08.13.
 */
public class UsersService {


    User user;
    private static UsersService ourInstance = new UsersService();

    public static UsersService getInstance() {
        return ourInstance;
    }

    private UsersService() {
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public boolean isUserAdmin()
    {
        return user != null ? user.isAdmin():false;
    }
}
