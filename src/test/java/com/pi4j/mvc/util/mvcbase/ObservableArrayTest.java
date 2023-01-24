package com.pi4j.mvc.util.mvcbase;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;


class ObservableArrayTest {

    @Test
    void testInitialization() {
        //when
        ObservableArray<Boolean> v = new ObservableArray<>(new Boolean[]{false, false, false, false});

        //then
        assertFalse(v.getValue(0));

        //when
        v = new ObservableArray<>(new Boolean[]{true, false, false, false});

        //then
        assertTrue(v.getValue(0));
    }

    @Test
    void testSetValue() {
        //given
        ObservableArray<Boolean> observableValue = new ObservableArray<>(new Boolean[]{false, false, false, false});

        //when
        observableValue.setValue(0, true);

        //then
        assertTrue(observableValue.getValue(0));

        //when
        observableValue.setValue(0, false);

        //then
        assertFalse(observableValue.getValue(0));
    }

    @Test
    void testSetListener(){
        //given
        String initialValue    = "initial Value";
        String firstValue      = "first value";
        ObservableArray<String> observableArray = new ObservableArray<>(new String[]{initialValue});

        AtomicInteger           counter  = new AtomicInteger(0);
        AtomicReference<String> foundOld = new AtomicReference<>();
        AtomicReference<String> foundNew = new AtomicReference<>();

        //when
        observableArray.onChange((oldValue, newValue) -> {
            counter.getAndIncrement();
            foundOld.set(oldValue[0]);
            foundNew.set(newValue[0]);
        });

        //then
        assertEquals(1, counter.get());                       // listener is called on registration
        assertEquals(initialValue, foundOld.get());           // initial value of oldValue is current value
        assertEquals(initialValue, foundNew.get());           // current value

        //when
        observableArray.setValue(0, initialValue);

        //then
        assertEquals(1, counter.get()); // value stays the same; listener is not called

        //when
        observableArray.setValue(0, firstValue);

        //then
        assertEquals(2, counter.get()); // value has changed; listener is called
        assertEquals(initialValue, foundOld.get());
        assertEquals(firstValue, foundNew.get());
    }
}