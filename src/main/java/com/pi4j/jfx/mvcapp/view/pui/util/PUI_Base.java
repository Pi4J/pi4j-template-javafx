package com.pi4j.jfx.mvcapp.view.pui.util;

import com.pi4j.context.Context;

import com.pi4j.jfx.mvcapp.model.util.ControllerBase;
import com.pi4j.jfx.mvcapp.model.util.ObservableValue;
import com.pi4j.jfx.mvcapp.model.util.ValueChangeListener;

/**
 * Base class for all PUIs.
 */
public abstract class PUI_Base<M, C extends ControllerBase<M>> {

    public PUI_Base(C controller, Context pi4J) {
        initializeComponents(pi4J);
        setupInputEvents(controller);
        setupPUIUpdates(controller.getModel());
    }

    /**
     * Initialize all components used in your PUI
     *
     * @param pi4J gives access to RaspPi's GPIO
     */
    protected abstract void initializeComponents(Context pi4J);

    /**
     * Override this method to specify all the reactions on user (or sensor) inputs
     *
     * Use withModel in your EventHandler to assure that model is updated on UI thread
     *
     */
    protected  void setupInputEvents(C controller){
    }

    /**
     * Override this method to specify all necessary updates of your hardware components when the model state has changed
     *
     * @param model the presentation model managing the whole application state
     */
    protected void setupPUIUpdates(M model) {
    }

    protected <T> Updater<T> onChangeOf(ObservableValue<T> observableValue){
        return new Updater<>(observableValue);
    }

    public static class Updater<T> {
        private final ObservableValue<T> observableValue;

        Updater(ObservableValue<T> observableValue){
            this.observableValue = observableValue;
        }

        public void triggerPUIAction(ValueChangeListener<T> action){
            observableValue.onChange((oldValue, newValue) -> {
                action.update(oldValue, newValue);
            });
         }
    }

}

