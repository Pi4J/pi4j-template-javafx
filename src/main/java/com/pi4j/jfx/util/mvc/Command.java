package com.pi4j.jfx.util.mvc;


class Command<T> {
        final ValueChangeListener<T> listener;
        final T                      oldValue;
        final T                      newValue;

        Command(ValueChangeListener<T> listener, T oldValue, T newValue) {
            this.listener = listener;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        void execute() {
            listener.update(oldValue, newValue);
        }
    }