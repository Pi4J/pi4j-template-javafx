package com.pi4j.jfx.util.mvc;

/**
 * Base class for all Controllers.
 *
 * The whole application logic is located in controller classes.
 *
 * Controller classes work on and manage the 'model'. Models encapsulate the whole application state.
 */
public abstract class ControllerBase<T> {
    // the model managed by this Controller. Only subclasses have direct access
    protected final T model;

    /**
     * Controller needs a model.
     *
     * @param model model managed by this Controller
     */
    protected ControllerBase(T model){
        this.model = model;
    }

    /**
     * Only the other base classes 'ViewMixin' and 'PUI_Base' need access, therefore it's package private
     *
     * @return the model
     */
    T getModel() {
        return model;
    }

    /**
     * Even for setting  a value the controller is responsible
     *
     * @param observableValue the ObservableValue that gets a new value
     * @param newValue the new value
     */
    protected <V> void setValue(ObservableValue<V> observableValue, V newValue){
        observableValue.setValue(newValue);
    }

    /**
     * Convenience method to toggle a ObservableValue<Boolean>
     *
     * @param observableValue the ObservableValue that gets a new value
     */
    protected void toggle(ObservableValue<Boolean> observableValue){
        observableValue.setValue(!observableValue.getValue());
    }
}
