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
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getInt("status"),
                getSuggestedPreferredTopic(resultSet.getString("suggested_preferred_topic")));
    }
    public int[] getSuggestedPreferredTopic(String string){
        if(string != null){
            int[] list = new int[9];
            String[] str = string.split(",");
            for(int i = 0; i < str.length; i++){
                list[i] = Integer.parseInt(str[i]);
            }
            return list;
        }
        else{
            return null;
        }
    }

}
