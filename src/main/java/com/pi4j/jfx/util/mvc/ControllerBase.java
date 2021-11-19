package com.pi4j.jfx.util.mvc;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base class for all Controllers.
 *
 * The whole application logic is located in controller classes.
 *
 * Controller classes work on and manage the Model. Models encapsulate the whole application state.
 *
 * Controllers provide the whole core functionality of the application, so called 'Actions'
 *
 * Execution of Actions is asynchronous. The sequence is kept stable, such that
 * for all actions A and B: if B is submitted after A, B will only be executed after A is finished.
 */
public abstract class ControllerBase<M> {

    private ConcurrentTaskQueue<M> actionQueue;

    // the model managed by this Controller. Only subclasses have direct access
    protected final M model;

    /**
     * Controller needs a Model.
     *
     * @param model Model managed by this Controller
     */
    protected ControllerBase(M model){
        this.model = model;
    }

    public void shutdown(){
        if(null != actionQueue){
            actionQueue.shutdown();
            actionQueue = null;
        }
    }

    /**
     * Schedule the given action for execution in strict order in external thread, asynchronously.
     */
    protected void async(Supplier<M> todo, Consumer<M> onDone) {
        if(null == actionQueue){
            actionQueue = new ConcurrentTaskQueue<>();
        }
        actionQueue.submit(todo, onDone);
    }


    protected void async(Runnable todo){
        async(() -> {
                todo.run();
                return model;
            },
            m -> {});
    }


    public void runLater(Consumer<M> todo) {
        async(() -> model, todo);
    }

    /**
     * Only the other base classes 'ViewMixin' and 'PUI_Base' need access, therefore it's package private
     *
     * @return the model
     */
    M getModel() {
        return model;
    }

    /**
     * Even for setting a value the controller is responsible.
     *
     * @param observableValue the ObservableValue that gets a new value
     * @param newValue the new value
     */
    protected <V> void setValue(ObservableValue<V> observableValue, V newValue){
        async(() -> observableValue.setValue(newValue));
    }

    /**
     * Convenience method to toggle a ObservableValue<Boolean>
     *
     * @param observableValue the ObservableValue that gets a new value
     */
    protected void toggle(ObservableValue<Boolean> observableValue){
        setValue(observableValue, !observableValue.getValue());
    }

    /**
     * Utility function to pause execution of actions for the specified amount of time.
     *
     * An {@link InterruptedException} will be catched and ignored while setting the interrupt flag again.
     *
     * @param duration Time  to sleep
     */
    protected void pauseExecution(Duration duration) {
        async(() -> {
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
