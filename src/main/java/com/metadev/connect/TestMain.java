package testmain;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author ngzhi
 */
public class TestMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        CommentTree commentTree = new CommentTree();
        Like like = new Like(); // Initialize the 'like' variable
// Check if the serialized file exists
        File serializedFile = new File("comment.ser");
        if (serializedFile.exists()) {
            // If the file exists, deserialize the comment tree data from the file
            commentTree = CommentTree.deserializeCommentTreeFromFile();
        } else {
            // If the file doesn't exist, create a new CommentTree instance
            commentTree = new CommentTree();
        }

        while (!quit) {
            System.out.println("What do you want to perform?");
            System.out.println("1. Post Text");
            System.out.println("2. View Post");
            System.out.println("3. Search Post");
            System.out.println("4. Delete Post");
            System.out.println("5. Quit");

            int choices = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choices) {
                case 1:
                    // Post Text
                    break;
                case 2:
                    // View Post
                    boolean viewPostQuit = false;
                    while (!viewPostQuit) {
                        System.out.println("");
                        System.out.println("Choose an action:");
                        System.out.println("1. Like Post");
                        System.out.println("2. New Comment");
                        System.out.println("3. Reply to Comment");
                        System.out.println("4. Delete Comment");
                        System.out.println("5. View Comment");
                        System.out.println("6. Next Post");
                        System.out.println("7. Quit");
                        try {
                            int viewPostChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character

                            switch (viewPostChoice) {
                                case 1:
                                    like.likePost();
                                    System.out.println("");
                                    break;

                                case 2:
                                    // New Comment
                                    System.out.println("Enter your comment text:");
                                    String commentText = scanner.nextLine();
                                    System.out.println("Enter your name:");
                                    String author = scanner.nextLine();
                                    CommentNode newComment = new CommentNode(commentText, author);
                                    commentTree.addComment(-1, newComment); // Assuming it's a top-level comment
                                    System.out.println("Comment added successfully.");
                                  
                                    break;

                                case 3:
                                    System.out.println("----------------");
                                    System.out.println("List of comments");
                                    System.out.println("----------------");
                                    commentTree.displayAllCommentsFromFile();
                                    System.out.print("Enter the ID of the comment you want to reply to:");
                                    int parentCommentId = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline character

                                    // Get the total number of comments
                                    int totalComments = commentTree.getTotalComments();

                                    System.out.println();
                                    // Check if the entered comment ID is valid
                                    if (!CommentNode.isValidCommentId(parentCommentId, totalComments)) {
                                        System.out.println("Invalid comment ID. Reply could not be added.");
                                    } else {
                                        // Prompt for reply text and author name
                                        System.out.println("Enter your reply:");
                                        String replyText = scanner.nextLine();
                                        System.out.println("Enter your name:");
                                        String replyAuthor = scanner.nextLine();

                                        // Create the reply comment node
                                        CommentNode reply = new CommentNode(replyText, replyAuthor);

                                        // Add the reply to the specified comment
                                        commentTree.addComment(parentCommentId, reply);
                                        System.out.println("Reply added successfully.");
                                    }
                                    break;

                                case 4:
                                    // Delete Comment
                                    System.out.println("----------------");
                                    System.out.println("List of comments");
                                    System.out.println("----------------");
                                    commentTree.displayAllCommentsFromFile();
                                    System.out.println("Enter the ID of the comment you want to delete:");
                                    int commentIdToDelete;
                                    try {
                                        commentIdToDelete = scanner.nextInt();
                                        scanner.nextLine(); // Consume newline character

                                        // Check if the entered comment ID is valid
                                        if (!CommentNode.isValidCommentId(commentIdToDelete, commentTree.getTotalComments())) {
                                            System.out.println("Invalid comment ID. Comment could not be deleted.");
                                        } else {
                                            commentTree.deleteComment(commentIdToDelete);
                                        }
                                    } catch (InputMismatchException e) {
                                        // Handle the case where the user enters a non-numeric input
                                        System.out.println("Invalid input. Please enter a valid comment ID.");
                                        scanner.nextLine(); // Consume the invalid input
                                    }
                                    break;

                                case 5:
                                    // View Comment
                                    commentTree.displayAllCommentsFromFile(); // Modified to read comments from file
                                    break;

                                case 7:
                                    viewPostQuit = true;
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please choose again.");
                                    break;

                            }

                        } catch (InputMismatchException e) {
                            // Handle the case where the user enters a non-numeric input
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Consume the invalid input
                        }
                    }
                    break;

                case 3:
                    // Search Post
                    break;

                case 4:
                    // Delete Post
                    break;

                case 5:
                    // Quit
                    quit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
        scanner.close();
    }
}
