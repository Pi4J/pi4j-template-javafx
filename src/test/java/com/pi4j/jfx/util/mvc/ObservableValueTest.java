package com.pi4j.jfx.util.mvc;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ObservableValueTest {

    @Test
    void testInitialization() {
        //when
        ObservableValue<Boolean> v = new ObservableValue<>(false);

        //then
        assertFalse(v.getValue());

        //when
        v = new ObservableValue<>(true);

        //then
        assertTrue(v.getValue());
    }

    @Test
    void testSetValue(){
        //given
        ObservableValue<Boolean> v = new ObservableValue<>(false);

        AtomicInteger counter = new AtomicInteger(0);

        //when
        v.onChange((oldValue, newValue) -> counter.getAndIncrement());

        //then
        assertEquals(1, counter.get());   // listener is call on registration

        //when
        v.setValue(false);

        //then
        assertEquals(1, counter.get()); // value stays the same; listener is not called

        //when
        v.setValue(true);
        assertEquals(2, counter.get()); // value has changed; listener is called
    }

}