package com.metadev.connect.Service;

import com.metadev.connect.Controller.DataSourceConfig;
import com.metadev.connect.Entity.*;
import com.metadev.connect.Repository.PostRepository;
import com.metadev.connect.RowMapper.CommentRowMapper;
import com.metadev.connect.RowMapper.PostLikedRowMapper;
import com.metadev.connect.RowMapper.PostRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Service
public class PostService implements PostRepository, Serializable {
    @Serial
    private static final long serialVersionUID = 766744631710823462L;
    private final DataSourceConfig dataSourceConfig = new DataSourceConfig();
    private final  JdbcTemplate jdbc = new JdbcTemplate(dataSourceConfig.getDataSource());

    @Override
    public int createPost(Post post) {
        String sql = """
                    INSERT INTO 
                    [dbo].[post] 
                    ([user_id], [username], [content], [location], [tagging]) 
                    VALUES 
                    (?, ?, ?, ?, ?);
                    """;
        return jdbc.update(sql, UserLogined.getUserId(), UserLogined.getUsername(), post.getContent(), post.getLocation(), post.getTagsByString());
    }

    @Override
    public int updatePost(Post post) {
        return 0;
    }

    @Override
    public int deletePost(Post post) {
        return 0;
    }

    @Override
    public List<Post> findByContent(String content) {
        return null;
    }

    @Override
    public List<Post> findByTags(String tags) {
        return null;
    }

    @Override
    public List<Post> fetchPost() {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post]
                    ORDER BY post_created_date DESC 
                    """;
        return jdbc.query(sql, new PostRowMapper());
    }

    public List<PostLiked> fetchPostLikedByUserId(Long userId) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post_like]
                    WHERE user_id = ?
                    """;
        return jdbc.query(sql, new PostLikedRowMapper(), userId);
    }

    @Override
    public List<Post> fetchPostByUserId(Long userId) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post]
                    WHERE user_id = ?
                    ORDER BY post_created_date DESC 
                    """;
        return jdbc.query(sql, new PostRowMapper(), userId);
    }

    @Override
    public List<Post> fetchPostByPostId(Long postId) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post]
                    WHERE post_id = ?
                    ORDER BY post_created_date DESC 
                    """;
        return jdbc.query(sql, new PostRowMapper(), postId);
    }

    @Override
    public int deletePost(Long post_id) {
        String sql = """
                    UPDATE
                    [dbo].[post]
                    SET
                    [post_status] = 2
                    WHERE
                    post_id = ?
                    """;
        return jdbc.update(sql, post_id);

    }



    // Like function

    @Override
    public int getLikeCount(Long post_id) {
        String sql = """
                    SELECT
                    COUNT(post_id)
                    FROM 
                    [dbo].[post_like]
                    where post_id = ?
                    """;
        Integer like_count = jdbc.queryForObject(sql, new Object[]{post_id}, Integer.class);
        return Objects.requireNonNullElse(like_count, 0);
    }

    @Override
    public List<Long> getUserLikeStatus(Long user_id){
        String sql = """
                    SELECT
                    post_id
                    FROM 
                    [dbo].[post_like]
                    where user_id = ?
                    """;
        return jdbc.queryForList(sql, new Object[]{user_id}, Long.class);

    }

// Like Usage
    @Override
    public int addLike(Long post_id, Long user_id) {
        String sql = """
                    INSERT INTO 
                    [dbo].[post_like] 
                    ([post_id], [user_id]) 
                    VALUES 
                    (?, ?);
                    """;
        return jdbc.update(sql, post_id, user_id);
    }

    @Override
    public int removeLike(Long post_id, Long user_id) {
        String sql = """
                    DELETE FROM
                    [dbo].[post_like]
                    WHERE
                    post_id = ? AND user_id = ?
                    """;
        return jdbc.update(sql, post_id, user_id);
    }

    @Override
    public int updateLikeCount(Long post_id){
        String sql = """
                    UPDATE [dbo].[post] 
                    SET [dbo].[post].like_count 
                    = (SELECT COUNT(?) from [dbo].[post_like] WHERE [dbo].[post_like].post_id = [dbo].[post].post_id)
                    WHERE [dbo].[post].post_id = ?;
                    """;
        return jdbc.update(sql, post_id, post_id);
    }

    // Comment Usage
    @Override
    public int addComment(Long post_id, ByteArrayOutputStream byteArrayOutputStream, int totalComment, Long user_id) {
        String sql = """
                    INSERT INTO
                    [dbo].[post_comment]
                    ([post_id], [comment_OBJ], [total_comment], [top_comment_user_id])
                    VALUES
                    (?, ?, ?, ?)
                    """;
        return jdbc.update(sql, post_id, byteArrayOutputStream.toByteArray(), totalComment, user_id);

    }

    @Override
    public int udpateComment(Long post_id, ByteArrayOutputStream byteArrayOutputStream, int totalComment, int comment_OBJ_ID) {
        String sql = """
                    UPDATE
                    [dbo].[post_comment]
                    SET
                    [comment_OBJ] = ?, [total_comment] = ?
                    WHERE
                    post_id = ? AND comment_OBJ_ID = ?
                    """;
        return jdbc.update(sql, byteArrayOutputStream.toByteArray(), totalComment, post_id, comment_OBJ_ID);
    }

    @Override
    public List<Comment> getComment(Long post_id) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post_comment]
                    WHERE
                    post_id = ?
                    ORDER BY comment_created_date DESC 
                    """;
        return jdbc.query(sql, new Object[]{post_id},new CommentRowMapper());
    }
    @Override
    public List<Comment> getCommentByUserID(Long user_id) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post_comment]
                    WHERE
                    top_comment_user_id = ?
                    ORDER BY comment_created_date DESC 
                    """;
        return jdbc.query(sql, new Object[]{user_id},new CommentRowMapper());
    }

    @Override
    public List<Comment> getCommentByUserID(Long post_id, Long user_id) {
        String sql = """
                    SELECT
                    *
                    FROM 
                    [dbo].[post_comment]
                    WHERE
                    post_id = ? AND top_comment_user_id = ?
                    ORDER BY comment_created_date DESC 
                    """;
        return jdbc.query(sql, new Object[]{post_id, user_id},new CommentRowMapper());
    }

    @Override
    public int getCommentCount(Long post_id) {
        String sql = """
                    SELECT
                    SUM(total_comment)
                    FROM 
                    [dbo].[post_comment]
                    where post_id = ?
                    """;
        Integer like_count = jdbc.queryForObject(sql, new Object[]{post_id}, Integer.class);
        return Objects.requireNonNullElse(like_count, 0);
    }

    @Override
    public int updateCommentCount(Long post_id){
        String sql = """
                    UPDATE [dbo].[post] 
                    SET [dbo].[post].comment_count 
                    = (SELECT SUM(total_comment) from [dbo].[post_comment] WHERE [dbo].[post_comment].post_id = [dbo].[post].post_id)
                    WHERE [dbo].[post].post_id = ?;
                    """;
        return jdbc.update(sql, post_id);
    }
}
