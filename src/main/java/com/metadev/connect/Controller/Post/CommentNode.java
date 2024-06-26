package com.metadev.connect.Controller.Post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int nextCommentId = 1;
    private static final int MIN_COMMENT_ID = 1;

    private int commentId;
    private String text;
    private String authorId;



    private String author;
    private boolean deleted;
    private LocalDateTime timestamp;
    private List<CommentNode> children;

    public CommentNode(String text, String authorId, String author) {
        this.commentId = nextCommentId++;
        this.text = text;
        this.authorId = authorId;
        this.author = author;
        this.deleted = false;
        this.timestamp = LocalDateTime.now();
        this.children = new ArrayList<>();
    }

    public static void setNextMinCommentId(int next){
        nextCommentId = next;
    }

    public void addChildComment(CommentNode child) {
        children.add(child);
    }

    public List<CommentNode> getChildComment() {
        return children;
    }

    public void markDeleted() {
        this.deleted = true;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    // Method to remove a child comment

    public boolean removeChild(CommentNode child) {
        return children.remove(child);
    }

    public static void updateNextCommentId(int nextId) {
        nextCommentId = nextId;
    }

    public static boolean isValidCommentId(int commentId, int totalComments) {
        return commentId >= MIN_COMMENT_ID && commentId <= totalComments;
    }

}
