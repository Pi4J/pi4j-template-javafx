package com.pi4j.mvc.templateapp.view.pui;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.templateapp.view.pui.components.ButtonComponent;
import com.pi4j.mvc.util.Pi4JContext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SomePUITest {
    @Test
     void testLED() {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
 
         //when
         controller.setLedGlows(true);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertTrue(pui.led.glows());
 
         //when
         controller.setLedGlows(false);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertFalse(pui.led.glows());
     }
 
     @Test
     void testButton() {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
 
         int initialCounter = model.counter.getValue();
 
         //when
         pui.button.dispatchSimpleEvents(ButtonComponent.ButtonState.UP);
         pui.awaitCompletion();
         controller.awaitCompletion();
 
         //then
         assertEquals(initialCounter - 1, model.counter.getValue());
     }

}