package com.metadev.connect.Controller.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.Scanner;

public class SearchPosts {

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private void createConnection(String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        con = DriverManager.getConnection(url, username, password);
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchPostByContent(String content) throws SQLException {
        String query = "SELECT * FROM post WHERE content = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, content);
        rs = pstmt.executeQuery();

        // Check if there are any results
        if (!rs.next()) {
            System.out.println("No posts found with content: " + content);
            return;
        }

        // Process the result set
        do {
            getDataOfPostAfterSearch(rs);
        } while (rs.next());
    }

    private void searchPostByPostId(int postId) throws SQLException {
        String query = "SELECT * FROM post WHERE post_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, postId);
        rs = pstmt.executeQuery();

        // Check if there are any results
        if (!rs.next()) {
            System.out.println("No posts found with post_Id: " + postId);
            return;
        }

        // Process the result set
        do {
            getDataOfPostAfterSearch(rs);
        } while (rs.next());
    }

    private void searchPostByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM post WHERE user_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, userId);
        rs = pstmt.executeQuery();

        // Check if there are any results
        if (!rs.next()) {
            System.out.println("No posts found with user_Id: " + userId);
            return;
        }

        // Process the result set
        do {
            getDataOfPostAfterSearch(rs);
        } while (rs.next());
    }

    private void searchPostByTag(String tag) throws SQLException {
        String query = "SELECT * FROM post_tags WHERE tag = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, tag);
        rs = pstmt.executeQuery();

        // Check if there are any results
        if (!rs.next()) {
            System.out.println("No posts found with tag: " + tag);
            return;
        }

        // Process the result set
        do {
            getDataOfPostAfterSearch(rs);
        } while (rs.next());
    }


    public void getDataOfPostAfterSearch(ResultSet rs){
        try{
            int postId = rs.getInt("post_id");
            int userId = rs.getInt("user_id");
            String postStatus = rs.getString("post_status");
            int likeCount = rs.getInt("like_count");
            String content = rs.getString("content");
            String location = rs.getString("location");
            System.out.println("Post ID: " + postId + " ,User ID: "+userId+" ,port_status: "+postStatus+" ,like_count: "+likeCount+", Content: " + content+" ,location: "+location);
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SearchPosts pro = new SearchPosts();
        try {
            pro.createConnection("jdbc:sqlserver://connect-sql-db.database.windows.net:1433;databaseName=Connect_DB", "connectSQLDB", "metaDev2024");
            while (true) {
                System.out.println("Select option for searching: ");
                System.out.println("1. Search post by user_Id");
                System.out.println("2. Search post by post_Id");
                System.out.println("3. Search post by content");
                System.out.println("4. Search post by tag");
                System.out.println("5. Quit from searching");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        System.out.println("Enter user_Id: ");
                        int userId = scanner.nextInt();
                        pro.searchPostByUserId(userId);
                        break;
                    case 2:
                        System.out.println("Enter post_Id: ");
                        int postId = scanner.nextInt();
                        pro.searchPostByPostId(postId);
                        break;
                    case 3:
                        System.out.println("Enter content: ");
                        String content = scanner.nextLine();
                        pro.searchPostByContent(content);
                        break;
                    case 4:
                        System.out.println("Enter tag: ");
                        String tag = scanner.nextLine();
                        pro.searchPostByTag(tag);
                        break;
                    case 5:
                        System.out.println("Exiting from searching...");
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            pro.closeResources();
        }
    }


}
