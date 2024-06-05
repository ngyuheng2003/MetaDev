package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.PostCommentTree;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostCommentContainerController {
    @FXML
    private Text commentDate;
    @FXML private VBox repliedCommentContainer;
    @FXML private HBox informationBox;

    @FXML
    private Hyperlink commentReply;

    @FXML
    private Text commentText, dotText, dotText1;

    @FXML
    private Hyperlink commentUsername, postCommentDelete;
    @FXML private Button commentMenuButton;
    private PostContainerController postContainerController;
    private PostCommentTree postCommentTree;
    private PostCommentContainerController postCommentContainerController, postCommentContainerControllerParent;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    private Post post;


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
    private ThreadPool threadPoolPostCommentContainer;

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

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

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
        UserService userService = new UserService();
        username = userService.findUserUsernameById(Long.parseLong(commentInformation.get(0)[1])).getFirst();
        content = commentInformation.get(0)[0];
        date = commentInformation.get(0)[2];
        commentId = commentInformation.get(0)[3];
        depth = commentInformation.get(0)[4];
        System.out.println("Size: " + commentInformation.size());

        if(content.length() > 50 && Integer.parseInt(depth)==0)
            commentText.setWrappingWidth(320);
        else if(content.length() > 40 && Integer.parseInt(depth)==1)
            commentText.setWrappingWidth(255);
        else if(content.length() > 30 && Integer.parseInt(depth)==2)
            commentText.setWrappingWidth(190);
        commentUsername.setText(username);
        commentText.setText(content);
        commentDate.setText(date);
        System.out.println("Printing layer 1 comment");
        if(commentText.getText().contains("Comment deleted") || commentText.getText().contains("Comment deleted by owner")){
            commentText.setStyle("-fx-font-style: italic");
        }

        if(Integer.parseInt(depth)==2 || commentText.getText().equals("Comment deleted") || commentText.getText().equals("Comment deleted by owner")){
            informationBox.getChildren().remove(commentReply);
            informationBox.getChildren().remove(dotText);
        }
        if(Integer.parseInt(commentInformation.get(0)[1]) != UserLogined.getUserId() && post.getUserId() != UserLogined.getUserId()
                || commentText.getText().equals("Comment deleted") || commentText.getText().equals("Comment deleted by owner")){
            informationBox.getChildren().remove(postCommentDelete);
            informationBox.getChildren().remove(dotText1);
            System.out.println("???");
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
        postCommentContainerController.setPost(post);
        postCommentContainerController.setComment_OBJ_ID(getComment_OBJ_ID());
        postCommentContainerController.setPostCommentContainer(postCommentTree, 1);
        repliedCommentContainer.getChildren().add(0, postCommentBox);
    }

    public void commentReplyClicked(ActionEvent event) {
        PostCommentContainerController pCCC = this;
        postContainerController.setRepliedInformation(1, postCommentTree, Integer.parseInt(commentId), comment_OBJ_ID, username, pCCC);
        commentReply.setVisited(false);
    }


    public void commentButtonClicked(ActionEvent event) {
    }

    public void mouseEnterCommentContainer(MouseEvent mouseEvent) {
        commentMenuButton.setDisable(false);
        commentMenuButton.setVisible(true);
    }

    public void mouseExitedCommentContainer(MouseEvent mouseEvent) {
        commentMenuButton.setDisable(true);
        commentMenuButton.setVisible(false);
    }

    public void commentDeleteClicked(ActionEvent event) throws Exception {
        if(Integer.parseInt(commentId) != UserLogined.getUserId()){
            postCommentTree.deleteComment(Integer.parseInt(commentId), "Comment deleted");
        }
        else if(post.getUserId() != UserLogined.getUserId()){
            postCommentTree.deleteComment(Integer.parseInt(commentId), "Comment deleted by owner");
        }
        threadPoolPostCommentContainer = new ThreadPool(1,1);
        PostService postService = new PostService();
        threadPoolPostCommentContainer.execute(()->{
            try {
                System.out.println("POSTC: Deleting comment ...");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
                objectOut.writeObject(postCommentTree);
                postService.udpateComment(post.getPostId(), byteArrayOutputStream, postCommentTree.getTotalComments(),comment_OBJ_ID);
                objectOut.close();
                byteArrayOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(Integer.parseInt(commentId) != UserLogined.getUserId()){
                        commentText.setText("Comment deleted");
                        commentText.setStyle("-fx-font-style: italic");
                    }
                    else if(post.getUserId() != UserLogined.getUserId()){
                        commentText.setText("Comment deleted by owner");
                        commentText.setStyle("-fx-font-style: italic");
                    }
                    informationBox.getChildren().remove(commentReply);
                    informationBox.getChildren().remove(dotText);
                    informationBox.getChildren().remove(postCommentDelete);
                    informationBox.getChildren().remove(dotText1);
                    System.out.println("POSTC: Comment deleted");
                }
            });
            // Terminating the thread
            threadPoolPostCommentContainer.stop();
        });
    }
}
