package com.metadev.connect.Entity;

import java.sql.Blob;

public class Comment {

    private Blob blob;



    private int commentTopID;




    private Long postId;
    private Long topCommentUserId;



    private int totalComment;

    public Comment(Blob blob, int commentTopID, Long userId, Long postId, int totalComment){
        this.blob = blob;
        this.commentTopID = commentTopID;
        this.topCommentUserId = userId;
        this.postId = postId;
        this.totalComment = totalComment;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    public int getCommentTopID() {
        return commentTopID;
    }

    public void setCommentTopID(int commentTopID) {
        this.commentTopID = commentTopID;
    }

    public Long getTopCommentUserId() {
        return topCommentUserId;
    }

    public void setTopCommentUserId(Long topCommentUserId) {
        this.topCommentUserId = topCommentUserId;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }
}
