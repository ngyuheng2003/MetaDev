package com.metadev.connect.Controller.Post;

import com.metadev.connect.Service.PostService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostCommentTree implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private CommentNode root;
    private int totalComments; // Counter for total comments
    private List<CommentNode> topLevelComments = new ArrayList<>();
    ArrayList<String[]> list = new ArrayList<>();

    public PostCommentTree(Long post_id) {
        this.root = null;
        this.totalComments = 0; // Initialize totalComments counter

    }

    public void setCommentID(int totalComments){
        CommentNode.setNextMinCommentId(totalComments + 1);
    }

    public void addComment(int parentCommentId, CommentNode newComment) {
        if (parentCommentId == -1) {
            topLevelComments.add(newComment);

        } else {
            CommentNode parentComment = findComment(parentCommentId);
            if (parentComment != null) {
                parentComment.addChildComment(newComment);

            } else {
                System.out.println("Parent comment not found!");
            }
        }
        incrementTotalComments(); // Increment totalComments counter
    }

    private void incrementTotalComments() {
        totalComments++;
    }

    private CommentNode findComment(int commentId) {
        for (CommentNode topLevelComment : topLevelComments) {
            CommentNode found = findCommentRecursive(topLevelComment, commentId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private CommentNode findCommentRecursive(CommentNode node, int commentId) {
        if (node.getCommentId() == commentId) {
            return node;
        }

        for (CommentNode child : node.getChildComment()) {
            CommentNode found = findCommentRecursive(child, commentId);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void displayAllCommentsFromFile() {
        PostCommentTree deserializedTree = deserializeCommentTreeFromFile();
        if (deserializedTree != null) {
            deserializedTree.displayAllComments();
        } else {
            System.out.println("No comments found.");
        }
    }

    private int countComments(CommentNode node) {
        if (node == null) {
            return 0;
        }

        int count = 1; // Initialize count to 1 to count the current node itself

        // Recursively count comments of children
        for (CommentNode child : node.getChildComment()) {
            count += countComments(child);
        }

        return count;
    }

    public static PostCommentTree deserializeCommentTreeFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream("comment.ser");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            PostCommentTree postCommentTree = (PostCommentTree) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            // Update nextCommentId from the deserialized CommentTree
            CommentNode.updateNextCommentId(postCommentTree.totalComments + 1);

            return postCommentTree;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String[]> displayAllComments() {
        if (topLevelComments.isEmpty()) {
            return null;
        } else {
                return displayCommentsRecursive(topLevelComments.getFirst(), 0);

        }
    }


    public ArrayList<String[]> displayCommentsRecursive(CommentNode node, int depth) {
        String[] commentInformation = new String[5];
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("\t");
        }

        String commentText = node.getText();
        int commentId = node.getCommentId();
        String author = node.getAuthor();
        LocalDateTime timestamp = node.getTimestamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        System.out.println(indent + "Comment:");
        if (commentText.equals("Comment Deleted")) {
            System.out.println(indent + "\tText: " + commentText);
        } else {
            System.out.println(indent + "\tText: " + commentText);
            System.out.println(indent + "\tTimeStamp: " + formattedTimestamp);
            System.out.println(indent + "\tComment ID: " + commentId);
            System.out.println(indent + "\tAuthor: " + author);
            System.out.println(indent + "\tAuthor: " + depth);
            commentInformation[0] = commentText;
            commentInformation[1] = author;
            commentInformation[2] = formattedTimestamp;
            commentInformation[3] = String.valueOf(commentId);
            commentInformation[4] = String.valueOf(depth);
            list.add(commentInformation);

        }

        List<CommentNode> childComments = node.getChildComment();
        if (!childComments.isEmpty()) {
            System.out.println("POSTC: Checking replies");
            System.out.println(indent + "\tReplies:");
            for (CommentNode child : childComments) {
                displayCommentsRecursive(child, depth + 1);
            }
        }
        System.out.println(1);
        return list;
    }

    public void deleteComment(int commentId) {
        CommentNode commentToDelete = findComment(commentId); // Find the comment to delete

        if (commentToDelete != null) {
            // Check if the comment to delete is a top-level comment
            if (topLevelComments.contains(commentToDelete)) {
                // Mark the top-level comment as deleted
                commentToDelete.markDeleted();
                commentToDelete.setText("Comment Deleted");
            } else {
                // Find the parent comment of the comment to delete
                CommentNode parent = findParent(commentToDelete);

                if (parent != null) {
                    // Mark the child comment as deleted
                    commentToDelete.markDeleted();
                    commentToDelete.setText("Comment Deleted");
                } else {
                    System.out.println("Comment not found!");
                    return;
                }
            }

            System.out.println("Comment " + commentId + " deleted successfully.");
            // Save the updated comment tree to the file
        } else {
            System.out.println("Comment not found!");
        }
    }

    // Helper method to find the parent of a comment
    private CommentNode findParent(CommentNode node) {
        for (CommentNode topLevelComment : topLevelComments) {
            CommentNode parent = findParentRecursive(topLevelComment, node);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    // Recursive helper method to find the parent of a comment
    private CommentNode findParentRecursive(CommentNode currentNode, CommentNode nodeToFind) {
        if (currentNode.getChildComment().contains(nodeToFind)) {
            return currentNode;
        }

        for (CommentNode child : currentNode.getChildComment()) {
            CommentNode parent = findParentRecursive(child, nodeToFind);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

//    public void saveCommentTreeToFile() {
//        try {
//            System.out.println("POSTC: Saving comment ...");
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOutputStream);
//            objectOut.writeObject(this);
//            postService.addComment(post_id, byteArrayOutputStream);
//            objectOut.close();
//            byteArrayOutputStream.close();
//            System.out.println("POSTC: Comment saved");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
