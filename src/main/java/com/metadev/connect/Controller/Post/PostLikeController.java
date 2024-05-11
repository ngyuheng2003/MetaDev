package com.metadev.connect.Controller.Post;

import com.metadev.connect.Service.PostService;

public class PostLikeController {
    private int totalNumOfLikes;
    private boolean userLikeStatus; // Added boolean variable to track whether the post has been liked

    public PostLikeController(int totalNumOfLikes, boolean userLikeStatus) {
        this.totalNumOfLikes = totalNumOfLikes;
        this.userLikeStatus = userLikeStatus; // Initialize liked to false
    }

    public int getTotalNumOfLikes() {
        return totalNumOfLikes;
    }

    public void setTotalNumOfLikes(int totalNumOfLikes) {
        this.totalNumOfLikes = totalNumOfLikes;
    }

    public boolean getUserLikeStatus() {
        return userLikeStatus;
    }

    public void setUserLikeStatus(Long post_id, Long user_id){
        PostService postService = new PostService();
        if (!userLikeStatus) {
            postService.addLike(post_id, user_id);
            userLikeStatus = true; // Set liked to true to indicate that the user has liked the post
            System.out.println("POSTL: Post like added successfully.");
            totalNumOfLikes = postService.getLikeCount(post_id);
        } else {
            postService.removeLike(post_id, user_id);
            userLikeStatus = false; // Set liked to true to indicate that the user has liked the post
            System.out.println("POSTL: Post like remove successfully.");
            totalNumOfLikes = postService.getLikeCount(post_id);
        }
    }
}
