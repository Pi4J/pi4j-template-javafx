package com.pi4j.mvc.templatepuiapp.model;

import com.pi4j.mvc.util.mvcbase.ObservableValue;

public class SomeModel {
    public final ObservableValue<Integer> counter  = new ObservableValue<>(0);
    public final ObservableValue<Boolean> ledGlows = new ObservableValue<>(false);
}
