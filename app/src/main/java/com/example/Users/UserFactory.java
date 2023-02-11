package com.example.Users;

public class UserFactory {
    public User createUser(String userID) {
        User newUser = null;

        if (userID.substring(0,1)=="u") {
            return new CommonUser();
        }
        else if (userID.substring(0,1)=="1") {
            return new Admin();
        }
        else return null;
    }
}
