package com.pi4j.mvc.templateapp.view.pui;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;

import com.pi4j.catalog.ComponentTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SomePUITest extends ComponentTest {
    @Test
     void testLED() {
         //given
         SomeModel      model      = new SomeModel();
         SomeController controller = new SomeController(model);
         SomePUI        pui        = new SomePUI(controller);
 
         //when
         controller.setIsActive(true);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertTrue(pui.led.isOn());
 
         //when
         controller.setIsActive(false);
         controller.awaitCompletion();
         pui.awaitCompletion();
 
         //then
         assertFalse(pui.led.isOn());
     }


}