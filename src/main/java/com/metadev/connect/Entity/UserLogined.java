package com.metadev.connect.Entity;

import com.metadev.connect.Service.PostService;

import java.util.Date;
import java.util.List;

public class UserLogined {

    private static Long userId;
    private static String username;

    private static String name;
    private static String email;
    private static String bio;
    private static int status;
    private static Date date_created_account;
    private static String password;
    private static int[] suggested_preferred_topic;
    private static User userLogined;
    private static List<Long> userLikedPost;

    static PostService postService = new PostService();
    private static Post newPost;



    // Constructor to initializes a UserLogined object with a User object's information
    public UserLogined(User user) {
        userId = user.getUserId();
        username = user.getUsername();
        email = user.getEmail();
        bio = user.getBio();
        date_created_account = user.getDate_created_account();
        password = user.getPassword();
        userLogined = user;
        userLikedPost = postService.getUserLikeStatus(user.getUserId());
        name = user.getName();
        status = user.getStatus();
        suggested_preferred_topic = user.getSuggested_preferred_topic();
    }

    public static List<Long> getUserLikedPost() {
        return userLikedPost;
    }

    public static void setUserLikedPost(List<Long> userLikedPost) {
        UserLogined.userLikedPost = userLikedPost;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        UserLogined.status = status;
    }

    public static int[] getSuggested_preferred_topic() {
        return suggested_preferred_topic;
    }

    public static void setSuggested_preferred_topic(int[] suggested_preferred_topic) {
        UserLogined.suggested_preferred_topic = suggested_preferred_topic;
    }

    // Method to log out the user by setting all fields to null
    public void logout(){
        userId = null;
        username = null;
        email = null;
        bio = null;
        date_created_account = null;
        password = null;
    }

    // Getter and Setter methods for each static field
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
    public static User getUserLogined(){
        return userLogined;
    }

    public static Post getNewPost() {
        return newPost;
    }

    public static void setNewPost(Post newPost) {
        UserLogined.newPost = newPost;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserLogined.name = name;
    }



}