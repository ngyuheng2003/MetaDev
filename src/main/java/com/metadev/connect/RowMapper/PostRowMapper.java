package com.metadev.connect.RowMapper;

import com.metadev.connect.Entity.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Post(resultSet.getLong("post_id"),
                resultSet.getLong("user_id"),
                resultSet.getString("username"),
                resultSet.getInt("post_status"),
                resultSet.getString("content"),
                getTags(resultSet.getString("tagging")),
                null,
                resultSet.getInt("like_count"),
                resultSet.getInt("comment_count"),
                resultSet.getDate("post_created_date"));
    }

    public String[] getTags(String string){
        if(string != null){
            return string.split(",");
        }
        else{
            return null;
        }
    }
}
