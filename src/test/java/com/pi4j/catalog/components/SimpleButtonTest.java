package com.pi4j.catalog.components;

import com.pi4j.catalog.ComponentTest;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleButtonTest extends ComponentTest {

    private SimpleButton button;
    private MockDigitalInput digitalInput;
    private final PIN PinNumber = PIN.D26;

    @BeforeEach
    public void setUp() {
        button = new SimpleButton(pi4j, PinNumber, false);
        digitalInput = toMock(button.getDigitalInput());
    }

    @Test
    public void testGetState() {
        //when
        digitalInput.mockState(DigitalState.HIGH);
        //then
        assertEquals(DigitalState.HIGH, button.getState());
        //when
        digitalInput.mockState(DigitalState.LOW);
        //the
        assertEquals(DigitalState.LOW, button.getState());
        //when
        digitalInput.mockState(DigitalState.UNKNOWN);
        //then
        assertEquals(DigitalState.UNKNOWN, button.getState());
    }

    @Test
    public void testIsDown() {
        //when
        digitalInput.mockState(DigitalState.HIGH);
        //then
        assertTrue(button.isDown());
        //when
        digitalInput.mockState(DigitalState.LOW);
        //then
        assertFalse(button.isDown());
    }

    @Test
    public void testIsUp() {
        //when
        digitalInput.mockState(DigitalState.HIGH);
        //then
        assertFalse(button.isUp());
        //when
        digitalInput.mockState(DigitalState.LOW);
        //then
        assertTrue(button.isUp());
    }

    @Test
    public void testGetDigitalInput() {
        assertEquals(PinNumber.getPin(), button.getDigitalInput().address());
    }

    @Test
    public void testOnDown() {
        //given
        int[] counter = {0};
        digitalInput.mockState(DigitalState.LOW);
        button.onDown(() -> counter[0]++);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter[0]);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter[0]);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(1, counter[0]);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(2, counter[0]);

        //when
        button.deRegisterAll();

        //then
        assertNull(button.getOnDown());
        assertNull(button.getOnUp());
        assertNull(button.getWhilePressed());
    }

    @Test
    public void testOnUp() {
        //given
        int[] counter = {0};
        digitalInput.mockState(DigitalState.LOW);
        button.onUp(() -> counter[0]++);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(0, counter[0]);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(1, counter[0]);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter[0]);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(2, counter[0]);

        //when
        button.deRegisterAll();

        //then
        assertNull(button.getOnDown());
        assertNull(button.getOnUp());
        assertNull(button.getWhilePressed());

    }

    @Test
    public void testWhilePressed() throws InterruptedException {
        //given
        int samplingTime = 100;

        AtomicInteger counter = new AtomicInteger(0);

        button.whilePressed(counter::getAndIncrement, samplingTime);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(0, counter.get());

        //when
        sleep(150);

        //then
        assertEquals(1, counter.get());

        //when
        sleep(100);

        //then
        assertEquals(2, counter.get());

        //when
        button.deRegisterAll();
        sleep(100);

        //then
        assertEquals(2, counter.get());
        assertNull(button.getOnDown());
        assertNull(button.getOnUp());
        assertNull(button.getWhilePressed());

        //cleanup
        digitalInput.mockState(DigitalState.LOW);
    }
}
