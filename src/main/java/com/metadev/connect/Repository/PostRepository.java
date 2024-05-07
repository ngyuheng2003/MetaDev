package com.metadev.connect.Repository;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository {

    // Creation or Deletion of post
    // it will return 1 for success and 0 for failure
    public int createPost(Post post);

    public int updatePost(Post post);

    public int deletePost(Post post);

    //Searching of post
    public List<Post> findByContent(String content);

    public List<Post> findByTags(String tags);

    public List<Post> fetchPost();
    public int getLikeCount(Long post_id);

    public boolean getUserLikeStatus(Long post_id, Long user_id);

    public int addLike(Long post_id, Long user_id);
    public int removeLike(Long post_id, Long user_id);

}

