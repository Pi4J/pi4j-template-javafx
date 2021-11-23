package com.pi4j.mvc.templateapp.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ViewMixin;

public class SomePuiEmulator extends VBox implements ViewMixin<SomeModel, SomeController> {

    // for each PUI component, declare a corresponding JavaFX-control
    private Label  led;
    private Button decreaseButton;

    public SomePuiEmulator(SomeController controller){
        init(controller);
    }

    @Override
    public void initializeSelf() {
        setPrefWidth(250);
    }

    @Override
    public void initializeParts() {
        led = new Label();
        decreaseButton = new Button("Decrease");
    }

    @Override
    public void layoutParts() {
        setPadding(new Insets(20));
        setSpacing(20);
        setAlignment(Pos.CENTER);
        getChildren().addAll(led, decreaseButton);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        //trigger the same actions as the real PUI

        decreaseButton.setOnAction(event -> controller.decreaseCounter());
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        //observe the same values as the real PUI

        onChangeOf(model.ledGlows)
                .convertedBy(glows -> glows ? "on" : "off")
                .update(led.textProperty());
    }
}
