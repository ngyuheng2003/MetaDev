package com.metadev.connect.RowMapper;

import com.metadev.connect.Entity.Comment;
import com.metadev.connect.Entity.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Comment(resultSet.getBlob("comment_OBJ"),
                resultSet.getInt("comment_OBJ_ID"),
                resultSet.getLong("top_comment_user_id"),
                resultSet.getLong("post_id"));
    }
}
