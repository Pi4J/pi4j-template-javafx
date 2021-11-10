package com.pi4j.jfx.util.mvc;


import com.pi4j.context.Context;

/**
 * Base class for all PUIs.
 *
 * In our scenario we also have a GUI.
 *
 * We have to avoid that one of the UIs is blocked because the other UI has to perform a long running task.
 *
 * Therefore we need an additional "worker-thread" in both UIs.
 *
 * For JavaFX-based GUIs that's already implemented.
 *
 * For PUIs we need to do that ourselves. It's implemented as a provider/consumer-pattern.
 */
public abstract class PUI_Base<M, C extends ControllerBase<M>> {

    // all that needs to be done is encapsulated as a Command and queued up in this CommandQueue
    private final CommandQueue queue = new CommandQueue();

    public PUI_Base(C controller, Context pi4J) {
        initializeComponents(pi4J);
        setupInputEvents(controller);
        setupPUIUpdates(controller.getModel());

        queue.start();
    }

    public void shutdown(){
        queue.shutdown();
    }

    /**
     * Initialize all components used in your PUI
     *
     * @param pi4J gives access to RaspPi's GPIO
     */
    protected abstract void initializeComponents(Context pi4J);

    /**
     * Override this method to specify all the reactions on user (or sensor) inputs.
     *
     * There's no need to have access to model for this task.
     *
     * Every InputEvent should call a single method on controller.
     *
     * If you are about to call more than one method, you should introduce a new method on controller.
     */
    protected void setupInputEvents(C controller) {
    }

    /**
     * Override this method to specify all necessary updates of your hardware components whenever the model state changes.
     *
     * There's no need to have access to model for this task.
     *
     * @param model the model managing encapsulating the whole application state
     */
    protected void setupPUIUpdates(M model) {
    }

    /**
     * First step to register an observer.
     *
     * @param observableValue the value that should trigger some PUI-updates
     * @return an Updater to specify what needs to be done whenever observableValue changes
     */
    protected <T> Updater<T> onChangeOf(ObservableValue<T> observableValue) {
        return new Updater<>(observableValue);
    }

    /**
     * Second step to specify an observer.
     *
     * Use 'triggerPUIAction' to specify what needs to be done whenever the observed value changes
     */
    public class Updater<T> {
        private final ObservableValue<T> observableValue;

        Updater(ObservableValue<T> observableValue) {
            this.observableValue = observableValue;
        }

        public void triggerPUIAction(ValueChangeListener<T> action) {
            observableValue.onChange((oldValue, newValue) -> queue.queueEvent(new Command<>(action, oldValue, newValue)));
        }
    }

}


