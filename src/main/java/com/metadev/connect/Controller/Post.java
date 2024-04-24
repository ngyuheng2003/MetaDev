package com.metadev.connect.Controller;

import java.util.Date;

public class Post {

    private String postText;
    private String[] tags;
    private String location;
    private Date postCreatedDate;

    public Post(String postText, String[] tags, String location){
        this.postText = postText;
        this.tags = tags;
        this.location = location;
        this.postCreatedDate = new Date();
    }
}
