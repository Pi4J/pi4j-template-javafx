package com.pi4j.mvc.templateapp.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ViewMixin;

public class SomeGUI extends BorderPane implements ViewMixin<SomeModel, SomeController> { //all GUI-elements have to implement ViewMixin

    private static final String LIGHT_BULB = "\uf0eb";  // the unicode of the lightbulb-icon in fontawesome font

    // declare all the UI elements you need
    private Button ledButton;
    private Button increaseButton;
    private Label  counterLabel;
    private Label  infoLabel;

    public SomeGUI(SomeController controller) {
        init(controller); //don't forget to call 'init'
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
        addStylesheetFiles("/mvc/templateapp/style.css");

        getStyleClass().add("root-pane");
    }

    @Override
    public void initializeParts() {
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
        HBox topBox = new HBox(ledButton);
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
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        increaseButton.setOnAction  (event -> controller.increaseCounter());
        ledButton.setOnMousePressed (event -> controller.setLedGlows(true));
        ledButton.setOnMouseReleased(event -> controller.setLedGlows(false));
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        onChangeOf(model.systemInfo)                       // the value we need to observe, in this case that's an ObservableValue<String>, no need to convert it
                .update(infoLabel.textProperty());         // keeps textProperty and systemInfo in sync

        onChangeOf(model.counter)                          // the value we need to observe, in this case that's an ObservableValue<Integer>
                .convertedBy(String::valueOf)              // we have to convert the Integer to a String
                .update(counterLabel.textProperty());      // keeps textProperty and counter in sync
    }
}
