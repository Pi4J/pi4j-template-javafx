package com.pi4j.mvc.tilesapp.controller;

import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.interfaces.LedMatrixInterface;
import com.pi4j.components.tiles.LedMatrixTile;
import com.pi4j.components.tiles.LedStripTile;
import com.pi4j.components.tiles.helper.PixelColor;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;


public class SomeController extends ControllerBase<SomeModel> {

    public SomeController(SomeModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and from nowhere else)

    // Gives the model a boolean, if the button is pressed or not.
    public void setButtonPressed(boolean is) {
        setValue(model.isButtonPressed, is);
    }

    //turn off ledbutton led
    public void setLedButtonReleased() {
        setValue(model.isLedButtonActive, false);
    }

    //switch led of ledbutton on and off depending on delay
    public void whilePressedLedButton() {
        setValue(model.isLedButtonActive, !model.isLedButtonActive.getValue());
    }

    // Sends message as output
    public void whileMessage(String type){
        System.out.println(type+" button is still pressed");
    }

    //sends message when pressed or released
    public void buttonMessage(String type, boolean isPressed){
        String state = "released";
        if(isPressed) {
             state = "pressed";
        }

        System.out.println(type +" button is "+ state);
    }

    //change all pixels of matrix to yellow
    public void ledStripPush(LEDStripInterface tile){
        tile.setStripColor(PixelColor.YELLOW);
        tile.render();
    }

    //shuts off all pixels of ledstrip
    public void ledStripOff(LEDStripInterface tile){
        tile.allOff();
    }
    //change defined pixel of the ledstrip to green
    public void ledStripDirection(LEDStripInterface tile, int pixel){
        tile.setPixelColor(pixel-1, PixelColor.GREEN);
        tile.render();
    }

    //change brightness of ledstrip
    public void setStripBrightness(LEDStripInterface tile, double brightness){
        tile.setBrightness(brightness);
        tile.render();
    }

    //change defined pixel of the matrix to purple
    public void ledMatrixDirection(LedMatrixInterface tile, int row, int pixel){
        tile.setPixelColor(row-1, pixel-1, PixelColor.PURPLE);
        tile.render();
    }

    //change all pixels of matrix to red
    public void ledMatrixPush(LedMatrixInterface tile){
        tile.setMatrixColor(PixelColor.RED);
        tile.render();
    }

    //shuts off all pixels of matrix
    public void ledMatrixOff(LedMatrixInterface tile){
        tile.allOff();
    }

    //change first strip of matrix to blue
    public void changeFirstMatrixStrip(LedMatrixInterface tile){
        tile.setStripColor(0, PixelColor.BLUE);
        tile.render();
    }

    public void setX() {

    }
}
