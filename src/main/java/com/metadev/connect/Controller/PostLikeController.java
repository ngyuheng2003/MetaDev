package com.metadev.connect.Controller;

import com.metadev.connect.Service.PostService;

public class PostLikeController {
    private int totalNumOfLikes;
    private boolean liked; // Added boolean variable to track whether the post has been liked

    public PostLikeController(int totalNumOfLikes, boolean userLikeStatus) {
        this.totalNumOfLikes = totalNumOfLikes;
        this.liked = userLikeStatus; // Initialize liked to false
    }

    public int getTotalNumOfLikes() {
        return totalNumOfLikes;
    }

    public void setTotalNumOfLikes(int totalNumOfLikes) {
        this.totalNumOfLikes = totalNumOfLikes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setUserLikeStatus(Long post_id, Long user_id){
        PostService postService = new PostService();
        if (!liked) {
            postService.addLike(post_id, user_id);
            liked = true; // Set liked to true to indicate that the user has liked the post
            System.out.println("POSTL: Post like added successfully.");
            totalNumOfLikes = postService.getLikeCount(post_id);
        } else {
            postService.removeLike(post_id, user_id);
            liked = false; // Set liked to true to indicate that the user has liked the post
            System.out.println("POSTL: Post like remove successfully.");
            totalNumOfLikes = postService.getLikeCount(post_id);
        }
    }
}
