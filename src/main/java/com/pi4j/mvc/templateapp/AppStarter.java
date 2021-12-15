package com.pi4j.mvc.templateapp;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.templateapp.view.gui.SomeGUI;
import com.pi4j.mvc.templateapp.view.pui.SomePUI;
import com.pi4j.mvc.util.Pi4JContext;

public class AppStarter extends Application {

    private SomeController controller;
    private SomePUI        pui;

    @Override
    public void start(Stage primaryStage) {
        // that's your 'information hub'.
        SomeModel model = new SomeModel();

        controller = new SomeController(model);

        //both gui and pui are working on the same controller
        pui = new SomePUI(controller, Pi4JContext.createContext());

        Parent gui = new SomeGUI(controller);

        Scene scene = new Scene(gui);

        primaryStage.setTitle("GUI of a Pi4J App");
        primaryStage.setScene(scene);

        primaryStage.show();

        // on desktop it's convenient to have a very basic emulator for the PUI to test the interaction between GUI and PUI
        // startPUIEmulator(new SomePuiEmulator(controller));
    }

    @Override
    public void stop() {
        controller.shutdown();
        pui.shutdown();
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
