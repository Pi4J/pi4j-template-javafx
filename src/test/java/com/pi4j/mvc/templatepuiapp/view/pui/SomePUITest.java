package com.pi4j.mvc.templatepuiapp.view.pui;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.util.Pi4JContext;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
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
         controller.ledOn();
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertTrue(pui.led.getDigitalOutput().isOn());
 
         //when
         controller.ledOff();
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertFalse(pui.led.getDigitalOutput().isOn());
     }
 
     @Test
     void testButton() {
         //given
         SomeModel      model              = new SomeModel();
         SomeController controller         = new SomeController(model);
         SomePUI pui                       = new SomePUI(controller, Pi4JContext.createMockContext());
         MockDigitalInput mockDigitalInput = (MockDigitalInput) pui.button.getDigitalInput();

         int initialCounter = model.counter.getValue();

         //when
         mockDigitalInput.mockState(DigitalState.HIGH);
         controller.awaitCompletion();
         mockDigitalInput.mockState(DigitalState.LOW);
         controller.awaitCompletion();

         //then
         assertEquals(initialCounter + 1, model.counter.getValue());
     }

}