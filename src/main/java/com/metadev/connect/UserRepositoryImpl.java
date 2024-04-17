package com.metadev.connect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final DatabaseSQL databaseSQL;

    public UserRepositoryImpl(DatabaseSQL databaseSQL) {
        this.databaseSQL = databaseSQL;
    }
    @Override
    public void save(User user) {
        try (Connection connection = databaseSQL.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = databaseSQL.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id, username, password FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
