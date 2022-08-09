package com.pi4j.setup;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A tiny tool to check the basic setup of JavaFX.
 * <p>
 * It's a long way from here to a real application.
 * <p>
 * But as long as 'HelloFX' isn't working properly, it's useless to go any further.
 * <p>
 * It's not meant to be any kind of template to start your development.
 * <p>
 * Initially copied from https://github.com/openjfx/samples/blob/master/CommandLine/Modular/CLI/hellofx/src/hellofx/HelloFX.java
 *
 */
public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion   = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        Label lbl = new Label("JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        Button btn = new Button("Say Hello");
        btn.setOnAction(event -> lbl.setText("Hello"));

        ImageView imgView = new ImageView(new Image(HelloFX.class.getResourceAsStream("/setup/openduke.png")));
        imgView.setFitHeight(200);
        imgView.setPreserveRatio(true);

        VBox rootPane = new VBox(50, imgView, lbl, btn);
        rootPane.setAlignment(Pos.CENTER);

        rootPane.getStylesheets().add(HelloFX.class.getResource("/setup/style.css").toExternalForm());

        Scene scene = new Scene(rootPane, 640, 480);

        stage.setTitle("Plain JavaFX App");
        //if started in DRM, make stage full-screen
        if (System.getProperty("monocle.egl.lib") != null) {
            Rectangle2D bounds = Screen.getPrimary().getBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}