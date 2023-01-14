package com.pi4j.mvc.util.mvcbase;

import java.util.Objects;

/**
 * Projector is the common interface for both, GUI and PUI.
 *
 * See Dierk Koenig's conference talk: https://jaxenter.de/effiziente-oberflaechen-mit-dem-projektor-pattern-42119
 */
interface Projector<M, C extends ControllerBase<M>> {

    /**
     * needs to be called inside the constructor of your UI-part
     */
	default void init(C controller) {
        Objects.requireNonNull(controller);
        initializeSelf();
        initializeParts();
		setupUiToActionBindings(controller);
		setupModelToUiBindings(controller.getModel());
	}

    /**
     * Everything that needs to be done to initialize the UI-part itself.
     *
     * For GUIs loading stylesheet-files or additional fonts are typical examples.
     */
    default void initializeSelf(){
    }

    /**
     * completely initialize all necessary UI-elements (like buttons, text-fields, etc. on GUI or distance sensors on PUI )
     */
    void initializeParts();


    /**
     * Triggering some action on Controller if the user interacts with the UI.
     *
     * There's no need to have access to model for this task.
     *
     * All EventHandlers will call a single method on the Controller.
     *
     * If you are about to call more than one method, you should introduce a new method on Controller.
     */
	default void setupUiToActionBindings(C controller) {
	}

    /**
     * Whenever an 'ObservableValue' in 'model' changes, the UI must be updated.
     *
     * There's no need to have access to controller for this task.
     *
     * Register all necessary observers here.
     */
	default void setupModelToUiBindings(M model) {
	}

    /**
     * At the Startup, this method gets called.
     *
     * Perfect, if a function in the controller or in the pui needs to be run exactly once.
     */
    default void startUp(C controller) {
        controller.startUp();
    }
}
