package com.metadev.connect;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserService {
    private static final int SALT_LENGTH = 16; // Length of the salt in bytes
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String username, String password) {
        // Check if the username is already taken
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        // Validate the password against the policy
        if (!isPasswordValid(password)) {
            return false;
        }

        // Hash the password with a salt and store the user
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword);
        userRepository.save(user);
        return true;
    }

    public boolean loginUser(String username, String password) {
        // Fetch the hashed password from the database for the given username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        // Verify the password
        return verifyPassword(password, user.getPassword());
    }

    private String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash the password with the salt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12, random));

            // Combine the salt and hashed password
            return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure random salt", e);
        }
    }

    private boolean verifyPassword(String password, String storedPassword) {
        // Split the stored password to extract the salt and the actual hashed password
        String[] parts = storedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hashedPassword = parts[1];

        // Hash the input password with the extracted salt and compare the result
        String hashedInputPassword = BCrypt.hashpw(password, new String(salt));
        return BCrypt.checkpw(password, hashedPassword);
    }

    private boolean isPasswordValid(String password) {
        // Define the password policy rules
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        return pattern.matcher(password).matches();
    }
}
