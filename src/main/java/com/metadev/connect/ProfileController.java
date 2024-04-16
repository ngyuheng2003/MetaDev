package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ProfileController {

    @FXML private Button postSectionButton, repliesSectionButton;
    @FXML private VBox vbox;

    public void addPostButtonClicked(ActionEvent event) {
    }

    public void settingButtonClicked(ActionEvent event) {
    }

    public void postSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setStyle("-fx-background-color: transparent");
    }

    public void repliedSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setStyle("-fx-background-color: rgb(211,211,211,0.5)");
        postSectionButton.setStyle("-fx-background-color: transparent");
    }
}
