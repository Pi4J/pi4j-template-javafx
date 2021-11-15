package com.pi4j.jfx.exampleapp.controller;

import com.pi4j.jfx.exampleapp.model.ExampleModel;

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

        ExampleController controller = new ExampleController(model);

        //when
        controller.increaseCounter();

        //then
        assertEquals(initialCount + 1, model.counter.getValue());

        //when
        controller.decreaseCounter();

        //then
        assertEquals(initialCount, model.counter.getValue());
    }

    @Test
    void testLED() {
        //given
        ExampleModel model = new ExampleModel();

        ExampleController controller = new ExampleController(model);

        //when
        controller.setLedGlows(true);

        //then
        assertTrue(model.ledGlows.getValue());

        //when
        controller.setLedGlows(false);

        //then
        assertFalse(model.ledGlows.getValue());
    }

    @Test
    void testBlink() {
        //given
        ExampleModel model = new ExampleModel();
        Boolean      initial = model.blinkingTrigger.getValue();

        ExampleController controller = new ExampleController(model);

        //when
        controller.blink();

        //then
        assertNotEquals(initial, model.blinkingTrigger.getValue());

        //when
        controller.blink();

        //then
        assertEquals(initial, model.blinkingTrigger.getValue());
    }

}