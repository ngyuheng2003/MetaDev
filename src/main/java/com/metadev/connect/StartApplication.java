package com.metadev.connect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartUpView.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Connect");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/Logo/logo_colour.png"))));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}