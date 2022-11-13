package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ViewMixin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        //observe the same values as the real PUI

        onChangeOf(model.isLedActive)
                .convertedBy(active -> active ? "on" : "off")
                .update(led.textProperty());
    }
}
