package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickAnalogInterface;
import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.interfaces.LEDButtonInterface;
import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.interfaces.LedMatrixInterface;
import com.pi4j.components.interfaces.PotentiometerInterface;
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
import com.pi4j.components.tiles.helper.PixelColor;

public class SomeGUI extends FlowGridPane implements ViewMixin<SomeModel, SomeController> { //all GUI-elements have to implement ViewMixin

    // declare all the UI elements you need
    private SimpleLEDInterface led;
    private SimpleButtonInterface button;
    private JoystickInterface joystick;
    private LEDButtonInterface ledButton;

    private JoystickAnalogInterface joystickAnalog;

    private LEDStripInterface ledStrip;

    private LedMatrixInterface ledMatrix;

    private PotentiometerInterface potentiometer;

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
        joystick = new JoystickTile(PIN.PWM18,PIN.D23, PIN.PWM12,PIN.D16, PIN.D21);
        ledButton = new LedButtonTile(PIN.D26,PIN.D20);
        ledStrip = new LedStripTile(4,1.0, "SPI0 MOSI");
        ledMatrix = new LedMatrixTile(4, 4, 0.8, "SPI0 MOSI");
        joystickAnalog = new JoystickAnalogTile(PIN.D6, "0x01");
        potentiometer = new PotentiometerTile(PIN.D5,"0x01");
    }

    @Override
    public void layoutParts() {
        getChildren().addAll((Tile) led, (Tile) button, (Tile) ledButton, (Tile) joystick,
            (Tile) joystickAnalog,(Tile) ledStrip, (Tile)ledMatrix, (Tile)potentiometer);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        button.onDown(() -> {
            controller.setButtonPressed(true);
            controller.singlePotentiometer(potentiometer.singleShotGetNormalizedValue());
        });
        button.onUp  (() -> controller.setButtonPressed(false));
        button.whilePressed(() -> controller.whileMessage("Simple"),5000);

        //Send message for short and long press
        joystick.onNorthUp(() -> controller.sendMessage("Up",false));
        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.onSouthUp(() -> controller.sendMessage("Down",false));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.onWestUp(() -> controller.sendMessage("Left", false));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.onEastUp(() -> controller.sendMessage("Right",false));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));

        ledButton.onDown(() -> controller.sendMessage("LED",true));
        ledButton.onUp(controller::setLedButtonReleased);
        ledButton.btnwhilePressed(controller::whilePressedLedButton, 1000);

        //click/push change all color of ledstrip
        joystick.onPushDown(() -> {
            ledStrip.setStripColor(PixelColor.YELLOW);
            ledMatrix.setMatrixColor(PixelColor.RED);
            ledStrip.render();
            ledMatrix.render();
            controller.sendMessage("Joystick",true);
        });

        //turn off ledstrip
        joystick.onPushUp(() -> {
            ledStrip.allOff();
            ledMatrix.allOff();
            ledStrip.render();
            ledMatrix.render();
            controller.sendMessage("Joystick",false);
        });

        //change one led light of ledstrip
        //change one led light of second row of matrix
        int stripcolor = PixelColor.GREEN;
        int matrixcolor = PixelColor.PURPLE;

        joystick.onNorthDown(() -> {
            int pixel = 0;
            ledStrip.setPixelColor(pixel,stripcolor);
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            ledStrip.render();
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });
        joystick.onEastDown(() -> {
            int pixel = 1;
            ledStrip.setPixelColor(pixel,stripcolor);
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            ledStrip.render();
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        joystick.onSouthDown(() -> {
            int pixel = 2;
            ledStrip.setPixelColor(pixel,stripcolor);
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            ledStrip.render();
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        joystick.onWestDown(() -> {
            int pixel = 3;
            ledStrip.setPixelColor(pixel,stripcolor);
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            ledStrip.render();
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        joystick.pushWhilePushed(3000,() -> {
            //hold change brightness all ledstrip lights
            double brightness = 0.7;
            ledStrip.setBrightness(brightness);
            ledStrip.render();
            controller.brightnessMessage(brightness);
            //hold change color of first strip of matrix
            ledMatrix.setStripColor(0, PixelColor.BLUE);
            ledMatrix.render();
        });

        joystickAnalog.xOnMove(controller::setX);
        joystickAnalog.yOnMove(controller::setY);

        joystickAnalog.pushOnDown(() -> controller.sendMessage("Joystick analog", true));
        joystickAnalog.pushOnUp  (() -> controller.sendMessage("Joystick analog",false));
        joystickAnalog.pushWhilePressed(() -> controller.whileMessage("Joystick analog"),3000);


        potentiometer.setConsumerSlowReadChan(controller::setPotiX);
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
                if (newValue.equals(oldValue)) {
                    System.out.println("Joystick Analog - X Position: " + newValue);
                }
                    });

        onChangeOf(model.currentYPosition)
            .execute((oldValue, newValue) -> {
                if (newValue.equals(oldValue)) {
                    System.out.println("Joystick Analog - Y Position: " + newValue);
                }
            });

        onChangeOf(model.currentPotiPosition)
            .execute((oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) {
                    System.out.println("Potentiometer - " + newValue+"%");
                }
            });
    }
}
