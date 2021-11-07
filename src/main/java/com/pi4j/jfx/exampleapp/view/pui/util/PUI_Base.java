package com.pi4j.jfx.exampleapp.view.pui.util;

import java.util.function.Consumer;

import javafx.application.Platform;

import com.pi4j.context.Context;

/**
 * Base class for all PUIs.
 */
public abstract class PUI_Base<T> {
    private final T model;

    public PUI_Base(T model, Context pi4J) {
        this.model = model;
        initializeComponents(model, pi4J);
        setupInputEvents();
        setupModelChangeListeners(model);
    }

    /**
     * Initialize all components used in your PUI
     *
     * @param model the presentation model the PUI is working on
     * @param pi4J gives access to RaspPi's GPIO
     */
    protected abstract void initializeComponents(T model, Context pi4J);

    /**
     * model updates must be executed on UI-Thread otherwise the GUI can't be updated properly.
     *
     * @param action the necessary modifications on model
     */
    protected void withModel(Consumer<T> action) {
         Platform.runLater(() -> action.accept(model));
    }


    /**
     * Override this method to specify all the reactions on user (or sensor) inputs
     *
     * Use withModel in your EventHandler to assure that model is updated on UI thread
     *
     */
    protected  void setupInputEvents(){
    }


    /**
     * Override this method to specify all necessary updates of your hardware components when the model state has changed
     *
     * @param model the presentation model managing the whole application state
     */
    protected void setupModelChangeListeners(T model) {
    }

}

