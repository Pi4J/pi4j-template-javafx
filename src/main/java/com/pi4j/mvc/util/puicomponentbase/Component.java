package com.pi4j.mvc.util.puicomponentbase;

import com.pi4j.mvc.util.puicomponentbase.events.EventHandler;
import com.pi4j.mvc.util.puicomponentbase.events.SimpleEventHandler;
import com.pi4j.mvc.util.puicomponentbase.helpers.Logger;

public abstract class Component {
    /**
     * Logger instance
     */
    protected final Logger logger = new Logger(this.getClass());

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be catched and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Utility function to trigger a simple event handler.
     * If the handler is currently null it gets silently ignored.
     *
     * @param handler Event handler to call or null
     */
    protected void triggerSimpleEvent(SimpleEventHandler handler) {
        if (handler != null) {
            handler.handle();
        }
    }

    protected <V> void triggerEvent(EventHandler<V> handler, V newValue){
        if(handler != null){
            handler.handle(newValue);
        }
    }
}
