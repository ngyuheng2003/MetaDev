package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.CommentNode;
import com.metadev.connect.Controller.Post.PostCommentTree;
import com.metadev.connect.Controller.Post.PostLikeController;
import com.metadev.connect.Entity.Comment;
import com.metadev.connect.Entity.Post;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;


public class PostContainerController {
    @FXML private Button postMenuButton, postLikeButton, addCommentButton;

    @FXML private VBox postContainer,postMenuContainer, commentInputContainer, postCommentContainer, commentContainerMessageBox,upperContainer;

    @FXML private Text post_content, commentContainerMessage;

    @FXML private Text post_createdDate;

    @FXML private ImageView post_profileImage;

    @FXML private Text post_like_counter, post_comment_counter;
    @FXML private Hyperlink post_username;
    @FXML private TextArea commentTF;
    private PostCommentContainerController postCommentContainerController;
    private  PostService postService = new PostService();
    private PostLikeController like;
    private Post post;
    private ThreadPool threadPoolPostContainer;
    private NewsFeedController newsFeedController;
    private PostContainerController parentController;
    private int typeOfPost;
    private int typeOfComment = 0;
    private PostCommentTree postCommentTreeReplied;
    private int commentIdReplied;



    private int comment_OBJ_ID;

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

    public void setPostContainer(Post post, int type) throws InterruptedException {
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

        this.typeOfPost = type;
        if(typeOfPost == 1){
            postContainer.getChildren().remove(commentInputContainer);
            upperContainer.getChildren().remove(postCommentContainer);
        }

        post_comment_counter.setText(String.valueOf(postService.getCommentCount(post.getPostId())));

        if(Integer.parseInt(post_comment_counter.getText()) == 0){
            commentContainerMessage.setText("Be the first to comment on this post");
        }
        else{
            postCommentContainer.getChildren().remove(commentContainerMessageBox);
        }
        addCommentButton.setDisable(true);
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
            System.out.println(1);
        }
        postContainer.requestFocus();
    }

    // Post Like

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

    public void postUsernameHyperLinkClicked(ActionEvent event){
        post_username.setVisited(false);
    }

    // Post Comment
    public void commentButtonClicked(ActionEvent event) throws IOException, SQLException, InterruptedException, ClassNotFoundException {
        if(!isCommentSection) {
            newsFeedController.showCommentSection(post);

        }
        else{
            newsFeedController.closeCommentSection();
            setCommentSection(false);
        }
    }

    public void mouseEnterContainer(MouseEvent mouseEvent) {
        isMouseInContainer = true;
    }

    public void mouseExitContainer(MouseEvent mouseEvent) {
        isMouseInContainer = false;
    }

    public void checkTypeOfPost() {

    }

    public void displayComment() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        // Setting parent controller (PostContainerController)
        parentController = this;
        // Getting list of comment from database
        List<Comment> listOfComment = postService.getComment(post.getPostId());
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
            postCommentContainerController.setComment_OBJ_ID(listOfComment.get(i).getCommentTopID());
            postCommentTree.setCommentID(postService.getCommentCount(post.getPostId()));
            postCommentContainerController.setPostCommentContainer(postCommentTree, 1);
            postCommentContainer.getChildren().add(postCommentBox);
            System.out.println(2);
        }
    }

    public void addCommentButtonClicked(ActionEvent event) throws Exception {
        String commentInput = commentTF.getText();
        switch(typeOfComment){
            case 0:
                PostCommentTree postCommentTree = new PostCommentTree(post.getPostId());
                postCommentTree.setCommentID(0);
                postCommentTree.addComment(-1, new CommentNode(commentInput, UserLogined.getUsername()));
                threadPoolPostContainer = new ThreadPool(1,1);
                threadPoolPostContainer.execute(()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("POSTC: Saving comment ...");
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
                                objectOut.writeObject(postCommentTree);
                                postService.addComment(post.getPostId(), byteArrayOutputStream, postCommentTree.getTotalComments());
                                objectOut.close();
                                byteArrayOutputStream.close();
                                System.out.println("POSTC: Comment saved");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // Terminating the thread
                    threadPoolPostContainer.stop();
                });
                break;
            case 1:
                postCommentTreeReplied.addComment(commentIdReplied, new CommentNode(commentInput, UserLogined.getUsername()));
                threadPoolPostContainer = new ThreadPool(1,1);
                threadPoolPostContainer.execute(()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("POSTC: Saving comment ...");
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
                                objectOut.writeObject(postCommentTreeReplied);
                                postService.udpateComment(post.getPostId(), byteArrayOutputStream, postCommentTreeReplied.getTotalComments(),comment_OBJ_ID);
                                objectOut.close();
                                byteArrayOutputStream.close();
                                System.out.println("POSTC: Comment saved");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // Terminating the thread
                    threadPoolPostContainer.stop();
                });
        }


    }

    public void setRepliedInformation(int typeOfComment, PostCommentTree postCommentTree, int commentIdReplied, int comment_OBJ_ID, String username){
        this.typeOfComment = typeOfComment;
        this.postCommentTreeReplied = postCommentTree;
        this.commentIdReplied = commentIdReplied;
        this.comment_OBJ_ID = comment_OBJ_ID;
        this.commentTF.setPromptText("Replying to " + username);
        System.out.println("Set reply successfully");
    }

    public void commentKeyTyped(KeyEvent event) {
        if(commentTF.getText().isBlank()){
            addCommentButton.setDisable(true);
        }
        else{
            addCommentButton.setDisable(false);
        }
    }
}
