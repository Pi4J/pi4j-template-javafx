package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.interfaces.LEDButtonInterface;
import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.interfaces.LedMatrixInterface;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.interfaces.SimpleLEDInterface;
import com.pi4j.components.tiles.*;
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

    private JoystickAnalogTile joystickAnalogTile;

    private LEDStripInterface ledStrip;

    private LedMatrixInterface ledMatrix;

    public SomeGUI(SomeController controller) {
        super(4,2);
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
        ledStrip = new LedStripTile(4,1.0);
        ledMatrix = new LedMatrixTile(4, 3, 0.8);
        joystickAnalogTile = new JoystickAnalogTile();
    }

    @Override
    public void layoutParts() {
        getChildren().addAll((Tile) led, (Tile) button, (Tile) ledButton, (Tile) joystick, (Tile) joystickAnalogTile,(Tile) ledStrip, (Tile)ledMatrix);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        button.onDown(() -> controller.setButtonPressed(true));
        button.onUp  (() -> controller.setButtonPressed(false));
        button.whilePressed(() -> controller.whileMessage("Simple"),5000);

        //Send message for short and long press
        joystick.onNorthUp(() -> controller.buttonMessage("Up",false));
        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.onSouthUp(() -> controller.buttonMessage("Down",false));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.onWestUp(() -> controller.buttonMessage("Left", false));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.onEastUp(() -> controller.buttonMessage("Right",false));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));

        ledButton.onDown(() -> controller.buttonMessage("LED",true));
        ledButton.onUp(controller::setLedButtonReleased);
        ledButton.btnwhilePressed(controller::whilePressedLedButton, 1000);

        //click/push change all color of ledstrip
        joystick.onPushDown(() -> {
            controller.ledStripPush(ledStrip);
            controller.ledMatrixPush(ledMatrix);
        });

        //turn off ledstrip
        joystick.onPushUp(() -> {
            controller.ledStripOff(ledStrip);
            controller.ledMatrixOff(ledMatrix);
        });

        //change one led light of ledstrip
        //change one led light of second row of matrix
        joystick.onNorthDown(() -> {
            controller.ledStripDirection(ledStrip,1);
            controller.ledMatrixDirection(ledMatrix,2,1);
        });
        joystick.onEastDown(() -> {
            controller.ledStripDirection(ledStrip,2);
            controller.ledMatrixDirection(ledMatrix,2,2);
        });
        joystick.onSouthDown(() -> {
            controller.ledStripDirection(ledStrip,3);
            controller.ledMatrixDirection(ledMatrix,2,3);
        });
        joystick.onWestDown(() -> {
            controller.ledStripDirection(ledStrip,4);
            controller.ledMatrixDirection(ledMatrix,2,4);
        });

        joystick.pushWhilePushed(3000,() -> {
            //hold change brightness all ledstrip lights
            controller.setStripBrightness(ledStrip, 0.7);
            //hold change color of first strip of matrix
            controller.changeFirstMatrixStrip(ledMatrix);
        });

        joystickAnalogTile.xOnMove(value -> controller.getX(value));
        joystickAnalogTile.yOnMove(value -> controller.getY(value));
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

        onChangeOf(model.currentXPosition)
            .execute((oldValue, newValue) -> {
                System.out.println("X Position: " + newValue);
            });

        onChangeOf(model.currentYPosition)
            .execute((oldValue, newValue) -> {
                System.out.println("Y Position: " + newValue);
            });
    }
}
