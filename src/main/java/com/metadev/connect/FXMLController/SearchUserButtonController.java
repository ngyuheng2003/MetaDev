package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class SearchUserButtonController {

    @FXML private Button searchUserButtonUsername;
    private User user;
    public void searchUserButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(user);
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }

    public void setSearchUserButtonContainer(User user) {
        searchUserButtonUsername.setText(user.getUsername());
        this.user = user;
    }
}
