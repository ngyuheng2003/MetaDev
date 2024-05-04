package com.metadev.connect.RowMapper;

import com.metadev.connect.Entity.User;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getLong("user_id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("bio"),
                resultSet.getDate("date_created_account"),
                resultSet.getString("password"));
    }

}
