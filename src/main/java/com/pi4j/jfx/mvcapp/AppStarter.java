package com.pi4j.jfx.mvcapp;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pi4j.jfx.mvcapp.model.ExampleController;
import com.pi4j.jfx.mvcapp.model.ExampleModel;
import com.pi4j.jfx.mvcapp.view.gui.ExampleGUI;
import com.pi4j.jfx.mvcapp.view.pui.ExamplePUI;
import com.pi4j.jfx.util.Pi4JContext;

public class AppStarter extends Application {

    private ExamplePUI pui;

    @Override
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(pui != null){
                pui.shutdown();
            }
        }));
    }

    @Override
    public void start(Stage primaryStage) {
        // that's your 'information hub'.
        ExampleModel model = new ExampleModel();

        ExampleController controller = new ExampleController(model);

        //both gui and pui are working on the same controller
        pui = new ExamplePUI(controller, Pi4JContext.INSTANCE);
        Parent gui = new ExampleGUI(controller);

        Scene scene = new Scene(gui);

        primaryStage.setTitle("GUI of a Pi4J App");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  //start the whole application
    }
}
