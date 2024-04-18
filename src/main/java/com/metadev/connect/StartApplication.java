package com.metadev.connect;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class StartApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception{
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                new ApplicationContextInitializer<GenericApplicationContext>() {
                    @Override
                    public void initialize(GenericApplicationContext applicationContext) {
                        applicationContext.registerBean(Application.class, ()-> StartApplication.this);
                        applicationContext.registerBean(Parameters.class, ()-> getParameters());
                        applicationContext.registerBean(HostServices.class, () -> getHostServices());
                    }
                };

        this.context = new SpringApplicationBuilder()
                .sources(ConnectApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[00]));
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() throws Exception{
        this.context.close();
        Platform.exit();
    }
}

class StageReadyEvent extends ApplicationEvent {
    public Stage getStage(){
        return (Stage) getSource();
    }

    public StageReadyEvent(Stage source) {
        super(source);
    }
}
