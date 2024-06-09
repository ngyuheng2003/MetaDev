package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.PostDisplaying;
import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Comment;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.expression.spel.ast.PropertyOrFieldReference;
import org.springframework.security.core.parameters.P;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    // FXML Components
    @FXML private Button postSectionButton, repliesSectionButton, usernameButton;
    @FXML private VBox commentContainer;
    @FXML private VBox vbox;
    @FXML private VBox profile_postContainer, mainPane, commentPane;

    @FXML private Text profile_username, profile_bio, profile_name, profile_post_count, profile_date;
    @FXML private VBox profile_info_container;

    // Instance variables
    private User user = UserProfile.getUser();
    private PostContainerController<ProfileController> internal, postContainerControllerComment;
    private final PostDisplaying<ProfileController> display = new PostDisplaying<>();
    private ProfileController parentController;
    private List<Post> listOfPost;
    public VBox newFeedPostBox;

    // Initializing the page (Displaying post)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SimpleDateFormat dateF = new SimpleDateFormat("dd MMMM yyyy");
        profile_username.setText(user.getUsername());
        profile_date.setText(dateF.format(user.getDate_created_account()));
        if(user.getName() != null){
            profile_name.setText(user.getName());
        }
        else{
            profile_info_container.getChildren().remove(profile_name);
        }
        if(user.getBio() != null){
            profile_bio.setText(user.getBio());
        }
        else{
            profile_info_container.getChildren().remove(profile_bio);
        }

        loadProfilePost(0);
        parentController = this;
    }

    // Switch between user post and reply
    public void postSectionButtonClicked(ActionEvent event) {
        profile_postContainer.setAlignment(Pos.TOP_LEFT);
        repliesSectionButton.setId("profileNotSelectedButton");
        postSectionButton.setId("profileSelectedButton");
        loadProfilePost(0);
    }

    public void repliedSectionButtonClicked(ActionEvent event) {
        profile_postContainer.setAlignment(Pos.TOP_LEFT);
        repliesSectionButton.setId("profileSelectedButton");
        postSectionButton.setId("profileNotSelectedButton");
        loadProfilePost(1);
    }

    // Display user post/reply
    public void loadProfilePost(int type){
        PostService postService = new PostService();
        profile_postContainer.getChildren().clear();
        usernameButton.setText(UserLogined.getUsername());
        ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);
        parentController = this;
        try {
            threadPoolFetchPost.execute(()->{
                int post_count = postService.getPostCount(user.getUserId());
                System.out.println("NEWFD: Fetching New Feeds ...");
                switch(type){
                    case 0:
                        listOfPost = display.fetchPostByUserId(user.getUserId());
                        break;
                    case 1:
                        List<Comment> list = postService.getCommentByUserID(user.getUserId());
                        listOfPost.clear();
                        for(int i = 0; i < list.size(); i++){
                            listOfPost.add(postService.fetchPostByPostId(list.get(i).getPostId()).getFirst());
                        }
                }
                Platform.runLater(() -> {
                    try {
                        profile_post_count.setText(String.valueOf(post_count));
                        if(!listOfPost.isEmpty()) {
                            for (int i = 0; i < listOfPost.size(); i++) {
                                if (type == 0 && listOfPost.get(i).getStatus() == 2)
                                    continue;
                                display.displayPost(parentController, listOfPost.get(i), profile_postContainer, type + 1);
                            }
                        }
                        else{
                            String str = "";
                            if(type == 0){
                                str = "No posts available";
                            }
                            else{
                                str = "No replies available";
                            }
                            Text message = new Text(str);
                            message.setStyle("-fx-font-size: 20;");
                            profile_postContainer.setAlignment(Pos.CENTER);
                            profile_postContainer.getChildren().add(message);
                        }
                    }catch (IOException | InterruptedException | SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
                System.out.println("NEWFD: Fetching completed");
                threadPoolFetchPost.stop();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Display comment to user
    public void showCommentSection(Post post, PostContainerController<ProfileController> external) throws Exception {
        System.out.println("NEWFD: Opening comment section ...");
        commentPane.toFront();
        commentPane.setDisable(false);
        mainPane.setDisable(true);
        ThreadPool threadPoolShowComment = new ThreadPool(1,1);
        // Show the comments of the post
        threadPoolShowComment.execute(()->{
            post.updateInfo();
            Platform.runLater(() -> {
                try {
                    PostContainerController<ProfileController> postContainerController = display.displayComment(parentController, post, commentContainer, external, 0);
                    internal = postContainerController;
                    postContainerControllerComment = postContainerController;
                    System.out.println("NEWFD: Comment section opened");
                }catch (IOException | InterruptedException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
                threadPoolShowComment.stop();
        });
    }

    // Close the comment section
    public void closeCommentSection(){
        System.out.println("POSTC: Comment section closing ...");
        mainPane.toFront();
        mainPane.setDisable(false);
        commentPane.setDisable(true);
        Platform.runLater(() -> {
            commentContainer.getChildren().remove(newFeedPostBox);
            commentContainer.setStyle("-fx-background-color: transparent");
            System.out.println("POSTC: Comment section closed");
        });
    }

    // Exit from comment section by clicking outside the post container
    public void commentOuterAreaClicked(MouseEvent mouseEvent) {
        if(!postContainerControllerComment.isMouseInContainer()) {
            internal.updateParentPostContainer();
            closeCommentSection();
        }
    }

    // Navigation Pane
    public void profileButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(UserLogined.getUserLogined());
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }
}
