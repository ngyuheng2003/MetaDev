package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.PostLikeController;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class PostContainerController {
    @FXML private Button postMenuButton, postLikeButton;

    @FXML private VBox postMenuContainer;

    @FXML private Text post_content;

    @FXML private Text post_createdDate;

    @FXML private ImageView post_profileImage;

    @FXML private Text post_username, post_like_counter;
    private PostLikeController like;
    private Post post;
    private ThreadPool threadPoolPostContainer;

    public void setPostContainer(Post post) throws InterruptedException {
        this.post = post;
        // Setting profile image
        Image image = new Image("Images/General/defaultProfilePic_icon.png");
        this.post_profileImage.setImage(image);
        // Setting username
        UserService userService = new UserService();
        this.post_username.setText(userService.findUserUsernameById(post.getUserId()).getFirst());
        // Setting like count
        PostService postService = new PostService();
        like = new PostLikeController(
                postService.getLikeCount(post.getPostId()),
                postService.getUserLikeStatus(post.getPostId(), UserLogined.getUserId()
                ));
        if(like.getUserLikeStatus()){
            postLikeButton.setId("postLikeTrue");
            ImageView icon = new ImageView("Images/General/love_icon_resized_red.png");
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            postLikeButton.setGraphic(icon);
        }
        else{
            postLikeButton.setId("postLikeFalse");
            ImageView icon = new ImageView("Images/General/love_icon_resized.png");
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            postLikeButton.setGraphic(icon);
        }
        post_like_counter.setText(String.valueOf(like.getTotalNumOfLikes()));


        this.post_createdDate.setText(String.valueOf(post.getPostCreatedDate()));
        this.post_content.setText(post.getContent());
    }

    public void postMenuButtonClicked(ActionEvent event) {
        if(postMenuContainer.getOpacity() == 0){
            postMenuContainer.setOpacity(1);
            postMenuContainer.setDisable(false);
        }else{
            postMenuContainer.setOpacity(0);
            postMenuContainer.setDisable(true);
        }
    }

    public void containerMouseClicked(MouseEvent mouseEvent) {
        if(postMenuContainer.getOpacity() == 1){
            postMenuContainer.setOpacity(0);
            postMenuContainer.setDisable(true);
        }
    }

    public void postLikeButtonClicked(ActionEvent event) throws Exception {
        // Creating a thread for like function
        threadPoolPostContainer = new ThreadPool(1, 1);
        // Using the thread for like function to database
        threadPoolPostContainer.execute(()->{
            System.out.println("NEWFD: Adding/Removing like ...");
            // Checking and change status for user's like
            like.setUserLikeStatus(post.getPostId(), UserLogined.getUserId());
            // Update the like count
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    post_like_counter.setText(String.valueOf(like.getTotalNumOfLikes()));
                    if(like.getUserLikeStatus()){
                        postLikeButton.setId("postLikeTrue");
                        ImageView icon = new ImageView("Images/General/love_icon_resized_red.png");
                        icon.setFitHeight(20);
                        icon.setFitWidth(20);
                        postLikeButton.setGraphic(icon);
                    }
                    else{
                        postLikeButton.setId("postLikeFalse");
                        ImageView icon = new ImageView("Images/General/love_icon_resized.png");
                        icon.setFitHeight(20);
                        icon.setFitWidth(20);
                        postLikeButton.setGraphic(icon);
                    }
                }

            });
            // Terminating the thread
            threadPoolPostContainer.stop();
        });
    }
}
