package com.metadev.connect.Controller.Post;

import com.metadev.connect.Controller.ContentRecommendationSystem;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.FXMLController.NewsFeedController;
import com.metadev.connect.FXMLController.PostContainerController;
import com.metadev.connect.FXMLController.ProfileController;
import com.metadev.connect.FXMLController.SearchController;
import com.metadev.connect.Service.PostService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PostDisplaying<T> {
    PostService postService = new PostService();
    ContentRecommendationSystem contentRecommendationSystem = new ContentRecommendationSystem();
    public List<Post> fetchPost(){
        return contentRecommendationSystem.recommendPost(postService.fetchPost(), UserLogined.getUserId());
    }

    public List<Post> fetchPostByUserId(Long userId){
        return postService.fetchPostByUserId(userId);
    }

    public List<Post> fetchPostByPostId(Long postId){
        return postService.fetchPostByUserId(postId);
    }

    // Post displaying
    public void displayPost(T parentController, Post post, VBox container, int type) throws IOException, SQLException, InterruptedException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostContainer.fxml"));
        VBox box = fxmlLoader.load();
        PostContainerController<T> postContainerController = fxmlLoader.getController();
        postContainerController.setParentController(parentController);
        postContainerController.setPostContainer(post, type);
        container.getChildren().add(box);
    }

    public PostContainerController<T> displayComment(T parentController, Post post, VBox container, PostContainerController<T> external, int type) throws IOException, SQLException, InterruptedException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostContainer.fxml"));
        VBox box = null;
        if(parentController instanceof NewsFeedController newsFeedController) {
            newsFeedController.newFeedPostBox = fxmlLoader.load();
            box = newsFeedController.newFeedPostBox;
        }
        else if(parentController instanceof ProfileController profileController) {
            profileController.newFeedPostBox = fxmlLoader.load();
            box = profileController.newFeedPostBox;
        }
        else if(parentController instanceof SearchController searchController) {
            searchController.newFeedPostBox = fxmlLoader.load();
            box = searchController.newFeedPostBox;
        }
        PostContainerController<T> postContainerController = fxmlLoader.getController();
        postContainerController.setPostContainer(post, type);
        postContainerController.setCommentSection(true);
        postContainerController.setParentController(parentController);
        postContainerController.setExternalPostController(external);
        container.getChildren().add(box);
        container.setStyle("-fx-background-color:  rgb(255,255,255,0.5)");
        System.out.println("NEWFD: Comment section opened");
        postContainerController.displayComment();

        return postContainerController;
    }
}
