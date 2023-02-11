package com.example.Users;

public class CommonUser extends User{
    private String password;

    public CommonUser() {}

    public CommonUser(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }
}
