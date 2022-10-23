package com.pi4j.mvc.tilesapp;

import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.tilesapp.view.gui.SomeGUI;
import com.pi4j.mvc.tilesapp.view.pui.SomePUI;
import com.pi4j.mvc.util.Pi4JContext;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AppStarter extends Application {

    private SomeController controller;
    private SomePUI pui;

    @Override
    public void start(Stage primaryStage) {
        // that's your 'information hub'.
        SomeModel model = new SomeModel();

        controller = new SomeController(model);

        //both gui and pui are working on the same controller
        pui = new SomePUI(controller, Pi4JContext.createContext());

        Pane gui = new SomeGUI(controller);

        primaryStage.setTitle("GUI of a Pi4J App");
        setupStage(primaryStage, gui);

        primaryStage.show();

        // on desktop, it's convenient to have a very basic emulator for the PUI to test the interaction between GUI and PUI
        //startPUIEmulator(new SomePuiEmulator(controller));
    }


    @Override
    public void stop() {
        controller.shutdown();
        pui.shutdown();
    }

    private void setupStage(Stage stage, Pane gui) {
        //if started in DRM
        if (System.getProperty("egl.displayid") != null) {
            // make stage full-screen
            Rectangle2D bounds = Screen.getPrimary().getBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setResizable(false);

            // to get a nice background and the gui centered
            gui.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            gui.setStyle("-fx-border-color: dodgerblue; -fx-border-width: 3");

            StackPane background = new StackPane(gui);
            background.setStyle("-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, dodgerblue 0%, midnightblue 100%)");

            Scene scene = new Scene(background);

            stage.setScene(scene);
        } else {
            Scene scene = new Scene(gui);
            stage.setScene(scene);
        }
    }

    private void startPUIEmulator(Parent puiEmulator) {
        Scene emulatorScene  = new Scene(puiEmulator);
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("PUI Emulator");
        secondaryStage.setScene(emulatorScene);
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  //start the whole application
    }
}
