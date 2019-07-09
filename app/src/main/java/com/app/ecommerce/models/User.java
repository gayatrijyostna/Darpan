package com.app.ecommerce.models;

public class User
{
    private String userId;
    private String userName;
    private String userEmail;
    private String userMobile;

    public User(String userId, String userName, String userEmail, String userMobile) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public User(String userName, String userEmail, String userMobile) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
