package com.pi4j.jfx.exampleapp.pui;

import javafx.application.Platform;

import com.pi4j.jfx.exampleapp.model.ExamplePM;
import com.pi4j.jfx.exampleapp.pui.components.ButtonComponent;
import com.pi4j.jfx.util.Pi4JContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExamplePUITest {
    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {
            //nothing to do
        });
        Platform.setImplicitExit(false);
    }

    @Test
    void testLED() {
        //given
        ExamplePM pm   = new ExamplePM();
        ExamplePUI pui = new ExamplePUI(pm, Pi4JContext.createMockContext());

        //when
        pm.setLedGlows(true);

        //then
        assertTrue(pui.led.glows());

        //when
        pm.setLedGlows(false);

        //then
        assertFalse(pui.led.glows());
    }

    @Test
   void testButton() {
        //given
        ExamplePM pm   = new ExamplePM();
        ExamplePUI pui = new ExamplePUI(pm, Pi4JContext.createMockContext());

        int counter = pm.getCounter();

        //when
        pui.button.dispatchSimpleEvents(ButtonComponent.ButtonState.UP);

        sleep(100); // wait for model changes are done on UI-thread todo: Find a better solution

        //then
        assertEquals(counter - 1, pm.getCounter());
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}