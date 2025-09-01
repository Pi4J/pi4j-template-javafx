package com.pi4j.mvc.util.mvcbase;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.scene.text.Font;

/**
 * Use this interface for all of your GUI parts to ensure implementation consistency.
 * <p>
 * It provides the basic functionality to make MVC run.
 */
public interface ViewMixin<M,  C extends ControllerBase<M>> extends Projector<M, C> {

    @Override
    default void init(C controller) {
        Projector.super.init(controller);
        layoutComponents();
    }

    /**
     * the method name says it all
     */
	void layoutComponents();

    /**
     * just a convenience method to load stylesheet files
     *
     * @param stylesheetFiles name of the stylesheet file
     */
    default void addStylesheetFiles(String... stylesheetFiles){
        for(String file : stylesheetFiles){
            String stylesheet = Objects.requireNonNull(getClass().getResource(file)).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    /**
     * just a convenience method to load additional fonts
     */
    default void loadFonts(String... fonts){
        for(String f : fonts){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    List<String> getStylesheets();

    /**
     * Starting point for registering an observer.
     *
     * @param observableValue the value that needs to be observed
     *
     * @return a 'Converter' to specify a function converting the type of 'ObservableValue' into the type of the 'Property'
     */
    default <V> Converter<V> onChangeOf(ObservableValue<V> observableValue){
        return new Converter<>(observableValue);
    }

    /**
     * A utility class that facilitates reacting to changes in an ObservableValue
     * and updating a Property directly or via a conversion function.
     *
     * @param <V> the type of the value held by the ObservableValue
     */
    record Converter<V>(ObservableValue<V> observableValue) {

        /**
         * Second (optional) step for registering an observer to specify a converter-function
         *
         * @param converter the function converting the type of 'ObservableValue' into the type of the 'Property'
         * @return an Updater to specify the 'GUI-Property' that needs to be updated if 'ObservableValue' changes
         */
        public <R> Updater<V, R> convertedBy(Function<V, R> converter) {
            return new Updater<>(observableValue, converter);
        }

        /**
         * Registers an observer without any type conversion that will keep property-value and observableValue in sync.
         *
         * @param property GUI-Property that will be updated when observableValue changes
         */
        public void update(Property<? super V> property) {
            execute((oldValue, newValue) -> property.setValue(newValue));
        }

        /**
         * Registers an observer.
         *
         * @param listener whatever needs to be done on GUI when observableValue changes
         */
        public void execute(ObservableValue.ValueChangeListener<V> listener) {
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> listener.update(oldValue, newValue)));
        }
    }

    record Updater<V, P>(ObservableValue<V> observableValue, Function<V, P> converter) {

        /**
         * Registers an observer that will keep observableValue and GUI-Property in sync by applying the specified converter.
         *
         * @param property GUI-Property that will be updated when observableValue changes
         */
        public void update(Property<? super P> property) {
            observableValue.onChange((oldValue, newValue) -> {
                P convertedValue = converter.apply(newValue);
                Platform.runLater(() -> property.setValue(convertedValue));
            });
        }
    }

    /**
     * Registers an observer to track changes of the specified {@code Property}.
     * When the property value changes, an {@code ActionTrigger} is returned to
     * define and execute actions based on these changes.
     *
     * @param <V> the type of the value contained within the {@code Property}
     * @param property the property to observe for value changes
     * @return an {@code ActionTrigger} allowing the definition of actions to execute on property value changes
     */
    default <V> ActionTrigger<V> onChangeOf(Property<V> property){
        return new ActionTrigger<>(property);
    }

    /**
     * Registers an observer for changes on the provided {@link DoubleProperty}.
     * When the property value changes, the associated action can be triggered.
     *
     * @param property the {@link DoubleProperty} to observe for changes
     * @return an {@link ActionTrigger} for specifying the action to execute on value change
     */
    default ActionTrigger<Double> onChangeOf(DoubleProperty property){
        return new ActionTrigger<>(property);
    }

    /**
     * Registers an action to be triggered whenever the given IntegerProperty changes.
     *
     * @param property the IntegerProperty to observe for changes
     * @return an ActionTrigger that allows specifying the action to be executed upon property changes
     */
    default ActionTrigger<Integer> onChangeOf(IntegerProperty property){
        return new ActionTrigger<>(property);
    }

    record ActionTrigger<V>(Property<? super V> property) {

        public void triggerAction(Consumer<V> action) {
            property.addListener((observableValue, oldValue, newValue) -> action.accept((V) newValue));
        }
    }
}
