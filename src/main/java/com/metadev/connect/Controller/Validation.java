package com.metadev.connect.Controller;

import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.UserService;

public class Validation {
    static UserService userService = new UserService();

    // Validation for Username

    public static boolean checkUsernameExisted(String username) throws InterruptedException {
        return !(userService.findUserByUsername(username)).isEmpty(); // Connect this to UserService
    }

    public static boolean checkUsernameLengthMin(String username){
        return username.length() < 8;
    }

    public static boolean checkUsernameLengthMax(String username){
        return username.length() >18;
    }

    public static boolean checkUsernameContainSpace(String username){
        return username.contains(" ");
    }

    public static boolean checkUsernameContainSpecialChar(String username){
        return !username.matches("([0-9A-Za-z_]+)");
    }

    public static boolean checkUsernameContainReservedID(String username){
        return username.toLowerCase().contains("admin");
    }

    // Validation for Email Address

    public static boolean checkEmailExisted(String email){
        return !(userService.findUserByEmail(email)).isEmpty();
    }

    public static boolean checkEmailFormat(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Validation for Password

    public static boolean checkPasswordLengthMin(String password){
        return password.length() < 8;
    }

    public static boolean checkPasswordContainsUppercase(String password){
        for(int i = 0; i < password.length(); i++){
            if(Character.isUpperCase(password.charAt(i)))
                return true;
        }
        return false;
    }

    public static boolean checkPasswordContainsDigit(String password){
        for(int i = 0; i < password.length(); i++){
            if(Character.isDigit(password.charAt(i)))
                return true;
        }
        return false;
    }

    public static boolean checkPasswordContainsSpecialCHar(String password){
        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= 32 && password.charAt(i) <= 47 || password.charAt(i) >= 58 && password.charAt(i) <= 64
                    || password.charAt(i) >= 91 && password.charAt(i) <= 96 || password.charAt(i) >= 123 && password.charAt(i) <= 126)
                return true;
        }
        return false;
    }

    public static boolean checkPasswordMatch(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }


    public static boolean checkPasswordContainsLowercase(String password) {
        for(int i = 0; i < password.length(); i++){
            if(Character.isLowerCase(password.charAt(i)))
                return true;
        }
        return false;
    }

    // Login

    public static boolean loginUsingUsername(String username, String password){
        return userService.loginUserByUsername(username, password);
    }

    // Update password

    public static int updateNewPassword(String password){
        return userService.updatePassword(UserLogined.getUserId(), password);
    }
}
