package com.metadev.connect.Entity;

import org.springframework.stereotype.Component;

import java.util.Date;


public class Post {

    private Long postId;
    private Long userId;
    private String content;
    private String[] tags;
    private String location;
    private int likeCount;



    private int commentCount;
    private Date postCreatedDate;

    public Post(Long postId, Long userId, String content, String[] tags, String location, int likeCount, int commentCount, Date postCreatedDate) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.tags = tags;
        this.location = location;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postCreatedDate = postCreatedDate;
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

    public Date getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(Date postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
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


