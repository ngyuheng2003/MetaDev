package com.metadev.connect.Entity;

import java.util.Date;

public class UserLogined {

    private static Long userId;
    private static String username;
    private static String email;
    private static String bio;
    private static Date date_created_account;
    private static String password;

    public UserLogined(User user) {
        userId = user.getUserId();
        username = user.getUsername();
        email = user.getEmail();
        bio = user.getBio();
        date_created_account = user.getDate_created_account();
        password = user.getPassword();
    }

    public void logout(){
        userId = null;
        username = null;
        email = null;
        bio = null;
        date_created_account = null;
        password = null;
    }

    public static Long getUserId() {
        return userId;
    }

    public static void setUserId(Long userId) {
        UserLogined.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserLogined.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserLogined.email = email;
    }

    public static String getBio() {
        return bio;
    }

    public static void setBio(String bio) {
        UserLogined.bio = bio;
    }

    public static Date getDate_created_account() {
        return date_created_account;
    }

    public static void setDate_created_account(Date date_created_account) {
        UserLogined.date_created_account = date_created_account;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserLogined.password = password;
    }
}