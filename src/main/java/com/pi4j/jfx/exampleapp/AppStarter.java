package com.pi4j.jfx.exampleapp;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pi4j.jfx.exampleapp.gui.ExampleGUI;
import com.pi4j.jfx.exampleapp.model.ExamplePM;
import com.pi4j.jfx.exampleapp.pui.ExamplePUI;
import com.pi4j.jfx.util.Pi4JContext;

public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        ExamplePM model     = new ExamplePM();

        //both gui and pui are working on the same model
        new ExamplePUI(model, Pi4JContext.INSTANCE);
        Parent gui = new ExampleGUI(model);

        Scene scene = new Scene(gui);

        primaryStage.setTitle("GUI of a Pi4J App");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Pi4JContext.shutdown();
        }));
        launch(args);
    }
}
