package com.pi4j.components.components;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a joystick using 5 GPIO up, left, down, right and push  with Pi4J
 */
public class Joystick extends Component implements com.pi4j.components.interfaces.JoystickInterface {

    /**
     * Default debounce time in microseconds
     */
    protected static final long DEFAULT_DEBOUNCE = 10000;

    /**
     * Button component for joystick direction up
     */
    private final SimpleButton bNorth;
    /**
     * Button component for joystick direction left
     */
    private final SimpleButton bWest;
    /**
     * Button component for joystick direction down
     */
    private final SimpleButton bSouth;
    /**
     * Button component for joystick direction right
     */
    private final SimpleButton bEast;
    /**
     * Button component for joystick push
     */
    private final SimpleButton bPush;
    /**
     * Specifies if Joystick with push button
     */
    private final boolean pushIsPresent;

    /**
     * Creates a new joystick component with 5 custom GPIO address, a joystick with push button.
     *
     * @param pi4j     Pi4J context
     * @param addrNorth  GPIO address of button up
     * @param addrWest  GPIO address of button left
     * @param addrSouth  GPIO address of button down
     * @param addrEast  GPIO address of button right
     * @param addrPush  GPIO address of button push
     */
    public Joystick(Context pi4j, PIN addrNorth, PIN addrEast, PIN addrSouth, PIN addrWest, PIN addrPush){
        bNorth = new SimpleButton(pi4j, addrNorth, false, DEFAULT_DEBOUNCE);
        bWest  = new SimpleButton(pi4j, addrWest,  false, DEFAULT_DEBOUNCE);
        bSouth = new SimpleButton(pi4j, addrSouth, false, DEFAULT_DEBOUNCE);
        bEast  = new SimpleButton(pi4j, addrEast,  false, DEFAULT_DEBOUNCE);
        //joystick with push button
        bPush = new SimpleButton(pi4j, addrPush, false, DEFAULT_DEBOUNCE);
        pushIsPresent = true;
    }

    /**
     * Creates a new joystick component with 4 custom GPIO address, so no push button.
     *
     * @param pi4j     Pi4J context
     * @param addrNorth  GPIO address of button up
     * @param addrWest  GPIO address of button left
     * @param addrSouth  GPIO address of button down
     * @param addrEast  GPIO address of button right
     */
    public Joystick(Context pi4j, PIN addrNorth, PIN addrEast, PIN addrSouth, PIN addrWest){
        bNorth = new SimpleButton(pi4j, addrNorth, false, DEFAULT_DEBOUNCE);
        bWest  = new SimpleButton(pi4j,  addrWest,  false, DEFAULT_DEBOUNCE);
        bSouth = new SimpleButton(pi4j, addrSouth, false, DEFAULT_DEBOUNCE);
        bEast  = new SimpleButton(pi4j,  addrEast,  false, DEFAULT_DEBOUNCE);
        bPush  = null;
        //joystick without push button
        pushIsPresent = false;
    }

    /**
     * Returns a list of current state of the touch sensors
     *
     * @return a list of button states
     */
    public List<DigitalState> getStates(){

        List<DigitalState> buttonStates = new ArrayList<>();

        buttonStates.add(bNorth.getState());
        buttonStates.add(bEast.getState());
        buttonStates.add(bSouth.getState());
        buttonStates.add(bWest.getState());
        //only if joystick has a push button
        if (pushIsPresent){
            buttonStates.add(bPush.getState());
        }
        return buttonStates;
    }

    /**
     * Returns the current state of the button up
     *
     * @return Current button state
     */
    public DigitalState getStateNorth (){
        return bNorth.getState();
    }
    /**
     * Returns the current state of the button left
     *
     * @return Current button state
     */
    public DigitalState getStateWest (){
        return bWest.getState();
    }
    /**
     * Returns the current state of the button down
     *
     * @return Current button state
     */
    public DigitalState getStateSouth (){
        return bSouth.getState();
    }
    /**
     * Returns the current state of the button right
     *
     * @return Current button state
     */
    public DigitalState getStateEast (){
        return bEast.getState();
    }
    /**
     * Returns the current state of the button push
     *
     * @return Current button state
     */
    public DigitalState getStatePush (){
        return pushIsPresent ? bPush.getState() : DigitalState.UNKNOWN;
    }

