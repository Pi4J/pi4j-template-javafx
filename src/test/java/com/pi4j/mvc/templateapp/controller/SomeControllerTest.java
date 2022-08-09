package com.pi4j.mvc.templateapp.controller;

import com.pi4j.mvc.templateapp.model.SomeModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SomeControllerTest {
    @Test
    void testCounter() {
        //given
        SomeModel model        = new SomeModel();
        int       initialCount = model.counter.getValue();

        SomeController controller = new SomeController(model);

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
        SomeModel model = new SomeModel();

        SomeController controller = new SomeController(model);

        //when
        controller.setIsActive(true);
        controller.awaitCompletion();

        //then
        assertTrue(model.isActive.getValue());

        //when
        controller.setIsActive(false);
        controller.awaitCompletion();

        //then
        assertFalse(model.isActive.getValue());
    }

}