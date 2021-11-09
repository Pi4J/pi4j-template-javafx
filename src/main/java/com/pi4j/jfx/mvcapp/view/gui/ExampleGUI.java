package com.pi4j.jfx.mvcapp.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import com.pi4j.jfx.mvcapp.model.ExampleController;
import com.pi4j.jfx.mvcapp.model.ExampleModel;
import com.pi4j.jfx.mvcapp.view.gui.util.ViewMixin;

public class ExampleGUI extends BorderPane implements ViewMixin<ExampleModel, ExampleController> {
    private static final String LIGHT_BULB = "\uf0eb";  // the unicode of the lightbulb-icon in fontawesome font
    private static final String HEARTBEAT  = "\uf21e";  // the unicode of the heartbeat-icon in fontawesome font

    // declare all the UI elements you need
    private Button ledButton;
    private Button blinkButton;
    private Button increaseButton;
    private Label  counterLabel;
    private Label  infoLabel;

    public ExampleGUI(ExampleController controller) {
        init(controller); //don't forget to call init
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
        addStylesheetFiles("/jfx/mvcapp/style.css");

        getStyleClass().add("root-pane");
    }

    @Override
    public void initializeParts() {
        ledButton = new Button(LIGHT_BULB);
        ledButton.getStyleClass().add("icon-button");

        blinkButton = new Button(HEARTBEAT);
        blinkButton.getStyleClass().add("icon-button");

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

        HBox topBox = new HBox(ledButton, spacer, blinkButton);
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
    public void setupEventHandlers(ExampleController controller) {
        // look at that: all EventHandlers just trigger some action on 'controller'
        increaseButton.setOnAction(actionEvent -> controller.increaseCounter());

        ledButton.setOnMousePressed(mouseEvent  -> controller.setLedGlows(true));
        ledButton.setOnMouseReleased(mouseEvent -> controller.setLedGlows(false));

        blinkButton.setOnAction(actionEvent -> controller.blink());
    }

    @Override
    public void setupGUIUpdates(ExampleModel model) {
        onChangeOf(model.systemInfo)
                .update(infoLabel.textProperty());

        onChangeOf(model.counter)
                .convertedBy(String::valueOf)
                .update(counterLabel.textProperty());

    }
}
