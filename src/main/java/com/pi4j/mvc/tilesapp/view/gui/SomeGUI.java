package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.interfaces.LEDButtonInterface;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.interfaces.SimpleLEDInterface;
import com.pi4j.components.tiles.JoystickTile;
import com.pi4j.components.tiles.LedButtonTile;
import com.pi4j.components.tiles.SimpleButtonTile;
import com.pi4j.components.tiles.SimpleLEDTile;
import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ViewMixin;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SomeGUI extends FlowGridPane implements ViewMixin<SomeModel, SomeController> { //all GUI-elements have to implement ViewMixin

    // declare all the UI elements you need
    private SimpleLEDInterface led;
    private SimpleButtonInterface button;
    private JoystickInterface joystick;
    private LEDButtonInterface ledButton;

    public SomeGUI(SomeController controller) {
        super(8,1);
        setHgap(5);
        setVgap(5);
        setAlignment(Pos.CENTER);
        setCenterShape(true);
        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));

        init(controller); //don't forget to call 'init'
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
      //  loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
      //  addStylesheetFiles("/mvc/tilesapp/style.css");

        getStyleClass().add("root-pane");
    }

    @Override
    public void initializeParts() {
        led = new SimpleLEDTile(PIN.D22);
        button = new SimpleButtonTile(PIN.D24);
        joystick = new JoystickTile();
        ledButton = new LedButtonTile();
    }

    @Override
    public void layoutParts() {
        getChildren().addAll((Tile) led, (Tile) button, (Tile) ledButton, (Tile) joystick);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        button.onDown(() -> controller.setButtonPressed(true));
        button.onUp  (() -> controller.setButtonPressed(false));
        button.whilePressed(() -> controller.whileMessage("Simple"),5000);

        joystick.onPushDown(() -> controller.setButtonPressed(true));
        joystick.onPushUp(() -> controller.setButtonPressed(false));

        //Send message for short and long press
        joystick.pushWhilePushed(2000, () -> controller.whileMessage("Joystick"));
        joystick.onNorth(() -> controller.pressedMessage("Up"));
        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.onSouth(() -> controller.pressedMessage("Down"));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.onWest(() -> controller.pressedMessage("Left"));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.onEast(() -> controller.pressedMessage("Right"));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));

        ledButton.onDown(() -> controller.pressedMessage("LED"));
        ledButton.onUp(controller::setLedButtonReleased);
        ledButton.btnwhilePressed(controller::whilePressedLedButton, 1000);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {

        //TODO: GUI-MODEL
        //press button to turn on/off SimpleLED
        onChangeOf(model.isButtonPressed)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    led.on();
                } else {
                    led.off();

                }
            });

        //press button to turn on/off SimpleLED
        onChangeOf(model.isLedButtonActive)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledButton.LEDsetStateOn();
                } else {
                    ledButton.LEDsetStateOff();

                }
            });

    }
}