    /**
     * Checks if button north is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean buttonNorthIsDown() {return bNorth.isDown();}

    /**
     * Checks if button north is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean buttonNorthIsUp() {return bNorth.isUp();}

    /**
     * Checks if button west is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean buttonWestIsDown() {return bWest.isDown();}

    /**
     * Checks if button west is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean buttonWestIsUp() {return bWest.isUp();}

    /**
     * Checks if button south is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean buttonSouthIsDown() {return bSouth.isDown();}

    /**
     * Checks if button south is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean buttonSouthIsUp() {return bSouth.isUp();}

    /**
     * Checks if button east is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean buttonEastIsDown() {return bEast.isDown();}

    /**
     * Checks if button east is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean buttonEastIsUp() {return bEast.isUp();}

    /**
     * Checks if button push is currently pressed
     *
     * @return True if button is pressed, False if button is not pressed or button does not exist
     */
    public boolean buttonPushIsDown() {
        return pushIsPresent && bPush.isDown();}

    /**
     * Checks if button push is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed, False if button is pressed od button does not exits
     */
    public boolean buttonPushIsUp() {
        return pushIsPresent && bPush.isUp();}


    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    @Override
    public void onNorth(Runnable handler) {
        bNorth.onDown(handler);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void whileNorth(long millis, Runnable method) {
        bNorth.whilePressed(method, millis);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    @Override
    public void onWest(Runnable handler) {
        bWest.onDown(handler);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void whileWest(long millis, Runnable method) {
        bWest.whilePressed(method, millis);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    @Override
    public void onSouth(Runnable handler) {
        bSouth.onDown(handler);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void whileSouth(long millis, Runnable method) {
        bSouth.whilePressed(method, millis);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    @Override
    public void onEast(Runnable handler) {
        bEast.onDown(handler);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void whileEast(long millis, Runnable method) {
        bEast.whilePressed(method, millis);
    }

    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    @Override
    public void onPushDown(Runnable handler) {
        if (pushIsPresent){
            bPush.onDown(handler);}
        else{
            logError("No runnable on pushDown");
        }
    }
    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void onPushUp(Runnable method) {
        if (pushIsPresent){
            bPush.onUp(method);
        }else{
            logError("No runnable on pushUp.");
        }
    }
    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void pushWhilePushed(long millis, Runnable method) {
        if(pushIsPresent){
            bPush.whilePressed(method, millis);
        }else{
            //throw error
            logError("No runnable on buttonPushWhilePressed.");
        }

    }

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInputButtonNorth(){return bNorth.getDigitalInput();}

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInputButtonWest(){return bWest.getDigitalInput();}

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInputButtonSouth(){return bSouth.getDigitalInput();}

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInputButtonEast(){return bEast.getDigitalInput();}

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInputButtonPush(){
        return pushIsPresent ? bPush.getDigitalInput() : null;
    }

    /**
     * disables all the handlers for every button and each
     * onUp, onDown and WhilePressed Events
     */
    @Override
    public void deRegisterAll(){
        bNorth.deRegisterAll();
        bWest.deRegisterAll();
        bSouth.deRegisterAll();
        bEast.deRegisterAll();
        if(pushIsPresent){
            bPush.deRegisterAll();
        }
    }

    /**
     * Returns the methode for OnDown
     * @return Runnable onDown
     */
    public Runnable getOnNorth(){
        return bNorth.getOnDown();
    }

    /**
     * Returns the methode for OnDown
     * @return Runnable onDown
     */
    public Runnable getOnEast(){
        return bEast.getOnDown();
    }

    /**
     * Returns the methode for OnDown
     * @return Runnable onDown
     */
    public Runnable getOnSouth(){
        return bSouth.getOnDown();
    }

    /**
     * Returns the methode for OnDown
     * @return Runnable onDown
     */
    public Runnable getOnWest(){
        return bWest.getOnDown();
    }

    /**
     * Returns the methode for OnDown
     * @return Runnable onDown
     */
    public Runnable getOnPush(){
        return pushIsPresent? bPush.getOnDown() : null;
    }

    /**
     * Returns the methode for whilePressed
     * @return Runnable whilePressed
     */
    public Runnable getWhileNorth(){
        return bNorth.getWhilePressed();
    }

    /**
     * Returns the methode for whilePressed
     * @return Runnable whilePressed
     */
    public Runnable getWhileEast(){
        return bEast.getWhilePressed();
    }

    /**
     * Returns the methode for whilePressed
     * @return Runnable whilePressed
     */
    public Runnable getWhileSouth(){
        return bSouth.getWhilePressed();
    }

    /**
     * Returns the methode for whilePressed
     * @return Runnable whilePressed
     */
    public Runnable getWhileWest(){
        return bWest.getWhilePressed();
    }

    /**
     * Returns the methode for whilePressed
     * @return Runnable whilePressed
     */
    public Runnable getWhilePush(){
        return pushIsPresent? bPush.getWhilePressed() : null;
    }
}
