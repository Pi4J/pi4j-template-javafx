package com.pi4j.jfx.templateapp.view.pui;


import com.pi4j.jfx.templateapp.controller.SomeController;
import com.pi4j.jfx.templateapp.model.SomeModel;
import com.pi4j.jfx.templateapp.view.pui.components.ButtonComponent;
import com.pi4j.jfx.util.Pi4JContext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SomePUITest {
    @Test
     void testLED() {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
 
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
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
 
         int initialCounter = model.counter.getValue();
 
         //when
         pui.button.dispatchSimpleEvents(ButtonComponent.ButtonState.UP);
 
         //then
         controller.runLater(SomeModel -> assertEquals(initialCounter - 1, SomeModel.counter.getValue()));
     }

}