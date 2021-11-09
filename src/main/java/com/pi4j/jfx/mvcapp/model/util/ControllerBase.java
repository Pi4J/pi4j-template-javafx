package com.pi4j.jfx.mvcapp.model.util;


public abstract class ControllerBase<T> {
    protected final T model;

    protected ControllerBase(T model){
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    protected <V> void  setValue(ObservableValue<V> observableValue, V newValue){
        observableValue.setValue(newValue);
    }

    protected void toggle(ObservableValue<Boolean> observableValue){
        observableValue.setValue(!observableValue.getValue());
    }
}
