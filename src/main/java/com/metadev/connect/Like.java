/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testmain;

public class Like {
    private int likes;
    private int totalNumOfLikes;
    private boolean liked; // Added boolean variable to track whether the post has been liked

    public Like() {
        this.likes = 0;
        this.totalNumOfLikes = 0;
        this.liked = false; // Initialize liked to false
    }

    public int getLikes() {
        return likes;
    }

    public int getTotalNumOfLikes() {
        return totalNumOfLikes;
    }

    public void likePost() {
        if (!liked) {
            likes++;
            totalNumOfLikes++; // Increment total number of likes
            liked = true; // Set liked to true to indicate that the user has liked the post
            System.out.println("Post liked successfully.");
            System.out.println("Total number of likes: " + totalNumOfLikes);
        } else {
            System.out.println("You have already liked this post.");
        }
    }
}
