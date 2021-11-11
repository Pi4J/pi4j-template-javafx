package com.pi4j.jfx.mvcapp;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pi4j.jfx.mvcapp.controller.ExampleController;
import com.pi4j.jfx.mvcapp.model.ExampleModel;
import com.pi4j.jfx.mvcapp.view.gui.ExampleGUI;
import com.pi4j.jfx.mvcapp.view.gui.ExamplePuiEmulator;
import com.pi4j.jfx.mvcapp.view.pui.ExamplePUI;
import com.pi4j.jfx.util.Pi4JContext;

public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        // that's your 'information hub'.
        ExampleModel model = new ExampleModel();

        ExampleController controller = new ExampleController(model);

        //both gui and pui are working on the same controller
        new ExamplePUI(controller, Pi4JContext.INSTANCE);

        Parent gui = new ExampleGUI(controller);

        Scene scene = new Scene(gui);

        primaryStage.setTitle("GUI of a Pi4J App");
        primaryStage.setScene(scene);

        primaryStage.show();

        // on desktop it's convenient to have a very basic emulator for the PUI to test the interaction between GUI and PUI
        startPUIEmulator(new ExamplePuiEmulator(controller));
    }

    private void startPUIEmulator(Parent puiEmulator){
        Scene emulatorScene = new Scene(puiEmulator);
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("PUI Emulator");
        secondaryStage.setScene(emulatorScene);
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  //start the whole application
    }
}
