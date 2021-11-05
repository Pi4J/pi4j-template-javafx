package com.pi4j.jfx.exampleapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ExamplePMTest {
    @Test
    void testInitialization(){
        //when
        ExamplePM pm = new ExamplePM();

        //then
        assertFalse(pm.isLedOn());
    }

    @Test
    void testCounter(){
        //given
        ExamplePM pm = new ExamplePM();
        int initialCount = pm.getCounter();

        //when
        pm.increaseCounter();

        //then
        assertEquals(initialCount + 1, pm.getCounter());

        //when
        pm.decreaseCounter();

        //then
        assertEquals(initialCount, pm.getCounter());
    }

}