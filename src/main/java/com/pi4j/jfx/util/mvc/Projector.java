package com.pi4j.jfx.util.mvc;

/**
 * Projector is the common interface for both, GUI and PUI.
 *
 * See description of Projector Pattern: todo: add link
 */
public interface Projector<M, C extends ControllerBase<M>> {

    /**
     * needs to be called inside the constructor of your UI-part
     */
	default void init(C controller) {
        initializeSelf();
        initializeParts();
		setupUiToActionBindings(controller);
		setupModelToUiBindings(controller.getModel());
	}

    /**
     * Everything that needs to be done to initialize the ui-part itself.
     *
     * Loading stylesheet-files or additional fonts are typical examples.
     */
    default void initializeSelf(){
    }

    /**
     * completely initialize all necessary UI-elements (like buttons, text-fields, etc. on GUI )
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
}