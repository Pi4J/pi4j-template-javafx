package com.pi4j.mvc.multicontrollerapp.view.pui.components;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.mvc.util.piucomponentbase.ComponentTest;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ButtonComponentTest extends ComponentTest {

    private ButtonComponent  button;
    private MockDigitalInput din;

    @BeforeEach
    void setUp() {
        button = new ButtonComponent(pi4j, 26, true, 10_000);
        din = toMock(button.getDigitalInput());
    }

    @ParameterizedTest
    @CsvSource({
        "UNKNOWN, UNKNOWN",
        "LOW,     DOWN",
        "HIGH,    UP"
    })
    void testMapDigitalState(DigitalState digitalState, ButtonComponent.ButtonState expected) {
        // when
        final var actual = button.mapDigitalState(digitalState);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void testOnUp(){
        //given
        int[] counter = {0};

        din.mockState(DigitalState.LOW);

        button.onUp(() -> counter[0]++);

        //when
        din.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter[0]);

        //when
        din.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter[0]);

        //when
        din.mockState(DigitalState.LOW);

        //then
        assertEquals(1, counter[0]);

        //when
        din.mockState(DigitalState.HIGH);

        //then
        assertEquals(2, counter[0]);
    }
}