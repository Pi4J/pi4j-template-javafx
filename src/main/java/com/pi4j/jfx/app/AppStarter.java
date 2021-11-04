package com.pi4j.jfx.app;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pi4j.jfx.app.gui.RootPane;
import com.pi4j.jfx.app.model.RootPM;

public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        RootPM  model     = new RootPM();
        Parent  rootPanel = new RootPane(model);

        Scene scene = new Scene(rootPanel);


        primaryStage.setTitle("GUI of a Pi4J App");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
