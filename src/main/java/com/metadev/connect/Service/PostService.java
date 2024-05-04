package com.metadev.connect.Service;

import com.metadev.connect.Controller.DataSourceConfig;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Repository.PostRepository;
import com.metadev.connect.RowMapper.PostRowMapper;
import com.metadev.connect.RowMapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements PostRepository {
    private final DataSourceConfig dataSourceConfig = new DataSourceConfig();
    private final JdbcTemplate jdbc = new JdbcTemplate(dataSourceConfig.getDataSource());

    @Override
    public int createPost(Post post) {
        String sql = """
                    INSERT INTO 
                    [dbo].[post] 
                    ([user_id], [content], [location]) 
                    VALUES 
                    (?, ?, ?);
                    """;
        return jdbc.update(sql, UserLogined.getUserId(), post.getContent(), post.getLocation());
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
                    SELECT TOP 5
                    *
                    FROM 
                    [dbo].[post] 
                    """;
        return jdbc.query(sql, new PostRowMapper());
    }
}
