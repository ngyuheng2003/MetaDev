package com.metadev.connect.Controller.Post;

import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;

public class PostLikeController {

    PostService postService = new PostService();

    public int addLike(Long postId, Long userId) {
        return postService.addLike(postId, userId);
    }


    public int removeLike(Long postId, Long userId) {
        return postService.removeLike(postId, userId);
    }

    public boolean getUserLoginedLikeStatus(Long post_id){
        return UserLogined.getUserLikedPost().contains(post_id);
    }

    public void refreshUserLoginedLikeStatus(Long userId){
        UserLogined.setUserLikedPost(postService.getUserLikeStatus(userId));
    }

    public void updateLikeCount(Long postId) {
        postService.updateLikeCount(postId);
    }
}