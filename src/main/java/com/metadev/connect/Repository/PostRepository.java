package com.metadev.connect.Repository;

import com.metadev.connect.Entity.Comment;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
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


    List<Post> fetchPostByUserId(Long userId);

    List<Post> fetchPostByPostId(Long postId);

    public int getLikeCount(Long post_id);

    public boolean getUserLikeStatus(Long post_id, Long user_id);

    // Like Usage
    public int addLike(Long post_id, Long user_id);
    public int removeLike(Long post_id, Long user_id);


    // Comment Usage
    int addComment(Long post_id, ByteArrayOutputStream byteArrayOutputStream, int totalComment, Long user_id);

    public int udpateComment(Long post_id, ByteArrayOutputStream byteArrayOutputStream, int totalComment, int comment_OBJ_ID);

    public List<Comment> getComment(Long post_id);

    List<Comment> getCommentByUserID(Long user_id);

    List<Comment> getCommentByUserID(Long post_id, Long user_id);

    public int getCommentCount(Long post_id);

}

