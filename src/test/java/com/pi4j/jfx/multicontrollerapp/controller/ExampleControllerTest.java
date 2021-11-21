package com.pi4j.jfx.multicontrollerapp.controller;

import com.pi4j.jfx.multicontrollerapp.model.ExampleModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExampleControllerTest {

    @Test
    void testCounter() {
        //given
        ExampleModel model        = new ExampleModel();
        int          initialCount = model.counter.getValue();

        ApplicationController controller = new ApplicationController(model);

        //when
        controller.increaseCounter();
        controller.awaitCompletion();

        //then
        assertEquals(initialCount + 1, model.counter.getValue());

        //when
        controller.decreaseCounter();
        controller.awaitCompletion();

        //then
        assertEquals(initialCount, model.counter.getValue());
    }

    @Test
    void testLED() {
        //given
        ExampleModel model = new ExampleModel();

        ApplicationController controller = new ApplicationController(model);

        //when
        controller.setLedGlows(true);
        controller.awaitCompletion();
        
        //then
        assertTrue(model.ledGlows.getValue());

        //when
        controller.setLedGlows(false);
        controller.awaitCompletion();

        //then
        assertFalse(model.ledGlows.getValue());
    }


}
