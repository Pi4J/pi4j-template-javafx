package com.pi4j.mvc.templatepuiapp.controller;

import com.pi4j.mvc.templatepuiapp.model.SomeModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SomeControllerTest {

    @Test
    void testLedOn() {
        //given
        SomeModel      model      = new SomeModel();
        SomeController controller = new SomeController(model);

        //when
        controller.ledOn();
        controller.awaitCompletion();

        //then
        assertTrue(model.ledGlows.getValue());
        assertEquals(0, model.counter.getValue());
    }

    @Test
    void testLedOff() {
        //given
        SomeModel      model      = new SomeModel();
        SomeController controller = new SomeController(model);

        int count = controller.terminationCount;

        //when
        for (int i = 0; i < count; i++) {
            controller.ledOff();
        }
        controller.awaitCompletion();

        //then
        assertFalse(model.ledGlows.getValue());
        assertEquals(count, model.counter.getValue());
    }

    @Test
    void testTermination() {
        //given
        SomeModel      model      = new SomeModel();
        TestController controller = new TestController(model);

        //when
        for (int i = 0; i < controller.terminationCount; i++) {
            controller.ledOff();
        }
        controller.awaitCompletion();

        //then
        assertFalse(controller.terminateCalled);

        //when
        controller.ledOff();
        controller.awaitCompletion();

        //then
        assertTrue(controller.terminateCalled);
    }

    private static class TestController extends SomeController {
        private boolean terminateCalled = false;

        public TestController(SomeModel model) {
            super(model);
        }

        @Override
        protected void terminate() {
            terminateCalled = true;
        }
    }
}