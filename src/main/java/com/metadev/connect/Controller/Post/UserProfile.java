package com.metadev.connect.Controller.Post;

import com.metadev.connect.Entity.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

public class UserProfile {

    public static User user;

    public UserProfile(User userInput){
        user = userInput;
    }

    public static void setUser(User userInput){
        user = userInput;
    }

    public static User getUser(){
        return user;
    }

    public static Long getUserId(){
        return user.getUserId();
    }
}
