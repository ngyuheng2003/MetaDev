package com.metadev.connect;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
public interface UserRepository {
    void save(User user);
    Optional<User> findByUsername(String username);
}

