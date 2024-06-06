package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.CommentNode;
import com.metadev.connect.Controller.Post.PostCommentTree;
import com.metadev.connect.Controller.Post.PostLikeController;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PostContainerController {
    @FXML private Button postMenuButton, postLikeButton, addCommentButton, postDeleteButton;

    @FXML private VBox postContainer,postMenuContainer, commentInputContainer, postCommentContainer, commentContainerMessageBox,upperContainer;

    @FXML private Text post_content, commentContainerMessage;

    @FXML private Text post_createdDate;

    @FXML private ImageView post_profileImage;

    @FXML private Text post_like_counter, post_comment_counter;
    @FXML private Hyperlink post_username;
    @FXML private TextArea commentTF;
    private PostCommentContainerController postCommentContainerController, postCommentContainerControllerReplied;
    private final PostService postService = new PostService();
    private Post post;
    private ThreadPool threadPoolPostContainer;
    private NewsFeedController newsFeedController;
    private SearchController searchController;
    private PostContainerController parentController, external;
    private ProfileController profileController;
    private PostLikeController like = new PostLikeController();
    private int typeOfPost;
    private int typeOfComment = 0;
    private PostCommentTree postCommentTreeReplied;
    private int commentIdReplied, comment_OBJ_ID;
    public void setComment_OBJ_ID(int comment_OBJ_ID) {
        this.comment_OBJ_ID = comment_OBJ_ID;
    }
    public boolean isCommentSection() {
        return isCommentSection;
    }

    public void setCommentSection(boolean commentSection) {
        isCommentSection = commentSection;
    }

    private boolean isCommentSection = false;
    private boolean isMouseInContainer = false;

    public boolean isMouseInContainer() {
        return isMouseInContainer;
    }

    public void setMouseInContainer(boolean mouseInContainer) {
        isMouseInContainer = mouseInContainer;
    }

    public void setParentController(NewsFeedController newsFeedController){
        this.newsFeedController = newsFeedController;
    }

    public void setSearchController(SearchController searchController){
        this.searchController = searchController;
    }
    public void setExternalPostController(PostContainerController postContainerController){
        this.external = postContainerController;
    }

    public void setProfileController(ProfileController profileController){
        this.profileController = profileController;
    }

    public void setPostContainer(Post post, int type) throws InterruptedException, SQLException, IOException, ClassNotFoundException {
        this.post = post;
        // Setting profile image
        Image image = new Image("Images/General/defaultProfilePic_icon.png");
        this.post_profileImage.setImage(image);
        // Setting username
        this.post_username.setText(post.getUsername());
        // Setting like count
        if(like.getUserLoginedLikeStatus(post.getPostId())){
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
        if(post.getStatus() == 2) {
            post.setContent("Post deleted by author");
            this.post_content.setText(post.getContent());
            post_content.setStyle("-fx-font-style: italic");
        }
        else{
            this.post_content.setText(post.getContent());
        }
        post_like_counter.setText(String.valueOf(post.getLikeCount()));
        this.post_createdDate.setText(String.valueOf(post.getPostCreatedDate()));


        this.typeOfPost = type;
        if(typeOfPost == 1){
            postContainer.getChildren().remove(commentInputContainer);
            upperContainer.getChildren().remove(postCommentContainer);
        }else if(typeOfPost == 2){
            postContainer.getChildren().remove(commentInputContainer);
            postCommentContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
            displayComment();
        }

        post_comment_counter.setText(String.valueOf(post.getCommentCount()));

        if(Integer.parseInt(post_comment_counter.getText()) == 0){
            commentContainerMessage.setText("Be the first to comment on this post");
        }
        else{
            postCommentContainer.getChildren().remove(commentContainerMessageBox);
        }
        addCommentButton.setDisable(true);
        if(post.getUserId() != UserLogined.getUserId())
            postDeleteButton.setDisable(true);

    }


    public void postMenuButtonClicked(ActionEvent event) {
        if(postMenuContainer.getOpacity() == 0){
            postMenuContainer.setOpacity(1);
            postMenuContainer.setDisable(false);
            if(post.getUserId() == UserLogined.getUserId() && post.getStatus() != 2)
                postDeleteButton.setDisable(false);
        }else{
            postMenuContainer.setOpacity(0);
            postMenuContainer.setDisable(true);
            postDeleteButton.setDisable(true);
        }
    }

    public void containerMouseClicked(MouseEvent mouseEvent) {
        if(postMenuContainer.getOpacity() == 1){
            postMenuContainer.setOpacity(0);
            postMenuContainer.setDisable(true);
            System.out.println(1);
        }
        postContainer.requestFocus();

        setRepliedInformation(0, null, 0, 0, null, null);
    }

    // Post Like

    public void postLikeButtonClicked(ActionEvent event) throws Exception {
        updateCountGUI();
        // Creating a thread for like function
        threadPoolPostContainer = new ThreadPool(1, 1);
        // Using the thread for like function to database
        threadPoolPostContainer.execute(()->{
            System.out.println("NEWFD: Adding/Removing like ...");
            int sqlStatus = 0;
            // Checking and change status for user's like
            if (!like.getUserLoginedLikeStatus(post.getPostId())) {
                sqlStatus = like.addLike(post.getPostId(), UserLogined.getUserId());
                like.refreshUserLoginedLikeStatus(UserLogined.getUserId()); // Set liked to true to indicate that the user has liked the post
                System.out.println("POSTL: Post like added successfully.");
            } else {
                sqlStatus = like.removeLike(post.getPostId(), UserLogined.getUserId());
                like.refreshUserLoginedLikeStatus(UserLogined.getUserId()); // Set liked to true to indicate that the user has liked the post
                System.out.println("POSTL: Post like remove successfully.");
            }
            like.updateLikeCount(post.getPostId());
            post.updateInfo();
            int finalSqlStatus = sqlStatus;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(finalSqlStatus == 0) {
                        updateCountGUI();
                        System.out.println("POSTL: Post like add/remove failed.");
                    }
                }
            });
            // Terminating the thread
            threadPoolPostContainer.stop();
        });
    }

    // Update GUI for Like count and Comment count
    public void updateCountGUI(){
        if(!like.getUserLoginedLikeStatus(post.getPostId())){
            post_like_counter.setText(String.valueOf(Integer.parseInt(post_like_counter.getText())+ 1));
            postLikeButton.setId("postLikeTrue");
            ImageView icon = new ImageView("Images/General/love_icon_resized_red.png");
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            postLikeButton.setGraphic(icon);
        }
        else{
            post_like_counter.setText(String.valueOf(Integer.parseInt(post_like_counter.getText()) - 1));
            postLikeButton.setId("postLikeFalse");
            ImageView icon = new ImageView("Images/General/love_icon_resized.png");
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            postLikeButton.setGraphic(icon);
        }
    }

    // Show profile page of the post owner
    public void postUsernameHyperLinkClicked(ActionEvent event) throws Exception {
        post_username.setVisited(false);
        newsFeedController.showLoadingPane();
        ThreadPool threadPoolUsername = new ThreadPool(1, 1);
        // Using the thread for like function to database
        threadPoolUsername.execute(()->{
            UserService userService = new UserService();
            UserProfile.setUser(userService.findUserInfoById(post.getUserId()).getFirst());
            // Update the like count
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new StartUp(event, "/FXMLView/ProfileView.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Terminating the thread
            threadPoolUsername.stop();
        });
    }

    // Post Comment
    public void commentButtonClicked(ActionEvent event) throws Exception {
        if(newsFeedController != null) {
            if (!isCommentSection) {
                newsFeedController.showCommentSection(post, this);
            } else {
                newsFeedController.closeCommentSection();
                setCommentSection(false);
            }
        }
        else if(searchController != null){
            if (!isCommentSection) {
                searchController.showCommentSection(post, this);
            } else {
                searchController.closeCommentSection();
                setCommentSection(false);
            }
        }else{
            if (!isCommentSection) {
                profileController.showCommentSection(post, this);
            } else {
                profileController.closeCommentSection();
                setCommentSection(false);
            }
        }
    }

    public void mouseEnterContainer(MouseEvent mouseEvent) {
        isMouseInContainer = true;
    }

    public void mouseExitContainer(MouseEvent mouseEvent) {
        isMouseInContainer = false;
    }

    public void displayComment() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        // Setting parent controller (PostContainerController)
        parentController = this;
        List<Comment> listOfComment;
        // Getting list of comment from database
        if(typeOfPost == 2){
            listOfComment = postService.getCommentByUserID(post.getPostId(), UserProfile.getUserId());
        }else{
            listOfComment = postService.getComment(post.getPostId());
        }
        // Loading the comment into the post
        for(int i = 0; i < listOfComment.size(); i++) {
            // Convert blob to object
            ObjectInputStream objectIn = new ObjectInputStream(listOfComment.get(i).getBlob().getBinaryStream());
            PostCommentTree postCommentTree = (PostCommentTree) objectIn.readObject();
            objectIn.close();
            // Needed element
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostCommentContainer.fxml"));
            VBox postCommentBox = fxmlLoader.load();
            postCommentContainerController = fxmlLoader.getController();
            postCommentContainerController.setParentController(parentController);
            postCommentContainerController.setPostCommentTree(postCommentTree);
            postCommentContainerController.getCommentInformationFromObj();
            postCommentContainerController.setPost(post);
            postCommentContainerController.setComment_OBJ_ID(listOfComment.get(i).getCommentTopID());
            postCommentTree.setCommentID(listOfComment.get(i).getTotalComment());
            postCommentContainerController.setPostCommentContainer(postCommentTree, 1);
            postCommentContainer.getChildren().add(postCommentBox);
        }
    }

    public void addCommentButtonClicked(ActionEvent event) throws Exception {
        String commentInput = commentTF.getText();
        commentTF.setDisable(true);
        addCommentButton.setDisable(true);
        switch(typeOfComment){
            case 0:
                PostCommentTree postCommentTree = new PostCommentTree(post.getPostId());
                postCommentTree.setCommentID(0);
                postCommentTree.addComment(-1, new CommentNode(commentInput, String.valueOf(UserLogined.getUserId()), UserLogined.getUsername()));
                threadPoolPostContainer = new ThreadPool(1,1);
                threadPoolPostContainer.execute(()->{
                    try {
                        System.out.println("POSTC: Saving comment ...");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
                        objectOut.writeObject(postCommentTree);
                        postService.addComment(post.getPostId(), byteArrayOutputStream, postCommentTree.getTotalComments(), UserLogined.getUserId());
                        objectOut.close();
                        byteArrayOutputStream.close();
                        System.out.println("POSTC: Comment saved");
                        post.updateInfo();
                        postService.updateCommentCount(post.getPostId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostCommentContainer.fxml"));
                                VBox postCommentBox = fxmlLoader.load();
                                postCommentContainerController = fxmlLoader.getController();
                                postCommentContainerController.setParentController(parentController);
                                postCommentContainerController.setPostCommentTree(postCommentTree);
                                postCommentContainerController.getCommentInformationFromObj();
                                postCommentContainerController.setPost(post);
                                postCommentContainerController.setComment_OBJ_ID(0);
                                postCommentTree.setCommentID(postService.getCommentCount(post.getPostId()));
                                postCommentContainerController.setPostCommentContainer(postCommentTree, 1);
                                postCommentContainer.getChildren().add(postCommentBox);
                                if(commentContainerMessageBox != null){
                                    postCommentContainer.getChildren().remove(commentContainerMessageBox);
                                }
                                commentTF.clear();
                                addCommentButton.setDisable(true);
                                commentTF.setDisable(false);
                                post_comment_counter.setText(String.valueOf(Integer.parseInt(post_comment_counter.getText()) + 1));
                            } catch (IOException | SQLException | InterruptedException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    // Terminating the thread
                    threadPoolPostContainer.stop();
                });
                break;
            case 1:
                postCommentTreeReplied.addComment(commentIdReplied, new CommentNode(commentInput, String.valueOf(UserLogined.getUserId()), UserLogined.getUsername()));
                threadPoolPostContainer = new ThreadPool(1,1);
                threadPoolPostContainer.execute(()->{
                    try {
                        System.out.println("POSTC: Saving comment ...");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
                        objectOut.writeObject(postCommentTreeReplied);
                        postService.udpateComment(post.getPostId(), byteArrayOutputStream, postCommentTreeReplied.getTotalComments(),comment_OBJ_ID);
                        objectOut.close();
                        byteArrayOutputStream.close();
                        System.out.println("POSTC: Comment saved");
                        post.updateInfo();
                        postService.updateCommentCount(post.getPostId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<String[]> list = postCommentContainerControllerReplied.getCommentInformation();
                                list.add(new String[]{commentInput, String.valueOf(UserLogined.getUserId()), "just now",
                                        String.valueOf(commentIdReplied), String.valueOf(Integer.parseInt(postCommentContainerControllerReplied.getDepth()) + 1), UserLogined.getUsername()});
                                postCommentContainerControllerReplied.setCommentInformation(list);
                                postCommentContainerControllerReplied.addRepliedComment();
                            } catch (IOException | SQLException | InterruptedException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            commentTF.clear();
                            addCommentButton.setDisable(true);
                            commentTF.setDisable(false);
                            post_comment_counter.setText(String.valueOf(Integer.parseInt(post_comment_counter.getText()) + 1));
                        }
                    });
                    // Terminating the thread
                    threadPoolPostContainer.stop();
                });
        }
    }

    public void setRepliedInformation(int typeOfComment, PostCommentTree postCommentTree, int commentIdReplied, int comment_OBJ_ID, String username, PostCommentContainerController postCommentContainerControllerReplied){
        this.typeOfComment = typeOfComment;
        this.postCommentTreeReplied = postCommentTree;
        this.commentIdReplied = commentIdReplied;
        this.comment_OBJ_ID = comment_OBJ_ID;
        this.postCommentContainerControllerReplied = postCommentContainerControllerReplied;
        if(typeOfComment != 0)
            this.commentTF.setPromptText("Replying to " + username);
        else
            this.commentTF.setPromptText("Write a comment ...");
        System.out.println("POSTC: Set reply successfully");
    }

    public void commentKeyTyped(KeyEvent event) {
        if(commentTF.getText().isBlank()){
            addCommentButton.setDisable(true);
        }
        else{
            addCommentButton.setDisable(false);
        }
    }

    // Update post info after exiting comment section
    public void updateParentPostContainer(){
        if(external != null) {
            System.out.println("POSTS: Updating post info ...");
            if (like.getUserLoginedLikeStatus(post.getPostId())) {
                external.postLikeButton.setId("postLikeTrue");
                ImageView icon = new ImageView("Images/General/love_icon_resized_red.png");
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                external.postLikeButton.setGraphic(icon);
            } else {
                external.postLikeButton.setId("postLikeFalse");
                ImageView icon = new ImageView("Images/General/love_icon_resized.png");
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                external.postLikeButton.setGraphic(icon);
            }
            external.post_like_counter.setText(String.valueOf(post.getLikeCount()));
            external.post_comment_counter.setText(String.valueOf(post.getCommentCount()));
        }
    }

    // Delete post function
    public void postDeleteButtonClicked(ActionEvent event) throws Exception {
        ThreadPool threadPoolDeletePost = new ThreadPool(1,1);
        postDeleteButton.setDisable(true);
        threadPoolDeletePost.execute(()->{
            System.out.println("POSTS: Deleting post ...");
            postService.deletePost(post.getPostId());
            post.setStatus(2);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    post.setContent("Post deleted by author");
                    post_content.setText("Post deleted by author");
                    post_content.setStyle("-fx-font-style: italic");
                }
            });
            System.out.println("POSTS: Delete successfully");
            threadPoolDeletePost.stop();
        });
    }
}
