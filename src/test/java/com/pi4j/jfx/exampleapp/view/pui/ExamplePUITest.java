package com.pi4j.jfx.exampleapp.view.pui;

import java.time.Duration;

import com.pi4j.jfx.exampleapp.controller.ExampleController;
import com.pi4j.jfx.exampleapp.model.ExampleModel;
import com.pi4j.jfx.exampleapp.view.pui.components.ButtonComponent;
import com.pi4j.jfx.util.Pi4JContext;

import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExamplePUITest {

    @Test
    void testLED() {
        //given
        ExampleModel      model      = new ExampleModel();
        ExampleController controller = new ExampleController(model);
        ExamplePUI        pui        = new ExamplePUI(controller, Pi4JContext.createMockContext());

        //when
        controller.setLedGlows(true);

        //then

        //todo: this should fail !!!
        pui.runLater(unused -> assertTrue(pui.led.glows()));

        //when
        controller.setLedGlows(false);

        //then
        pui.runLater(unused -> assertFalse(pui.led.glows()));
    }

    @Test
    void testButton() {
        //given
        ExampleModel      model      = new ExampleModel();
        ExampleController controller = new ExampleController(model);
        ExamplePUI        pui        = new ExamplePUI(controller, Pi4JContext.createMockContext());

        int initialCounter = model.counter.getValue();

        //when
        pui.button.dispatchSimpleEvents(ButtonComponent.ButtonState.UP);

        //then
        controller.runLater(exampleModel -> assertEquals(initialCounter - 1, exampleModel.counter.getValue()));
    }
}
