package com.pi4j.catalog.components;

import com.pi4j.catalog.ComponentTest;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SimpleLEDTest extends ComponentTest {

    protected SimpleLED led;

    protected DigitalOutput digitalOutput;

    @BeforeEach
    public void setUp() {
        this.led = new SimpleLED(pi4j, PIN.D26);
        this.digitalOutput = led.getDigitalOutput();
    }

    @Test
    public void testLED_Address() {
        //when
        digitalOutput = led.getDigitalOutput();
        //then
        assertEquals(26, digitalOutput.getAddress());

    }

    @Test
    public void testSetState_On() {
        //when
        led.setState(true);
        //then
        assertEquals(DigitalState.HIGH, digitalOutput.state());
    }

    @Test
    public void testSetState_Off() {
        //when
        led.setState(false);
        //then
        assertEquals(DigitalState.LOW, digitalOutput.state());
    }

    @Test
    public void testSetStateOn() {
        //when
        led.on();
        //then
        assertEquals(DigitalState.HIGH, digitalOutput.state());
    }

    @Test
    public void testSetStateOff() {
        //given
        led.on();
        //when
        led.off();
        //then
        assertEquals(DigitalState.LOW, digitalOutput.state());
    }

    @Test
    public void testToggleState() {
        //when
        led.toggleState();
        //then
        assertEquals(DigitalState.HIGH, digitalOutput.state());
    }

}
