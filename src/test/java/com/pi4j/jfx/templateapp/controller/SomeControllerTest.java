package com.pi4j.jfx.templateapp.controller;


import com.pi4j.jfx.templateapp.model.SomeModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SomeControllerTest {
    @Test
    void testCounter() {
        //given
        SomeModel model        = new SomeModel();
        int          initialCount = model.counter.getValue();

        SomeController controller = new SomeController(model);

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
        SomeModel model = new SomeModel();

        SomeController controller = new SomeController(model);

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