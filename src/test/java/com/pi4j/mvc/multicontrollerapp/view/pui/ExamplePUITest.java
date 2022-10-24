package com.pi4j.mvc.multicontrollerapp.view.pui;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.mvc.multicontrollerapp.controller.ApplicationController;
import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.Pi4JContext;

import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExamplePUITest {

    @Test
    void testLED() {
        //given
        ExampleModel          model      = new ExampleModel();
        ApplicationController controller = new ApplicationController(model);
        ExamplePUI            pui        = new ExamplePUI(controller, Pi4JContext.createMockContext());

        //when
        controller.setLedGlows(true);
        controller.awaitCompletion();
        pui.awaitCompletion();

        //then
        assertTrue(pui.led.getDigitalOutput().isOn());

        //when
        controller.setLedGlows(false);
        controller.awaitCompletion();
        pui.awaitCompletion();

        //then
        assertFalse(pui.led.getDigitalOutput().isOn());
    }

    @Test
    void testButton() {
        //given
        ExampleModel          model            = new ExampleModel();
        ApplicationController controller       = new ApplicationController(model);
        ExamplePUI            pui              = new ExamplePUI(controller, Pi4JContext.createMockContext());
        MockDigitalInput      mockDigitalInput = (MockDigitalInput) pui.button.getDigitalInput();

        int initialCounter = model.counter.getValue();

        //when
        mockDigitalInput.mockState(DigitalState.HIGH);
        mockDigitalInput.mockState(DigitalState.LOW);
        controller.awaitCompletion();

        //then
        assertEquals(initialCounter - 1, model.counter.getValue());
    }
}
