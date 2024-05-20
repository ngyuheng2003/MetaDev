package com.metadev.connect.Controller;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.PostLiked;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import org.apache.commons.text.similarity.LevenshteinDistance;
//import org.apache.commons.text.similarity.JaccardSimilarity;
//import java.util.Locale;

public class ContentRecommendationSystem {
    private UserService userService = new UserService();
    private PostService postService = new PostService();
//    // Method to compute Levenshtein distance similarity score between two words
//    public static int computeLevenshteinDistance(String word1, String word2) {
//        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
//
//        // Compute Levenshtein distance between the two words
//        return levenshteinDistance.apply(word1.toLowerCase(), word2.toLowerCase());
//    }

//    // Method to compute Jaccard similarity between two words
//    public static double computeJaccardSimilarity(String word1, String word2) {
//        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
//
//        // Compute Jaccard similarity between the two words
//        return jaccardSimilarity.apply(word1.toLowerCase(), word2.toLowerCase());
//    }

    // Method to compute Jaro-Winkler similarity between two words
    public static double computeJaroWinklerSimilarity(String word1, String word2) {
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

        // Compute Jaro-Winkler similarity between the two words
        return jaroWinklerSimilarity.apply(word1.toLowerCase(), word2.toLowerCase());
    }

    //Get all userId
    public ArrayList<Long> getAllUserId(){
        List<User> users = userService.findUser();
        ArrayList<Long> idList = new ArrayList<Long>();

        for(User user : users)
        {
            idList.add(user.getUserId());
        }

        return idList;
    }

    //Get all post id liked by a user
    public ArrayList<Long> getLikedPostId(Long userId){
        List<PostLiked> postList = postService.fetchPostLikedByUserId(userId);
        ArrayList<Long> idList = new ArrayList<Long>();

        for(PostLiked postLiked : postList)
        {
            idList.add(postLiked.getPostId());
        }

        return idList;
    }

    //Get all tags of posts liked by a user
    public ArrayList<String> getTags(List<Long> postId){
        List<Post> list = new ArrayList<>();
        for(Long id : postId)
        {
            List<Post> post = postService.fetchPostByPostId(id);
            list.addAll(post);
        }

        ArrayList<String> tagList = new ArrayList<>();
        for(Post post : list)
        {
            if(post.getTags() == null) continue;
            String[] tagArr = post.getTags();
            for(String tag : tagArr)
            {
                tagList.add(tag);
            }
        }

        return tagList;
    }

    //Calculate weight of preferred topic of an user
    public HashMap<String, Double> preferedTopic(ArrayList<String> tagList){
        HashMap<String,Double> map = new HashMap<>();
        for(String tag : tagList)
        {
            double weight = 0;
            for(String otherTag : tagList)
            {
                weight += computeJaroWinklerSimilarity(tag,otherTag);
            }
            map.put(tag,weight);
        }
        return map;
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost\\\\SQLEXPRESS:1433;databaseName=Connect_DB;Trusted_Connection=True;trustServerCertificate=True;encrypt=true;";
        String username = "sa";
        String password = "metaDev2024";

        DataSourceConfig config = new DataSourceConfig(url,username,password);

        ContentRecommendationSystem sys = new ContentRecommendationSystem();
        ArrayList<Long> allUserId = sys.getAllUserId();
        ArrayList<Long> allLikedPostId = sys.getLikedPostId(30L);
        ArrayList<String> allTags = sys.getTags(allLikedPostId);
        HashMap<String,Double> map = sys.preferedTopic(allTags);
        System.out.println(map);
    }
}
