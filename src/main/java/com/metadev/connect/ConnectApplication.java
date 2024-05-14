package com.metadev.connect;

import com.metadev.connect.Controller.StartUpController.StartApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ConnectApplication {

    public static void main(String[] args){
        Application.launch(StartApplication.class, args);
    }
}
