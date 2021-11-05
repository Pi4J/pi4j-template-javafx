package com.pi4j.jfx.app.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import com.pi4j.jfx.app.gui.util.ViewMixin;
import com.pi4j.jfx.app.model.ExamplePM;

public class ExampleGUI extends BorderPane implements ViewMixin {
    private static final String LIGHT_BULB = "\uf0eb";

    private final ExamplePM pm;

    private Button ledButton;
    private Button increaseButton;
    private Label  ledLabel;
    private Label  counterLabel;
    private Label  infoLabel;

    public ExampleGUI(ExamplePM pm) {
        this.pm = pm;
        init();
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
        addStylesheetFiles("/jfx/app/style.css");

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
        increaseButton.setOnAction(actionEvent -> pm.increaseCounter());

        ledButton.setOnMousePressed(mouseEvent -> pm.setLedGlows(true));
        ledButton.setOnMouseReleased(mouseEvent -> pm.setLedGlows(false));
    }

    @Override
    public void setupBindings() {
        infoLabel.textProperty().bind(pm.systemInfoProperty());
        counterLabel.textProperty().bind(pm.counterProperty().asString());
    }
}
