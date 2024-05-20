package com.metadev.connect.Entity;

public class PostLiked {

    private Long postId;
    private Long userId;

    public PostLiked(Long postId, Long userId){
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }
}
