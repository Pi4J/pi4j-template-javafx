package com.pi4j.jfx.util.mvc;


import com.pi4j.context.Context;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base class for all PUIs.
 *
 * In our scenario we also have a GUI.
 *
 * We have to avoid that one of the UIs is blocked because the other UI has to perform a long-running task.
 *
 * Therefore, we need an additional "worker-thread" in both UIs.
 *
 * For JavaFX-based GUIs that's already implemented.
 *
 * For PUIs we need to do that ourselves. It's implemented as a provider/consumer-pattern (see {@link ConcurrentTaskQueue}.
 */
public abstract class PUI_Base<M, C extends ControllerBase<M>> implements Projector<M, C>{

    // all PUI actions should be done asynchronously (to avoid GUI freezing)
    private final ConcurrentTaskQueue<Void> queue = new ConcurrentTaskQueue<>();

    protected final Context pi4J;

    public PUI_Base(C controller, Context pi4J) {
        this.pi4J = pi4J;

        init(controller);
    }

    public void shutdown(){
        queue.shutdown();
    }

    protected void async(Supplier<Void> todo, Consumer<Void> onDone) {
        queue.submit(todo, onDone);
    }

    public void runLater(Consumer<Void> todo) {
        async(() -> null, todo);
    }

    /**
     * First step to register an observer.
     *
     * @param observableValue the value that should trigger some PUI-updates
     * @return an Updater to specify what needs to be done whenever observableValue changes
     */
    protected <V> Updater<V> onChangeOf(ObservableValue<V> observableValue) {
        return new Updater<>(observableValue);
    }

    /**
     * Second step to specify an observer.
     *
     * Use 'triggerPUIAction' to specify what needs to be done whenever the observed value changes
     */
    public class Updater<V> {
        private final ObservableValue<V> observableValue;

        Updater(ObservableValue<V> observableValue) {
            this.observableValue = observableValue;
        }

        public void triggerPUIAction(ValueChangeListener<V> action) {
            observableValue.onChange((oldValue, newValue) -> queue.submit(() -> {
                action.update(oldValue, newValue);
                return null;
            }));
        }
    }
}


