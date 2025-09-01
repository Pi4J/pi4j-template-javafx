package com.pi4j.mvc.util.mvcbase;

import java.util.Objects;

/**
 * Projector is the common interface for both, GUI and PUI.
 * <p>
 * See <a href="https://jaxenter.de/effiziente-oberflaechen-mit-dem-projektor-pattern-42119">Prof. Dierk Koenig's conference talk</a>
 */
interface Projector<M, C extends ControllerBase<M>> {

    /**
     * needs to be called inside the constructor of your UI-part
     */
	default void init(C controller) {
        Objects.requireNonNull(controller);
        initializeSelf();
        initializeComponents();
		setupEventHandler(controller);
		updateComponents(controller.getModel());
	}

    /**
     * Everything that needs to be done to initialize the UI-part itself.
     * <p>
     * For GUIs loading stylesheet-files or additional fonts are typical examples.
     */
    default void initializeSelf(){
    }

    /**
     * completely initialize all necessary UI-elements (like buttons, text-fields, etc. on GUI or distance sensors on PUI)
     */
    void initializeComponents();


    /**
     * Triggering some action on Controller if the user interacts with the UI.
     * <p>
     * There's no need to have access to the model for this task.
     * <p>
     * All EventHandlers will call a single method on the Controller.
     * <p>
     * If you are about to call more than one method, you should introduce a new method on the controller.
     */
	default void setupEventHandler(C controller) {
	}

    /**
     * Whenever an 'ObservableValue' in 'model' changes, the UI must be updated.
     * <p>
     * There's no need to have access to the controller for this task.
     * <p>
     * Register all necessary observers here.
     */
	default void updateComponents(M model) {
	}

    /**
     * At the Startup, this method gets called.
     * <p>
     * Perfect if a function in the controller or in the pui needs to be run exactly once.
     */
    default void startUp(C controller) {
        controller.startUp();
    }
}
