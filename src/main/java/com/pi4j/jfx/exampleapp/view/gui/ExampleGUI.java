package com.pi4j.jfx.exampleapp.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import com.pi4j.jfx.exampleapp.view.gui.util.ViewMixin;
import com.pi4j.jfx.exampleapp.model.ExamplePM;

public class ExampleGUI extends BorderPane implements ViewMixin {
    private static final String LIGHT_BULB = "\uf0eb";  // the unicode of the lightbulb-icon in fontawesome font

    // GUIs without a presentation model doesn't make any sense. You have to store it
    private final ExamplePM pm;

    // declare all the UI elements you need
    private Button ledButton;
    private Button increaseButton;
    private Label  ledLabel;
    private Label  counterLabel;
    private Label  infoLabel;

    public ExampleGUI(ExamplePM pm) {
        this.pm = pm;
        init(); //don't forget to call init
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
        addStylesheetFiles("/jfx/exampleapp/style.css");

        getStyleClass().add("root-pane");
    }

    @Override
    public void initializeParts() {
        ledLabel = new Label("Let the LED glow");

        ledButton = new Button(LIGHT_BULB);
        ledButton.getStyleClass().add("icon-button");

        increaseButton = new Button("+");

        counterLabel = new Label();
        counterLabel.getStyleClass().add("counter-label");

        infoLabel = new Label();
        infoLabel.getStyleClass().add("info-label");
    }

    @Override
    public void layoutParts() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBox = new HBox(ledLabel, spacer, ledButton);
        topBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(counterLabel, increaseButton);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setFillWidth(true);
        centerBox.setPadding(new Insets(30));

        setTop(topBox);
        setCenter(centerBox);
        setBottom(infoLabel);
    }

    @Override
    public void setupEventHandlers() {
        // look at that: all EventHandlers just trigger some action on 'pm'
        increaseButton.setOnAction(actionEvent -> pm.increaseCounter());

        ledButton.setOnMousePressed(mouseEvent -> pm.setLedGlows(true));
        ledButton.setOnMouseReleased(mouseEvent -> pm.setLedGlows(false));
    }

    @Override
    public void setupBindings() {
        // oh, wow: all the information comes from 'pm' the UI-Elements just visualizes the pm-values
        infoLabel.textProperty().bind(pm.systemInfoProperty());
        counterLabel.textProperty().bind(pm.counterProperty().asString());
    }
}
