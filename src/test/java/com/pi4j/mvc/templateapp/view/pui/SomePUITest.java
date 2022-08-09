package com.pi4j.mvc.templateapp.view.pui;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.Pi4JContext;

import org.junit.jupiter.api.Test;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

import com.pi4j.components.ComponentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SomePUITest extends ComponentTest {
    @Test
     void testLED() {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
 
         //when
         controller.setIsActive(true);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertTrue(pui.led.glows());
 
         //when
         controller.setIsActive(false);
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

         MockDigitalInput digitalInput = toMock(pui.button.getDigitalInput());
         digitalInput.mockState(DigitalState.HIGH);
 
         //when
         digitalInput.mockState(DigitalState.LOW);
         pui.awaitCompletion();
         controller.awaitCompletion();
 
         //then
         assertEquals(initialCounter - 1, model.counter.getValue());
     }

}