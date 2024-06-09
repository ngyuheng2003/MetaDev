package com.metadev.connect.Controller.Post;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;

import java.util.ArrayList;

public class PostCreating {

    PostService postService = new PostService();
    Post newPost;

    // Creating a post object before uploading to database
    public void convertPost(String content){
        // Handling tags
        String[] word = content.split(" ");
        ArrayList<String> tags = new ArrayList<>();
        for (String string : word) {
            if (string.charAt(0) == '#')
                tags.add(string.substring(1));
        }
        String[] tagging = new String[tags.size()];
        for(int i = 0; i < tags.size(); i++){
            tagging[i] = tags.get(i);
        }

        // Convert into a post object
        newPost = new Post(null, UserLogined.getUserId(), UserLogined.getUsername(), 0, content, tagging, null,0,  0,null);
    }

    // Upload to database
    public boolean publishPost(){
        boolean status = postService.createPost(newPost) == 1;
        if(status)
            UserLogined.setNewPost(newPost);
        return status;
    }

    // Changing the post statue to 'deleted'
    public void deletePost(Post post){
        postService.deletePost(post.getPostId());
        post.setStatus(2);
    }
}
