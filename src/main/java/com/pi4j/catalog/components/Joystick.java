package com.pi4j.catalog.components;

import java.time.Duration;

import com.pi4j.context.Context;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

import com.pi4j.catalog.components.base.Component;
import com.pi4j.catalog.components.base.PIN;

/**
 * Implementation of a joystick using 5 GPIO up, left, down, right and push with Pi4J
 */
public class Joystick extends Component {

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
        bNorth = new SimpleButton(pi4j, addrNorth, false);
        bWest  = new SimpleButton(pi4j, addrWest,  false);
        bSouth = new SimpleButton(pi4j, addrSouth, false);
        bEast  = new SimpleButton(pi4j, addrEast,  false);

        //joystick has push button
        if(addrPush != null){
            bPush = new SimpleButton(pi4j, addrPush, false);
        }
        else {
            bPush = null;
        }
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
        this(pi4j, addrNorth, addrEast, addrSouth, addrWest, null);
    }

    /**
     * Checks if button north is currently pressed
     *
     * @return true if button is pressed
     */
    public boolean isNorth() {
        return bNorth.isDown();
    }

    /**
     * Checks if button west is currently pressed
     *
     * @return true if button is pressed
     */
    public boolean isWest() {
        return bWest.isDown();
    }

    /**
     * Checks if button south is currently pressed
     *
     * @return true if button is pressed
     */
    public boolean isSouth() {
        return bSouth.isDown();
    }

    /**
     * Checks if button east is currently pressed
     *
     * @return true if button is pressed
     */
    public boolean isEast() {
        return bEast.isDown();
    }

    /**
     * Checks if button push is currently pressed
     *
     * @return true if button is pressed, False if button is not pressed or button does not exist
     */
    public boolean isPushed() {
        return pushIsPresent() && bPush.isDown();
    }

    /**
     * Checks if button push is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed, False if button is pressed od button does not exits
     */
    public boolean isCenter() {
        return bEast.isUp() &&
                bWest.isUp() &&
                bNorth.isUp() &&
                bSouth.isUp() &&
                (!pushIsPresent() || (pushIsPresent() && bPush.isUp()));}


    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onNorth(Runnable task) {
        bNorth.onDown(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whileNorth(Runnable task, Duration delay) {
        bNorth.whilePressed(task, delay);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onWest(Runnable task) {
        bWest.onDown(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whileWest(Runnable task, Duration delay) {
        bWest.whilePressed(task, delay);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onSouth(Runnable task) {
        bSouth.onDown(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whileSouth(Runnable task, Duration delay) {
        bSouth.whilePressed(task, delay);
    }
    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onEast(Runnable task) {
        bEast.onDown(task);
    }

    public void onCenter(Runnable task) {
        bNorth.onUp(task);
        bSouth.onUp(task);
        bEast.onUp(task);
        bWest.onUp(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whileEast(Runnable task, Duration delay) {
        bEast.whilePressed(task, delay);
    }

    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param handler Event handler to call or null to disable
     */
    public void onPushDown(Runnable handler) {
        if (pushIsPresent()){
            bPush.onDown(handler);}
        else{
            throw new IllegalStateException("No runnable on pushDown allowed if no push button is present");
        }
    }
    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onPushUp(Runnable task) {
        if (pushIsPresent()){
            bPush.onUp(task);
        }else{
            throw new IllegalStateException("No runnable on pushDown allowed if no push button is present");
        }
    }
    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whilePushed(Runnable task, Duration delay) {
        if (pushIsPresent()) {
            bPush.whilePressed(task, delay);
        } else {
            throw new IllegalStateException("No push button available, can't do a whilePushed");
        }
    }


    @Override
    public void reset(){
        bNorth.reset();
        bWest.reset();
        bSouth.reset();
        bEast.reset();
        if(pushIsPresent()){
            bPush.reset();
        }
    }

    public boolean isInInitialState(){
        return bNorth.isInInitialState() &&
                bWest.isInInitialState() &&
                bSouth.isInInitialState() &&
                bEast.isInInitialState() &&
                (!pushIsPresent() || (pushIsPresent() && bPush.isInInitialState()));
    }

    private boolean pushIsPresent(){
        return bPush != null;
    }



    // --------------- for testing --------------------

    MockDigitalInput mockNorth() {
        return bNorth.mock();
    }

    MockDigitalInput mockSouth() {
        return bSouth.mock();
    }

    MockDigitalInput mockEast() {
        return bEast.mock();
    }

    MockDigitalInput mockWest() {
        return bWest.mock();
    }

    MockDigitalInput mockPush() {
        if(pushIsPresent()){
            return bPush.mock();
        }
        else {
            throw new IllegalStateException("No push button available, no DigitalInput available");
        }
    }
}
