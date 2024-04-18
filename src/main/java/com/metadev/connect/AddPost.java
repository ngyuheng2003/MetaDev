package com.metadev.connect;

import org.springframework.stereotype.Component;

import java.util.Date;

public class AddPost {
    private String postText;
    private String[] tags;
    private String location;
    private Date postCreatedDate;

    public AddPost(String postText, String[] tags, String location){
        this.postText = postText;
        this.tags = tags;
        this.location = location;
        this.postCreatedDate = new Date();
    }
}
