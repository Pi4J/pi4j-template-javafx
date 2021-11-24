package com.pi4j.mvc.util.puicomponentbase.events;

/**
 * Generic event provider to trigger a {@link SimpleEventHandler} once a value has flapped between two values for a couple times.
 * The threshold once an event should be dispatched as well as the max idle time can be configured.
 * Any instance should be registered in a component using {@link EventProvider#addListener(EventHandler)} to actually monitor value changes.
 *
 * @param <V> Type of value which gets passed to event handlers.
 */
public class FlappingEventProvider<V> implements EventHandler<V> {
    /**
     * Default maximum idle time between transitioning from value A to B or vice-versa.
     * Whenever this time is exceeded between value changes, the counter will be reset.
     */
    private static final int DEFAULT_MAX_IDLE_TIME_MS = 500;
    /**
     * Default threshold when handler should be fired once counter reaches this value.
     */
    private static final int DEFAULT_THRESHOLD = 5;

    /**
     * Simple event handler to call once threshold has been hit.
     */
    private SimpleEventHandler handler;
    /**
     * Current count of consecutive value changes.
     */
    private int                counter;
    /**
     * Threshold when handler should be fired once counter reaches this value.
     */
    private int threshold;

    /**
     * Value of previous value change
     */
    private V lastValue;
    /**
     * Time in milliseconds of last value change
     */
    private long lastChange;
    /**
     * Value A which gets tracked by this event provider
     */
    private final V valueA;
    /**
     * Value B which gets tracked by this event provider
     */
    private final V valueB;
    /**
     * Max idle time in milliseconds between a value transition.
     */
    private final long maxIdleTime;

    /**
     * Initializes a new {@link FlappingEventProvider} with the two given values.
     * A default maximum idle time between transitions will be used by this method.
     *
     * @param valueA First value to watch for transitions to second value
     * @param valueB Second value to watch for transitions to first value
     * @see FlappingEventProvider#FlappingEventProvider(long, Object, Object)
     */
    public FlappingEventProvider(V valueA, V valueB) {
        this(DEFAULT_MAX_IDLE_TIME_MS, valueA, valueB);
    }

    /**
     * Initializes a new {@link FlappingEventProvider} with the two given values.
     * Whenever the value changes from A to B or vice-versa, an internal counter gets increased.
     * Once a certain threshold has been reached, a user-specified handler will be called.
     *
     * @param maxIdleTime Maximum idle time between transitions in milliseconds before counter is reset
     * @param valueA      First value to watch for transitions to second value
     * @param valueB      Second value to watch for transitions to first value
     */
    public FlappingEventProvider(long maxIdleTime, V valueA, V valueB) {
        this.valueA = valueA;
        this.valueB = valueB;
        this.maxIdleTime = maxIdleTime;
        this.threshold = DEFAULT_THRESHOLD;
    }

    /**
     * Sets the threshold and event handler for this instance.
     * These changes will immediately apply and the counter does not get reset.
     *
     * @param threshold Threshold before event should be fired
     * @param handler   Event handler to call, null to disable
     */
    public synchronized void setOptions(int threshold, SimpleEventHandler handler) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Target count must be greater than 0");
        }

        this.handler = handler;
        this.threshold = threshold;
    }

    /**
     * Implementation of {@link EventHandler} which must be added as a listener for this event provider to work.
     * It will track all state changes between value A and B and automatically resets or fires the handler accordingly.
     *
     * @param value Event value
     */
    @Override
    public synchronized void handle(V value) {
        // Skip if no handler is defined
        if (handler == null) {
            return;
        }

        // Skip if value is equal to current one or not tracked
        if (value == lastValue || (value != valueA && value != valueB)) {
            return;
        }

        // Reset current count if last change is too long ago
        final var now = System.currentTimeMillis();
        if (counter != 0 && now - lastChange > maxIdleTime) {
            counter = 0;
            return;
        }

        // Increment counter and update state tracking
        counter++;
        lastValue = value;
        lastChange = now;

        // Trigger event if threshold was reached
        if (counter >= threshold) {
            handler.handle();
            counter = 0;
        }
    }
}
