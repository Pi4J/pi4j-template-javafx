package com.pi4j.mvc.util.mvcbase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerBaseTest {
    private TestModel                 model;
    private ControllerBase<TestModel> controller;

    @BeforeEach
    void setup() {
        model = new TestModel();
        controller = new ControllerBase<>(model) {
        };
    }

    @Test
    void testInitialization() {
        //then
        assertSame(model, controller.getModel());
    }

    @Test
    void testSetValue() {
        //given
        int     newInt  = 42;
        boolean newBool = true;

        //when
        controller.setValue(model.someInt, newInt);
        controller.setValue(model.someBoolean, newBool);

        controller.awaitCompletion();

        //then
        assertEquals(newInt,  model.someInt.getValue());
        assertEquals(newBool, model.someBoolean.getValue());
    }

    @Test
    void testToggle() {
        //given
        model.someBoolean.setValue(true);

        //when
        controller.toggle(model.someBoolean);
        controller.awaitCompletion();

        //then
        assertFalse(model.someBoolean.getValue());

        //when
        controller.toggle(model.someBoolean);
        controller.awaitCompletion();

        //then
        assertTrue(model.someBoolean.getValue());
    }

    private static class TestModel {
        final ObservableValue<Integer> someInt     = new ObservableValue<>(73);
        final ObservableValue<Boolean> someBoolean = new ObservableValue<>(false);
    }

}
