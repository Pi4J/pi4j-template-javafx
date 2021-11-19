package com.pi4j.jfx.multicontrollerapp.controller;

import com.pi4j.jfx.multicontrollerapp.model.ExampleModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

        //then
        controller.runLater(m ->  assertEquals(initialCount + 1, m.counter.getValue()));

        //when
        controller.decreaseCounter();

        //then
        controller.runLater(m -> assertEquals(initialCount, m.counter.getValue()));
    }

    @Test
    void testLED() {
        //given
        ExampleModel model = new ExampleModel();

        ApplicationController controller = new ApplicationController(model);

        //when
        controller.setLedGlows(true);

        //then
        controller.runLater(m-> assertTrue(m.ledGlows.getValue()));

        //when
        controller.setLedGlows(false);

        //then
        controller.runLater(m-> assertFalse(m.ledGlows.getValue()));
    }


}
