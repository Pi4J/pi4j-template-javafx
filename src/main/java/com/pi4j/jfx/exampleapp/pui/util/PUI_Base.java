package com.pi4j.jfx.exampleapp.pui.util;

import javafx.application.Platform;

import com.pi4j.context.Context;

/**
 * Base class for all PUIs.
 */
public abstract class PUI_Base<T> {
    public PUI_Base(T model, Context pi4J) {
        initializeComponents(model, pi4J);
        setupInputEvents(model);
        setupModelChangeListeners(model);
    }

    protected abstract void initializeComponents(T model, Context pi4J);

    /**
     * model updates must be executed on UI-Thread otherwise the GUI can't be updated properly.
     *
     * @param action the necessary modifications on model
     */
    protected void withModel(Runnable action) {
         Platform.runLater(() -> action.run());
    }




    /**
     * Override this method to specify all the reaction on user (or sensor) inputs
     *
     * Use withModel to assure that all updates are performed on ui thread
     *
     * @param model the presentation model managing the whole application state
     */
    protected  void setupInputEvents(T model){
    }


    /**
     * Override this method to specify all necessary updates of your hardware components when the model state has changed
     *
     * @param model the presentation model managing the whole application state
     */
    protected void setupModelChangeListeners(T model) {
    }

}

