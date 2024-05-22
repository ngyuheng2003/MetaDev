package com.metadev.connect.Controller;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.PostLiked;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserPreferredTopic;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.util.*;
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

    //Calculate weight of preferred topic of a user
    public ArrayList<UserPreferredTopic> preferedTopic(Long userId,ArrayList<String> tagList){
        ArrayList<UserPreferredTopic> list = new ArrayList<>();
        for(String tag : tagList)
        {
            double weight = 0;
            for(String otherTag : tagList)
            {
                weight += computeJaroWinklerSimilarity(tag,otherTag);
            }
            list.add(new UserPreferredTopic(userId,tag,weight));
        }
        return list;
    }

    //Insert top 5 preferred topic for each a user
    public void insertTable(ArrayList<UserPreferredTopic> list){
        if(!list.isEmpty())
        {
            System.out.println("Inserting data...");
            if(list.size() >= 5)
            {
                for(int i=0;i<5;i++)
                {
                    userService.insertUserPreferredTopic(list.get(i));
                }
            }
            else
            {
                for(int i=0;i<list.size();i++)
                {
                    userService.insertUserPreferredTopic(list.get(i));
                }
            }
        }
    }

    //Clear preferred topic for a user
    public void clearTable(ArrayList<UserPreferredTopic> list){
        if(!list.isEmpty())
        {
            userService.removeUserPreferredTopic(list.get(0));
            System.out.println("Clearing user preferred topic data...");
        }
    }

    //Check whether to use insert or update method
    public boolean checkInsertOrUpdateTable(Long userId) throws InterruptedException {
        return userService.checkUserPreferredTopicExistById(userId);
    }

    public static void main(String[] args) throws InterruptedException {
        String url = "jdbc:sqlserver://localhost\\\\SQLEXPRESS:1433;databaseName=Connect_DB;Trusted_Connection=True;trustServerCertificate=True;encrypt=true;";
        String username = "sa";
        String password = "metaDev2024";

        DataSourceConfig config = new DataSourceConfig(url,username,password);

        ContentRecommendationSystem sys = new ContentRecommendationSystem();
        ArrayList<Long> allUserId = sys.getAllUserId();
        for (Long userId : allUserId)
        {
            ArrayList<Long> allLikedPostId = sys.getLikedPostId(userId);
            ArrayList<String> allTags = sys.getTags(allLikedPostId);
            ArrayList<UserPreferredTopic> list = sys.preferedTopic(userId,allTags);
            Collections.sort(list,Collections.reverseOrder());
            if(sys.checkInsertOrUpdateTable(userId))
                sys.clearTable(list);
            sys.insertTable(list);
        }
    }
}
