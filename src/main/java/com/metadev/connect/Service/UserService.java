package com.metadev.connect.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import com.metadev.connect.Controller.DataSourceConfig;
import com.metadev.connect.Entity.UserPreferredTopic;
import com.metadev.connect.RowMapper.UserRowMapper;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

@Service
public class UserService implements UserRepository {
    private static final int SALT_LENGTH = 16; // Length of the salt in bytes

    // Create an instance of DataSourceConfig to configure the data source
    private final DataSourceConfig dataSourceConfig = new DataSourceConfig();

    // Create a JdbcTemplate object to interact with the database
    private final JdbcTemplate jdbc = new JdbcTemplate(dataSourceConfig.getDataSource());


    @Override
    public int registerNewUser(String username, String email, String password) {
        // SQL query to insert a new user into the table
        String sql = """
                    INSERT INTO 
                    [dbo].[user] 
                    ([username], [email], [password]) 
                    VALUES 
                    (?, ?, ?);
                    INSERT INTO 
                    [dbo].[user_Setting]  
                    (user_id) 
                    SELECT user_id from [dbo].[user] WHERE username = ?;
                    """;
        // Execute the query using JdbcTemplate and return the result
        return jdbc.update(sql, username, email, hashPassword(password), username);
    }

    public int insertUserPreferredTopic(UserPreferredTopic userPreferredTopic){
        // SQL query to insert user preffered topic into the table
        String sql = """
                INSERT INTO
                [dbo].[user_preferred_topic]
                ([user_id], [preferred_topic], [weight])
                VALUES
                (?, ?, ?)
                """;
        // Execute the query using JdbcTemplate and return the result
        return jdbc.update(sql, userPreferredTopic.getUserId(), userPreferredTopic.getTopic(), userPreferredTopic.getWeight());
    }

    public int removeUserPreferredTopic(UserPreferredTopic userPreferredTopic){
        // SQL query to insert user preffered topic into the table
        String sql = """
                DELETE FROM
                [dbo].[user_preferred_topic]
                WHERE
                user_id = ?
                """;
        // Execute the query using JdbcTemplate and return the result
        return jdbc.update(sql, userPreferredTopic.getUserId());
    }

    public boolean checkUserPreferredTopicExistById(Long user_id) throws InterruptedException {
        // SQL query to count the number of preferred topics for the given user ID
        String sql = """
                SELECT 
                COUNT(*) 
                FROM 
                [dbo].[user_preferred_topic] 
                WHERE 
                user_id = ?
                """;
        // Execute the query and retrieve the count
        int count = jdbc.queryForObject(sql, new Object[]{user_id}, Integer.class);
        // Return true if count is greater than 0, otherwise return false
        return count > 0;
    }

    public List<String> findUserPreferredTopic(Long user_id){
        // SQL query to find a username by user ID
        String sql = """
                    SELECT 
                    preferred_topic
                    FROM 
                    [dbo].[user_preferred_topic] 
                    WHERE 
                    user_id = ?
                    """;
        // Execute the query and return the list of matching usernames
        try {
            // Execute the query and return the list of matching preferred topics
            return jdbc.queryForList(sql, new Object[]{user_id}, String.class);
        } catch (EmptyResultDataAccessException e) {
            // Return an empty list if no preferred topics are found for the user
            return List.of();
        } catch (Exception e) {
            // Handle other potential exceptions (e.g., SQL exceptions)
            throw new RuntimeException("Error querying user preferred topics", e);
        }
    }

    @Override
    public List<User> findUser() {
        String sql = """
                    SELECT 
                    *
                    FROM 
                    [dbo].[user] 
                    """;
        return jdbc.query(sql, new UserRowMapper());

    }

    @Override
    public List<String> findUserByUsername(String username) throws InterruptedException {
        // SQL query to find a user by their username
        String sql = """
                    SELECT 
                    username 
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    username = ?
                    """;
        // Execute the query and return the list of matching username
        return jdbc.queryForList(sql, new Object[]{username}, String.class);
    }

    @Override
    public List<String> findUserUsernameById(Long user_id) throws InterruptedException {
        // SQL query to find a username by user ID
        String sql = """
                    SELECT 
                    username 
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    user_id = ?
                    """;
        // Execute the query and return the list of matching usernames
        return jdbc.queryForList(sql, new Object[]{user_id}, String.class);
    }

    @Override
    public List<String> findUserByEmail(String email) {
        // SQL query to find a user by their email
        String sql = """
                    SELECT 
                    email 
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    email = ?
                    """;
        // Execute the query and return the list of matching emails
        return jdbc.queryForList(sql, new Object[]{email}, String.class);
    }

    @Override
    public List<User> findUserInfoByUsername(String username) {
        // SQl query to find all user information by username
        String sql = """
                    SELECT 
                    *
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    username = ?
                    """;
        // Execute the query and return the list of matching User objects
        return jdbc.query(sql, new UserRowMapper(), username);

    }

    @Override
    public List<User> findUserInfoById(Long userId) {
        String sql = """
                    SELECT 
                    *
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    user_id = ?
                    """;
        return jdbc.query(sql, new UserRowMapper(), userId);

    }

    @Override
    public boolean loginUserByEmail(String email, String password) {
        // SQL query to authenticate a user by email and password
        String sql = """
                    SELECT 
                    * 
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    email = ? AND password = ?
                    """;
        // Execute the query and return whether a matching user record is found
        List<String> lists = jdbc.queryForList(sql, new Object[]{email, hashPassword(password)}, String.class);

        return lists.isEmpty();
    }

    @Override
    public boolean loginUserByUsername(String username, String inputPassword) {
        // SQL qeury to find the stored password by username
        String sql = """
                    SELECT 
                    password 
                    FROM 
                    [dbo].[user] 
                    WHERE 
                    username = ?
                    """;
        String userPassword;
        try{
            // Execute the query and get the stored password
            userPassword = jdbc.queryForObject(sql, new Object[]{username}, String.class);
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        // Verify the input password against the stored password
        return verifyPassword(inputPassword, userPassword);
    }

    @Override
    public int updateProfile(Long user_id, String username, String name, String bio, int status, String suggested_preferred_topic) {
        String sql = """
                    UPDATE
                    [dbo].[user]
                    SET
                    [username] = ?, [name] = ?, [bio] = ?, [status] = ?, [suggested_preferred_topic] = ?
                    WHERE
                    user_id = ?;
                    """;
        return jdbc.update(sql, username, name, bio, status, suggested_preferred_topic, user_id);
    }

    @Override
    public List<Integer> fetch2FA(Long user_id) {
        String sql = """
                    SELECT 
                    two_factor_authentication
                    FROM
                    [dbo].[user_setting]
                    WHERE
                    user_id = ?;
                    """;
        return jdbc.queryForList(sql, new Object[]{user_id}, Integer.class);
    }
    @Override
    public int update2FA(Long user_id, int status) {
        String sql = """
                    UPDATE
                    [dbo].[user_setting]
                    SET
                    [two_factor_authentication] = ?
                    WHERE
                    user_id = ?;
                    """;
        return jdbc.update(sql, status, user_id);
    }

    @Override
    public int updatePassword(Long user_id, String newPassword) {
        String sql = """
                    UPDATE
                    [dbo].[user]
                    SET
                    [password] = ?
                    WHERE
                    user_id = ?;
                    """;
        return jdbc.update(sql, hashPassword(newPassword), user_id);
    }


    // Password Hashing

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
        return BCrypt.checkpw(password, hashedPassword);
    }



}
