package com.metadev.connect.Controller;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animation {
    PauseTransition pause = new PauseTransition();

    public void translateLeft(Node node, double duration, double delay, double distance){
        TranslateTransition translateLeft = new TranslateTransition();
        translateLeft.setNode(node);
        translateLeft.setByX(distance);
        translateLeft.setInterpolator(Interpolator.EASE_BOTH);
        translateLeft.setDelay(Duration.seconds(delay));
        translateLeft.setDuration(Duration.seconds(duration));
        translateLeft.play();
    }

    public void fade(Node node, double duration, double delay, double initialValue, double finalValue){
        FadeTransition fade = new FadeTransition();
        fade.setNode(node);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.setDelay(Duration.seconds(delay));
        fade.setDuration(Duration.seconds(duration));
        fade.setFromValue(initialValue);
        fade.setToValue(finalValue);
        fade.play();
    }

    public void rotate(Node node, double duration, double delay) throws InterruptedException {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(node);
        rotate.setDuration(Duration.seconds(duration));
        rotate.setDelay(Duration.seconds(delay));
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setCycleCount(20);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
    }

    public void pause(Transition object, double duration) {
        object.pause();
    }
}
