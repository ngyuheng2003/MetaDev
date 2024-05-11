package com.metadev.connect.Entity;

import java.sql.Blob;

public class Comment {

    private Blob blob;



    private int commentTopID;

    public Comment(Blob blob, int commentTopID){
        this.blob = blob;
        this.commentTopID = commentTopID;
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
}
