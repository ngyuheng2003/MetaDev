package com.metadev.connect.RowMapper;

import com.metadev.connect.Entity.PostLiked;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostLikedRowMapper implements RowMapper<PostLiked>{
    public PostLiked mapRow(ResultSet resultSet, int i) throws SQLException {
        return new PostLiked(resultSet.getLong("post_id"),
                resultSet.getLong("user_id"));
    }
}
