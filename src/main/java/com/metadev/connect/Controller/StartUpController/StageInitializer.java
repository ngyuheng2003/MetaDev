package com.metadev.connect.Controller.StartUpController;

import com.metadev.connect.Controller.DataSourceConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext context;

    public StageInitializer(@Value("${spring.application.name}") String applicationTitle,
                            @Value("classpath:/FXMLView/StartUpView.fxml") Resource resourse,
                            ApplicationContext context,
                            @Value("${spring.datasource.url}") String url,
                            @Value("${spring.datasource.username}") String username,
                            @Value("${spring.datasource.password}")String password) {
        this.applicationTitle = applicationTitle;
        this.fxml = resourse;
        this.context = context;
        new DataSourceConfig(url, username, password);

    }


    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return context.getBean(aClass);
                }
            });
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.getIcons().add(new Image("Images/Logo/logo_colour.png"));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
