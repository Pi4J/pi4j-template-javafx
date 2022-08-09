package com.pi4j.mvc.multicontrollerapp.view.pui;

import com.pi4j.mvc.multicontrollerapp.controller.ApplicationController;
import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.Pi4JContext;

import org.junit.jupiter.api.Test;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

import com.pi4j.components.ComponentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExamplePUITest extends ComponentTest {

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
        assertTrue(pui.led.glows());

        //when
        controller.setLedGlows(false);
        controller.awaitCompletion();
        pui.awaitCompletion();

        //then
        assertFalse(pui.led.glows());
    }

    @Test
    void testButton() {
        //given
        ExampleModel          model      = new ExampleModel();
        ApplicationController controller = new ApplicationController(model);
        ExamplePUI            pui        = new ExamplePUI(controller, Pi4JContext.createMockContext());

        int initialCounter = model.counter.getValue();

        MockDigitalInput digitalInput = toMock(pui.button.getDigitalInput());
        digitalInput.mockState(DigitalState.HIGH);

        //when
        digitalInput.mockState(DigitalState.LOW);
        controller.awaitCompletion();

        //then
        assertEquals(initialCounter - 1, model.counter.getValue());
    }
}
