package com.example.Users;

public abstract class User {
    public String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean likePost(String postID) {
        return false;
    }
}
