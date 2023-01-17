package com.pi4j.mvc.templatepuiapp.controller;

import com.pi4j.mvc.templatepuiapp.model.SomeModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SomeControllerTest {

    @Test
    void testActivate() {
        //given
        SomeModel      model      = new SomeModel();
        SomeController controller = new SomeController(model);

        //when
        controller.activate();
        controller.awaitCompletion();

        //then
        assertTrue(model.busy.getValue());
        assertEquals(0, model.counter.getValue());  //counter is increased on  'deactivate'
    }

    @Test
    void testDeactivate() {
        //given
        SomeModel      model      = new SomeModel();
        SomeController controller = new SomeController(model);

        //when
        controller.deactivate();
        controller.awaitCompletion();

        //then
        assertFalse(model.busy.getValue()); //only busy on'activate'
        assertEquals(1, model.counter.getValue());
    }

    @Test
    void testIsNotBusyAndNotTerminated() {
        //given
        SomeModel      model      = new SomeModel();
        SomeController controller = new SomeController(model);

        int count = controller.terminationCount;

        //when
        for (int i = 0; i < count; i++) {
            controller.deactivate();
        }
        controller.awaitCompletion();

        //then
        assertFalse(model.busy.getValue());
        assertEquals(count, model.counter.getValue());
    }

    @Test
    void testTermination() {
        //given
        SomeModel      model      = new SomeModel();
        TestController controller = new TestController(model);

        //when
        for (int i = 0; i < controller.terminationCount; i++) {
            controller.deactivate();
        }
        controller.awaitCompletion();

        //then
        assertFalse(controller.terminateCalled);

        //when
        controller.deactivate();
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