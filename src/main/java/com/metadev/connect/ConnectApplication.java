package com.metadev.connect;

import com.metadev.connect.Controller.StartApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
public class ConnectApplication {

    public static void main(String[] args){
        Application.launch(StartApplication.class, args);
    }
}
