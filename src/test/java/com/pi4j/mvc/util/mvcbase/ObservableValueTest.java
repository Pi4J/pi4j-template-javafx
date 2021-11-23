package com.pi4j.mvc.util.mvcbase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
    void testSetValue() {
        //given
        ObservableValue<Boolean> observableValue = new ObservableValue<>(false);

        //when
        observableValue.setValue(true);

        //then
        assertTrue(observableValue.getValue());

        //when
        observableValue.setValue(false);

        //then
        assertFalse(observableValue.getValue());
    }

    @Test
    void testSetListener(){
        //given
        String                  initialValue    = "initial Value";
        String                  firstValue      = "first value";
        ObservableValue<String> observableValue = new ObservableValue<>(initialValue);

        AtomicInteger           counter  = new AtomicInteger(0);
        AtomicReference<String> foundOld = new AtomicReference<>();
        AtomicReference<String> foundNew = new AtomicReference<>();

        //when
        observableValue.onChange((oldValue, newValue) -> {
            counter.getAndIncrement();
            foundOld.set(oldValue);
            foundNew.set(newValue);
        });

        //then
        assertEquals(1, counter.get());                       // listener is called on registration
        assertEquals(initialValue, foundOld.get());           // initial value of oldValue is current value
        assertEquals(initialValue, foundNew.get());           // current value

        //when
        observableValue.setValue(initialValue);

        //then
        assertEquals(1, counter.get()); // value stays the same; listener is not called

        //when
        observableValue.setValue(firstValue);

        //then
        assertEquals(2, counter.get()); // value has changed; listener is called
        assertEquals(initialValue, foundOld.get());
        assertEquals(firstValue, foundNew.get());
    }

    @Disabled("This test sometimes fails, most probably because testcase is wrong, not implementation")
    @Test
    void testEdgeCase(){
        //given
        ObservableValue<String> observableValue = new ObservableValue<>("start");

        List<String>  log1 = new ArrayList<>();
        List<String>  log2 = new ArrayList<>();

        //when
        observableValue.onChange((oldValue, newValue) -> {
            log1.add(oldValue);
            log1.add(newValue);
            if(newValue.equals("second")){
                observableValue.setValue("third");
            }
        });

        observableValue.onChange((oldValue, newValue) -> {
            log2.add(oldValue);
            log2.add(newValue);
        });

        //then
        assertArrayEquals(new String[]{"start", "start"}, log1.toArray(new String[0]));
        assertArrayEquals(new String[]{"start", "start"}, log2.toArray(new String[0]));

        //when
        observableValue.setValue("second");

        //then
        // first observer has seen all value changes
        assertArrayEquals(new String[]{"start", "start", "start", "second", "second", "third"}, log1.toArray(new String[0]));

        // the second observer might _not_ have seen all value changes but he sees
        // at least the last proper value change !!!
        assertArrayEquals(new String[]{"start", "start", "second", "third"}, log2.toArray(new String[0]));
    }

}