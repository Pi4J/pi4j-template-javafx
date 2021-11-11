package com.pi4j.jfx.exampleapp.view.pui;

import java.time.Duration;

import javafx.application.Platform;

import com.pi4j.jfx.exampleapp.model.ExamplePM;
import com.pi4j.jfx.exampleapp.view.pui.components.ButtonComponent;
import com.pi4j.jfx.util.Pi4JContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
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

        //then

        //wait until modification is done on ui-thread
        int expected = counter - 1;

        await().atMost(Duration.ofMillis(500)).until(() -> pm.getCounter() == expected);
        assertEquals(expected, pm.getCounter());
    }
}