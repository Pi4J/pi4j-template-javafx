package com.pi4j.jfx.templatepuiapp.model;

import com.pi4j.jfx.util.mvc.ObservableValue;

public class SomeModel {
    public final ObservableValue<Integer> counter  = new ObservableValue<>(0);
    public final ObservableValue<Boolean> ledGlows = new ObservableValue<>(false);
}
