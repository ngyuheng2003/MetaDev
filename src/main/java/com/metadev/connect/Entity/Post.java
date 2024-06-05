package com.metadev.connect.Entity;

import com.metadev.connect.Service.PostService;
import org.springframework.stereotype.Component;

import java.util.Date;


public class Post {

    private Long postId;
    private Long userId;

    private String username;

    private int status;
    private String content;
    private String[] tags;
    private String location;
    private int likeCount;
    private int commentCount;
    private String postCreatedDate;

    public Post(Long postId, Long userId, String username, int status, String content, String[] tags, String location, int likeCount, int commentCount, Date postCreatedDate) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.content = content;
        this.tags = tags;
        this.location = location;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postCreatedDate = String.valueOf(postCreatedDate);
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(String postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void updateInfo(){
        PostService postService = new PostService();
        likeCount = postService.getLikeCount(postId);
        commentCount = postService.getCommentCount(postId);

    }

    public String getTagsByString(){
        String tagging = "";
        if(tags != null) {
            for (int i = 0; i < tags.length; i++) {
                if (i == tags.length - 1) {
                    tagging += tags[i];
                } else {
                    tagging += tags[i] + ",";
                }

            }
            return tagging;
        }
        else{
            return null;
        }

    }
}


