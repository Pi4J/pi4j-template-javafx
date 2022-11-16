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
    private SimpleLEDInterface ledTile;
    private SimpleButtonInterface buttonTile;

    private JoystickInterface joystickTile;

    private LEDButtonInterface ledButtonTile;

    public SomeGUI(SomeController controller) {
        super(5,1);
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
        ledTile    = new SimpleLEDTile(PIN.D22);
        buttonTile = new SimpleButtonTile(PIN.D24);
        joystickTile = new JoystickTile();
        ledButtonTile = new LedButtonTile();
    }

    @Override
    public void layoutParts() {
        getChildren().addAll((Tile)ledTile, (Tile)buttonTile, (Tile)ledButtonTile, (Tile)joystickTile);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        buttonTile.onDown(() -> controller.setButtonPressed(true));
        buttonTile.onUp  (() -> controller.setButtonPressed(false));
        buttonTile.whilePressed(() -> controller.whileMessage("Simple"),5000);

        joystickTile.onPushDown(() -> controller.setButtonPressed(true));
        joystickTile.onPushUp(() -> controller.setButtonPressed(false));

        //Send message for short and long press
        joystickTile.pushWhilePushed(2000, () -> controller.whileMessage("Joystick"));
        joystickTile.onNorth(() -> controller.pressedMessage("Up"));
        joystickTile.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystickTile.onSouth(() -> controller.pressedMessage("Down"));
        joystickTile.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystickTile.onWest(() -> controller.pressedMessage("Left"));
        joystickTile.whileWest(2000, () -> controller.whileMessage("Left"));
        joystickTile.onEast(() -> controller.pressedMessage("Right"));
        joystickTile.whileEast(2000, () -> controller.whileMessage("Right"));

        ledButtonTile.onDown(() -> controller.pressedMessage("LED"));
        ledButtonTile.onUp(controller::setLedButtonReleased);
        ledButtonTile.btnwhilePressed(controller::whilePressedLedButton, 1000);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {

        //TODO: GUI-MODEL
        //press button to turn on/off SimpleLED
        onChangeOf(model.isButtonPressed)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledTile.on();
                } else {
                    ledTile.off();

                }
            });

        //press button to turn on/off SimpleLED
        onChangeOf(model.isLedButtonActive)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledButtonTile.LEDsetStateOn();
                } else {
                    ledButtonTile.LEDsetStateOff();

                }
            });

    }
}
