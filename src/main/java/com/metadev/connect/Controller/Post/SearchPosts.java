package com.metadev.connect.Controller.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import javafx.geometry.Pos;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.lang3.StringUtils; // For Levenshtein distance calculation
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SearchPosts {

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private UserService userService = new UserService();
    private PostService postService = new PostService();

    //1. search post by using username
    public List<User> searchUserByUserName(String username) throws SQLException {
        boolean found = false; // Flag to track if any matching user is found
        List<User> list = userService.findUser();
        List<User> searchList = new ArrayList<>();

            // Process the result set
            for(int i = 0; i < list.size(); i++) {
                String dbUsername = list.get(i).getUsername();
                String dbUsernames=dbUsername.toLowerCase();
                String usernames=username.toLowerCase();
                // Check if the username exactly matches or if Levenshtein distance is within the threshold
                if (dbUsernames.contains(usernames) || StringUtils.getLevenshteinDistance(dbUsernames, usernames) <= 5) { // Adjust the threshold as needed
                    searchList.add(list.get(i));
                }
            }

            // If no matching user is found, print a message
            if (searchList.size() != 0) {
                return searchList;

            }else{
                System.out.println("No matching user found with username: " + username);
                return null;
            }
        }

    // Method to fetch posts by user_id
    private void getPostsByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM [dbo].[post] WHERE user_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, userId); // Set the parameter value
        ResultSet rs = stmt.executeQuery();

        // Process the result set
        while (rs.next()) {
            // Process each post data
            getDataOfPostAfterSearch(rs);
        }
    }


    //2. search post by using content of post
    public List<Post> searchPostByContent(String content) throws SQLException {
        if(content.length() > 2) {
            List<Post> list = postService.fetchPost();
            List<Post> searchList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                String dbContent = list.get(i).getContent();
                String contents = content.toLowerCase();

                // Split the user's input into words
                String[] userWords = contents.split("\\s+");

                String[] dbWords = dbContent.split("\\s+");

                // Count the number of words that match the search term
                int matchingWordCount = 0;

                for (String userWord : userWords) {
                    if(userWord.length() > 2) {
                        for (String dbWord : dbWords) {
                            if (userWord.contains(dbWord) || StringUtils.getLevenshteinDistance(userWord, dbWord) <= 2) {
                                matchingWordCount++;
                                break; // Exit the inner loop if a match is found
                            }
                        }
                    }
                }

                // Check if the number of matching words meets your criteria or if Levenshtein distance is within the threshold
                if (matchingWordCount >= 1 ) { // Adjust the threshold as needed
                    searchList.add(list.get(i));
                }
            }

            // If no matching post is found, print a message
            if (!searchList.isEmpty()) {
                return searchList;

            } else {
                System.out.println("No matching post found");
                return null;
            }
        }
        else{
            return null;
        }
    }


    //3. search post by using tag
    private void searchPostByTag(String tag) throws SQLException {
        String query = "SELECT * FROM [dbo].[post_tags]";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        boolean found = false; // Flag to track if any matching post is found

        // Process the result set
        while (rs.next()) {
            String dbTag = rs.getString("tag");
            if (dbTag.contains(tag) || StringUtils.getLevenshteinDistance(tag.toLowerCase(), dbTag.toLowerCase())<=4) {
                int postId = rs.getInt("post_id");
                // Fetch post information using postId
                getPostsByPostId(postId);
                found = true; // Set the flag to true since a matching post is found
            }
        }

        // If no matching post is found, print a message
        if (!found) {
            System.out.println("No matching post found with tag: " + tag);
        }
    }

    private void getPostsByPostId(int postId) throws SQLException {
        String query = "SELECT * FROM [dbo].[post] WHERE post_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, postId);
        ResultSet rs = stmt.executeQuery();

        // Process the result set and output or process post information as needed
        while (rs.next()) {
            getDataOfPostAfterSearch(rs);
        }
    }

    //get all information from databsa "post"
    public void getDataOfPostAfterSearch(ResultSet rs){
        try{
            int postId = rs.getInt("post_id");
            int userId = rs.getInt("user_id");
            String postStatus = rs.getString("post_status");
            int likeCount = rs.getInt("like_count");
            String content = rs.getString("content");
            String location = rs.getString("location");

            // Fetch username using userId
            String username = getUsernameByUserId(userId);

            System.out.println("Post ID: " + postId + " ,User ID: "+userId+" ,Username: "+username+" ,port_status: "+postStatus+" ,like_count: "+likeCount+", Content: " + content+" ,location: "+location);
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch username by user_id
    private String getUsernameByUserId(int userId) throws SQLException {
        String query = "SELECT username FROM [dbo].[user] WHERE user_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("username");
        } else {
            return "Unknown"; // Return a default value if username is not found
        }
    }


//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        SearchPosts pro = new SearchPosts();
//        try {
//            pro.createConnection("jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=Connect_DB;Trusted_Connection=True;trustServerCertificate=True;encrypt=true;","sa","metaDev2024");
//            while (true) {
//                System.out.println("Select option for searching: ");
//                System.out.println("1. Search post by userName");
//                System.out.println("2. Search post by content");
//                System.out.println("3. Search post by tag");
//                System.out.println("4. Quit from searching");
//                int choice = scanner.nextInt();
//                scanner.nextLine(); // Consume newline character
//
//                switch (choice) {
//                    case 1:
//                        System.out.println("Enter userName: ");
//                        String username = scanner.nextLine();
//                        pro.searchPostByUserName(username);
//                        break;
//                    case 2:
//                        System.out.println("Enter content: ");
//                        String content = scanner.nextLine();
//                        pro.searchPostByContent(content);
//                        break;
//                    case 3:
//                        System.out.println("Enter tag: ");
//                        String tag = scanner.nextLine();
//                        pro.searchPostByTag(tag);
//                        break;
//                    case 4:
//                        System.out.println("Exiting from searching...");
//                        break;
//                    default:
//                        System.out.println("Invalid input");
//                        break;
//                }
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            pro.closeResources();
//        }
//    }
}
