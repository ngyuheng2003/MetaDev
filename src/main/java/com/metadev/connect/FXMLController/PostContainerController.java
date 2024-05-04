package com.metadev.connect.FXMLController;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Repository.UserRepository;
import com.metadev.connect.Service.UserService;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;



public class PostContainerController {
    @FXML private Text post_content;

    @FXML private Text post_createdDate;

    @FXML private ImageView post_profileImage;

    @FXML private Text post_username;

    public void setPostContainer(Post post) throws InterruptedException {
        UserService userService = new UserService();
        this.post_username.setText(userService.findUserUsernameById(post.getUserId()).getFirst());
        Image image = new Image("Images/General/defaultProfilePic_icon.png");
        this.post_profileImage.setImage(image);
        this.post_createdDate.setText(String.valueOf(post.getPostCreatedDate()));
        this.post_content.setText(post.getContent());
    }

}
