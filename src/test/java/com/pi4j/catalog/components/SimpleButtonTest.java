package com.pi4j.catalog.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

import com.pi4j.catalog.ComponentTest;
import com.pi4j.catalog.components.base.PIN;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleButtonTest extends ComponentTest {

    private SimpleButton button;
    private MockDigitalInput digitalInput;
    private final PIN pinNumber = PIN.D26;

    @BeforeEach
    public void setUp() {
        button = new SimpleButton(pi4j, pinNumber, false);
        digitalInput = button.mock();
    }

    @Test
    public void testButtonState() {
        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertTrue(button.isDown());
        assertFalse(button.isUp());

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertTrue(button.isUp());
        assertFalse(button.isDown());
    }

    @Test
    public void testButtonStateOfInvertedButton(){
        //given
        SimpleButton invertedButton = new SimpleButton(pi4j, PIN.D21, true);
        digitalInput = invertedButton.mock();

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertTrue(invertedButton.isDown());
        assertFalse(invertedButton.isUp());

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertFalse(invertedButton.isDown());
        assertTrue(invertedButton.isUp());
    }

    @Test
    public void testPinNumber() {
        assertEquals(pinNumber.getPin(), button.pinNumber());
    }

    @Test
    public void testOnDown() {
        //given
        Counter counter = new Counter();

        digitalInput.mockState(DigitalState.LOW);

        button.onDown(() -> counter.increase());

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter.count);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter.count);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(1, counter.count);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(2, counter.count);

        //when
        button.reset();

        //then
        assertTrue(button.isInInitialState());
    }

    @Test
    public void testOnUp() {
        //given
        Counter counter = new Counter();

        digitalInput.mockState(DigitalState.LOW);

        // counter should be increased whenever button gets depressed
        // or, in other words, whenever the state of GPIO-Pin switches from HIGH to LOW

        button.onUp(() -> counter.increase());

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(0, counter.count);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(1, counter.count);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(1, counter.count);

        //when
        digitalInput.mockState(DigitalState.LOW);

        //then
        assertEquals(2, counter.count);
    }

    @Test
    public void testWhilePressed() throws InterruptedException {
        //given
        Duration samplingTime =  Duration.ofMillis(100);

        Counter counter = new Counter();

        button.whilePressed(counter::increase, samplingTime);

        //when
        digitalInput.mockState(DigitalState.HIGH);

        //then
        assertEquals(0, counter.count);

        //when
        sleep(2 * samplingTime.toMillis());

        //stop whilePressed
        digitalInput.mockState(DigitalState.LOW);

        //then
        int currentCount = counter.count;
        assertTrue(currentCount <= 2);

        //when
        sleep(2 * samplingTime.toMillis());

        //then
        assertEquals(currentCount, counter.count);
    }

    @Test
    public void testDeRegisterAll(){
        //given
        Runnable task = () -> System.out.println("not important for test");
        button.onUp(task);
        button.onDown(task);
        button.whilePressed(task, Duration.ofMillis(10));

        //when
        button.reset();

        //then
        assertTrue(button.isInInitialState());
    }

    private class Counter {
        int count;

        void increase(){
            count++;
        }
    }
}
