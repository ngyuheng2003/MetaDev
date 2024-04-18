package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartUp {
    public StartUp(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(StartUp.class.getResource(fxml)));
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }
}
