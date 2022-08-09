package com.pi4j.components.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

import com.pi4j.components.ComponentTest;
import com.pi4j.components.components.helpers.PIN;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SimpleLEDTest extends ComponentTest {

    protected SimpleLED led;


    @BeforeEach
    public void setUp() {
        this.led = new SimpleLED(pi4j, PIN.D26);
    }

    @Test
    public void testLED_Address() {

        //then
        assertEquals(26, led.pin());
    }

    @Test
    public void testSetState_On() {
        //when
        led.on();
        //then
        assertEquals(DigitalState.HIGH, led.digitalOutput.state());
    }

    @Test
    public void testSetState_Off() {
        //when
        led.off();
        //then
        assertEquals(DigitalState.LOW, led.digitalOutput.state());
    }


    @Test
    public void testToggleState() {
        //when
        led.toggle();
        //then
        assertTrue(led.glows());
    }

}
