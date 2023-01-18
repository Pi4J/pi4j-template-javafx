package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.components.Ads1115;
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
import com.pi4j.context.Context;
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

    // Declare all the UI elements you need
    private SimpleLEDInterface      led;
    private SimpleButtonInterface   button;
    private JoystickInterface       joystick;
    private LEDButtonInterface      ledButton;
    private JoystickAnalogInterface joystickAnalog;
    private LEDStripInterface       ledStrip;
    private LedMatrixInterface      ledMatrix;
    private PotentiometerInterface  potentiometer;


    private final int DEFAULT_SPI_CHANNEL = 0;
    private final int[][] matrix = new int[3][4];
    Context pi4J;

    private Ads1115 ads1115;

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
        getStyleClass().add("root-pane");
    }

    // Initialize all tiles which are in GUI wanted
    @Override
    public void initializeParts() {
        led = new SimpleLEDTile(pi4J,PIN.D17);
        button = new SimpleButtonTile(pi4J,PIN.D24,false);
        ledButton = new LedButtonTile(pi4J,PIN.PWM18,false,PIN.D20);
        joystick = new JoystickTile(pi4J, PIN.D5, PIN.D6, PIN.PWM13, PIN.PWM19);
        potentiometer = new PotentiometerTile(ads1115, DEFAULT_SPI_CHANNEL, 3.3);
        joystickAnalog = new JoystickAnalogTile(pi4J, ads1115, 1, 2, 3.3, false, PIN.D26);
        ledStrip = new LedStripTile(pi4J,4,1.0, DEFAULT_SPI_CHANNEL);
        ledMatrix = new LedMatrixTile(pi4J, matrix, 0.8, DEFAULT_SPI_CHANNEL);
    }

    // Sets layout of tiles
    @Override
    public void layoutParts() {
        getChildren().addAll((Tile) led, (Tile) button, (Tile) ledButton, (Tile) joystick,
            (Tile) joystickAnalog, (Tile)potentiometer,(Tile) ledStrip, (Tile)ledMatrix);
    }

    // All EventHandlers from tiles which are needed
    @Override
    public void setupUiToActionBindings(SomeController controller) {
        button.onDown(() -> {
            // Sends button state boolean false
            controller.setButtonPressed(true);
            // Sends the current normalized potentiometer position
            controller.singlePotentiometer(potentiometer.singleShotGetNormalizedValue());
        });
        // Sends component name and button state boolean false
        button.onUp  (() -> controller.setButtonPressed(false));
        // Sends component name and amount of delay in milliseconds
        button.whilePressed(() -> controller.whileMessage("Simple"),5000);

        // Sends message to controller which direction and if short or long press
        joystick.onNorthUp(() -> controller.sendMessage("Up",false));
        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.onSouthUp(() -> controller.sendMessage("Down",false));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.onWestUp(() -> controller.sendMessage("Left", false));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.onEastUp(() -> controller.sendMessage("Right",false));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));

        // Sends component name and button state boolean true
        ledButton.onDown(() -> controller.sendMessage("LED",true));
        // Sends  button state boolean false
        ledButton.onUp(controller::setLedButtonReleased);
        // Sends button state boolean true for the amount of delay in milliseconds
        ledButton.btnwhilePressed(controller::whilePressedLedButton, 1000);

        // On press of joystick button click/push change all color of ledstrip
        joystick.onPushDown(() -> {
            ledStrip.setStripColor(PixelColor.YELLOW);
            ledMatrix.setMatrixColor(PixelColor.RED);
            ledStrip.render();
            ledMatrix.render();
            controller.sendMessage("Joystick",true);
        });

        // On press of joystick button turn off ledstrip
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

        // On press of up joystick button
        joystick.onNorthDown(() -> {
            int pixel = 0;
            // changes pixel color of the first led of the LED-Strip
            ledStrip.setPixelColor(pixel,stripcolor);
            // changes pixel color of the first led of the second strip of the LED-Matrix
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            // Re-Render Ledstrip
            ledStrip.render();
            // Re-Render ledMatrix
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        // On press of right joystick button
        joystick.onEastDown(() -> {
            int pixel = 1;
            // changes pixel color of the second led of the LED-Strip
            ledStrip.setPixelColor(pixel,stripcolor);
            // changes pixel color of the second led of the second strip of the LED-Matrix
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            // Re-Render Ledstrip
            ledStrip.render();
            // Re-Render ledMatrix
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        // On press of down joystick button
        joystick.onSouthDown(() -> {
            int pixel = 2;
            // changes pixel color of the third led of the LED-Strip
            ledStrip.setPixelColor(pixel,stripcolor);
            // changes pixel color of the third led of the second strip of the LED-Matrix
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            // Re-Render Ledstrip
            ledStrip.render();
            // Re-Render ledMatrix
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        // On press of left joystick button
        joystick.onWestDown(() -> {
            int pixel = 3;
            // changes pixel color of the fourth led of the LED-Strip
            ledStrip.setPixelColor(pixel,stripcolor);
            // changes pixel color of the fourth led of the second strip of the LED-Matrix
            ledMatrix.setPixelColor(1, pixel, matrixcolor);
            // Re-Render Ledstrip
            ledStrip.render();
            // Re-Render ledMatrix
            ledMatrix.render();
            controller.pixelMessage(pixel);
        });

        // Activates after provided time (in milliseconds)
        joystick.pushWhilePushed(3000,() -> {

            // Hold change brightness all ledstrip lights
            double brightness = 0.7;
            ledStrip.setBrightness(brightness);
            // Re-Render Ledstrip
            ledStrip.render();
            controller.brightnessMessage(brightness);

            // Hold to change color of first strip of matrix
            ledMatrix.setStripColor(0, PixelColor.BLUE);
            // Re-Render ledMatrix
            ledMatrix.render();
        });

        joystickAnalog.xOnMove(controller::setJoyAnalogX);
        joystickAnalog.yOnMove(controller::setJoyAnalogY);

        // Sends component name and button state boolean true
        joystickAnalog.pushOnDown(() -> controller.sendMessage("Joystick analog", true));

        // Sends component name and button state boolean false
        joystickAnalog.pushOnUp  (() -> controller.sendMessage("Joystick analog",false));

        // Sends component name and amount of delay in milliseconds
        joystickAnalog.pushWhilePressed(() -> controller.whileMessage("Joystick analog"),3000);

        // Provide Consumer with normalized position of Potentiometer
        potentiometer.setConsumerSlowReadChan(controller::setPotiX);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {

        // Observes isButtonPressed ObservableValue
        // Press button to turn on/off SimpleLED
        onChangeOf(model.isButtonPressed)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    led.on();
                } else {
                    led.off();

                }
            });

        // Observes isLedButtonActive ObservableValue
        // Press button to turn on/off LEDButton
        onChangeOf(model.isLedButtonActive)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledButton.LEDsetStateOn();
                } else {
                    ledButton.LEDsetStateOff();

                }
            });

        // Observes currentXPosition ObservableValue
        // Prints out normalized X-Position, if value changed
        onChangeOf(model.currentXPosition)
                .execute((oldValue, newValue) -> System.out.println("Joystick Analog-X Position: " + newValue));

        // Observes currentYPosition ObservableValue
        // Prints out normalized Y-Position, if value changed
        onChangeOf(model.currentYPosition)
                .execute((oldValue, newValue) -> System.out.println("Joystick Analog-Y Position: " + newValue));

        // Observes currentPotiPosition ObservableValue
        // Prints out Potentiometer position, if value changed
        onChangeOf(model.currentPotiPosition)
            .execute((oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) {
                    System.out.println("Potentiometer - " + newValue);
                }
            });
    }
}
