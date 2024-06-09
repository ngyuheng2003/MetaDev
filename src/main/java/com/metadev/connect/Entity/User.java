package com.metadev.connect.Entity;

import java.util.Date;

// Private fields to store user information
public class User {
    private Long userId;
    private String username;

    private String name;
    private String email;
    private String bio;
    private Date date_created_account;
    private String password;
    private int[] suggested_preferred_topic;
    private int status;

    // Constructor to initialize a User object with all fields
    public User(Long userId, String username, String email, String bio, Date date_created_account, String password, String name, int status, int[] suggested_preferred_topic) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.date_created_account = date_created_account;
        this.password = password;
        this.name = name;
        this.status = status;
        this.suggested_preferred_topic = suggested_preferred_topic;
    }

    // Default constructor to initialize a User object with null values
    public User() {
        this.userId = null;
        this.username = null;
        this.email = null;
        this.bio = null;
        this.date_created_account = null;
        this.password = null;
    }

    // Getter and Setter methods for each field
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDate_created_account() {
        return date_created_account;
    }

    public void setDate_created_account(Date date_created_account) {
        this.date_created_account = date_created_account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int[] getSuggested_preferred_topic() {
        return suggested_preferred_topic;
    }

    public void setSuggested_preferred_topic(int[] suggested_preferred_topic) {
        this.suggested_preferred_topic = suggested_preferred_topic;
    }
}


