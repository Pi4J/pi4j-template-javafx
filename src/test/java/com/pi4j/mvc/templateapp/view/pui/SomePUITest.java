package com.pi4j.mvc.templateapp.view.pui;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.Pi4JContext;

import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import org.junit.jupiter.api.Test;
import com.pi4j.catalog.ComponentTest;

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
         assertTrue(pui.led.getDigitalOutput().isOn());
 
         //when
         controller.setIsActive(false);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertFalse(pui.led.getDigitalOutput().isOn());
     }
 
     @Test
     void testButton() throws InterruptedException {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller, Pi4JContext.createMockContext());
         MockDigitalInput mockDigitalInput = (MockDigitalInput) pui.button.getDigitalInput();

         int initialCounter = model.counter.getValue();

         //when
         mockDigitalInput.mockState(DigitalState.HIGH);
         mockDigitalInput.mockState(DigitalState.LOW);
         controller.awaitCompletion();

         //then
         assertEquals(initialCounter - 1, model.counter.getValue());
     }

}