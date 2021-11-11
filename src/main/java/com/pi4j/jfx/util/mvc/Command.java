package com.pi4j.jfx.util.mvc;


class Command<V> {
        final ValueChangeListener<V> listener;
        final V                      oldValue;
        final V                      newValue;

        Command(ValueChangeListener<V> listener, V oldValue, V newValue) {
            this.listener = listener;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        void execute() {
            listener.update(oldValue, newValue);
        }
    }