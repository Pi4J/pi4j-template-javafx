package com.pi4j.mvc.templateapp.view.pui.components;

import com.pi4j.mvc.util.piucomponentbase.ComponentTest;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LEDComponentTest extends ComponentTest {
    private LEDComponent      led;
    private MockDigitalOutput dout;

    @BeforeEach
    void setup(){
        led = new LEDComponent(pi4j, 26);
        dout = toMock(led.digitalOutput);
    }

    @Test
    void testInitialization(){
        assertFalse(led.glows());
    }

    @Test
    void testOn(){
        //when
        led.on();

        //then
        assertTrue(dout.isHigh());
    }

    @Test
    void testOff(){
        //when
        led.off();

        //then
        assertTrue(dout.isLow());
    }

    @Test
    void testGlows(){
        //when
        dout.high();

        //then
        assertTrue(led.glows());

        //when
        dout.low();

        //then
        assertFalse(led.glows());
    }

}