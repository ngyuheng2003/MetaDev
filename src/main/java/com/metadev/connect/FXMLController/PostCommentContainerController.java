package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.PostCommentTree;
import com.metadev.connect.Controller.Post.PostLikeController;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PostCommentContainerController {
    @FXML
    private Text commentDate;
    @FXML private VBox repliedCommentContainer;
    @FXML private HBox informationBox;

    @FXML
    private Hyperlink commentReply;

    @FXML
    private Text commentText, dotText;

    @FXML
    private Hyperlink commentUsername;
    private PostContainerController postContainerController;
    private PostCommentTree postCommentTree;
    private PostCommentContainerController postCommentContainerController, postCommentContainerControllerParent;

    public ArrayList<String[]> getCommentInformation() {
        return commentInformation;
    }

    public void setCommentInformation(ArrayList<String[]> commentInformation) {
        this.commentInformation = commentInformation;
    }

    public ArrayList<PostCommentContainerController> getPostCommentContainerControllerList() {
        return postCommentContainerControllerList;
    }

    public void setPostCommentContainerControllerList(ArrayList<PostCommentContainerController> postCommentContainerControllerList) {
        this.postCommentContainerControllerList = postCommentContainerControllerList;
    }

    private ArrayList<String[]> commentInformation;
    private ArrayList<PostCommentContainerController> postCommentContainerControllerList = new ArrayList<>();
    private String content;
    private String username;
    private String date;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    private String commentId;

    public int getComment_OBJ_ID() {
        return comment_OBJ_ID;
    }

    public void setComment_OBJ_ID(int comment_OBJ_ID) {
        this.comment_OBJ_ID = comment_OBJ_ID;
    }

    private int comment_OBJ_ID;
    private String depth;
    private Blob blob;




    public void setParentController(PostContainerController postContainerController){
        this.postContainerController = postContainerController;
    }

    public void setParentCommentController(PostCommentContainerController postCommentContainerController){
        this.postCommentContainerControllerParent = postCommentContainerController;
    }
    
    public void setPostCommentTree(PostCommentTree postCommentTree){
        this.postCommentTree = postCommentTree;
    }
    
    public void getCommentInformationFromObj(){
        commentInformation = postCommentTree.displayAllComments();
    }
    

    public void setPostCommentContainer(PostCommentTree postCommentTree, int type) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
        username = commentInformation.get(0)[1];
        content = commentInformation.get(0)[0];
        date = commentInformation.get(0)[2];
        commentId = commentInformation.get(0)[3];
        depth = commentInformation.get(0)[4];
        System.out.println("Size: " + commentInformation.size());

        if(content.length() > 50)
            commentText.setWrappingWidth(320);
        commentUsername.setText(username);
        commentText.setText(content);
        commentDate.setText(date);
        System.out.println("Printing layer 1 comment");

        if(Integer.parseInt(depth)==2){
            informationBox.getChildren().remove(commentReply);
            informationBox.getChildren().remove(dotText);
        }

        commentInformation.removeFirst();
        if(!commentInformation.isEmpty()) {
            if (Integer.parseInt(depth) < Integer.parseInt(commentInformation.get(0)[4])) {
                postCommentContainerControllerList.add(this);
                addRepliedComment();
            }
            else if(Integer.parseInt(depth) >= Integer.parseInt(commentInformation.get(0)[4])){
                for(int i =0; i < (postCommentContainerControllerList.size() - Integer.parseInt(commentInformation.get(0)[4])); i++)
                    postCommentContainerControllerList.removeLast();
                postCommentContainerControllerList.getLast().addRepliedComment();
            }
            else{
            }


        }
    }
    
    public void addRepliedComment() throws IOException, SQLException, InterruptedException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostCommentContainer.fxml"));
        VBox postCommentBox = fxmlLoader.load();
        postCommentContainerController = fxmlLoader.getController();
        postCommentContainerController.setParentController(postContainerController);
        postCommentContainerController.setParentCommentController(this);
        postCommentContainerController.setCommentInformation(commentInformation);
        postCommentContainerController.setPostCommentContainerControllerList(postCommentContainerControllerList);
        postCommentContainerController.setPostCommentTree(postCommentTree);
        postCommentContainerController.setComment_OBJ_ID(getComment_OBJ_ID());
        postCommentContainerController.setPostCommentContainer(postCommentTree, 1);
        repliedCommentContainer.getChildren().add(0, postCommentBox);
    }

    public void commentReplyClicked(ActionEvent event) {
        postContainerController.setRepliedInformation(1, postCommentTree, Integer.parseInt(commentId), comment_OBJ_ID, username);
    }
}
