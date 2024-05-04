package com.metadev.connect.Controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TestMain {

    public static void main(String[] main){
        System.out.println(verifyPassword("hello", hashPassword("hello")));
    }
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hash the password with the salt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12, random));

            // Combine the salt and hashed password
            return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure random salt", e);
        }
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        // Split the stored password to extract the salt and the actual hashed password
        String[] parts = storedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hashedPassword = parts[1];

        // Hash the input password with the extracted salt and compare the result
        String hashedInputPassword = BCrypt.hashpw(password, new String(salt));
        return BCrypt.checkpw(password, hashedPassword);
    }
}
