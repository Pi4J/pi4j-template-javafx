package com.pi4j.catalog.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;

import com.pi4j.catalog.ComponentTest;
import com.pi4j.catalog.components.base.PIN;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleLedTest extends ComponentTest {

    private SimpleLed led;
    private MockDigitalOutput digitalOutput;

    @BeforeEach
    public void setUp() {
        led = new SimpleLed(pi4j, PIN.D26);
        digitalOutput = led.mock();
    }

    @Test
    public void testInitialization() {
        assertEquals(26, led.pinNumber());
    }

    @Test
    public void testOn() {
        //when
        led.on();

        //then
        assertEquals(DigitalState.HIGH, digitalOutput.state());
        assertTrue(led.isOn());
    }

    @Test
    public void testOff() {
        //when
        led.off();

        //then
        assertEquals(DigitalState.LOW, digitalOutput.state());
        assertFalse(led.isOn());
    }


    @Test
    public void testToggle() {
        //given
        led.off();

        //when
        led.toggle();

        //then
        assertEquals(DigitalState.HIGH, digitalOutput.state());

        //when
        led.toggle();

        //then
        assertEquals(DigitalState.LOW, digitalOutput.state());
    }

}