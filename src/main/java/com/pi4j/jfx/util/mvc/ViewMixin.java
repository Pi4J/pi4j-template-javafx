package com.pi4j.jfx.util.mvc;

import java.util.List;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.scene.text.Font;

/**
 * Use this interface for all of your GUI-parts to assure implementation consistency.
 *
 * It also provides the basic functionality to make MVC run.
 */
public interface ViewMixin<M,  C extends ControllerBase<M>> extends Projector<M, C> {

    @Override
    default void init(C controller) {
        Projector.super.init(controller);
        layoutParts();
    }

    /**
     * the method name says it all
     */
	void layoutParts();

    /**
     * just a convenience method to load stylesheet files
     *
     * @param stylesheetFiles name of the stylesheet file
     */
    default void addStylesheetFiles(String... stylesheetFiles){
        for(String file : stylesheetFiles){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    /**
     * just a convenience method to load additional fonts
     * @param fonts name of the fonts file
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
     *
     *
     */
    class Converter<V>{
        private final ObservableValue<V> observableValue;

        Converter(ObservableValue<V> observableValue) {
            this.observableValue = observableValue;
        }

        /**
         * Second (optional) step for registering an observer to specify a converter-function
         *
         * @param converter the function converting the type of 'ObservableValue' into the type of the 'Property'
         *
         * @return an Updater to specify the 'GUI-Property' that needs to be updated if 'ObservableValue' changes
         */
        public <R> Updater<V, R> convertedBy(Function<V, R> converter){
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
        public void execute(ValueChangeListener<V> listener){
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> listener.update(oldValue, newValue)));
        }
    }

    class Updater<V, P> {
        private final ObservableValue<V> observableValue;
        private final Function<V, P>     converter;

        Updater(ObservableValue<V> observableValue, Function<V, P> converter){
            this.observableValue = observableValue;
            this.converter       = converter;
        }

        /**
         * Registers an observer that will keep observableValue and GUI-Property in sync by applying the specified converter.
         *
         * @param property GUI-Property that will be updated when observableValue changes
         */
        public void update(Property<? super P> property){
            observableValue.onChange((oldValue, newValue) -> {
                P convertedValue = converter.apply(newValue);
                Platform.runLater(() -> property.setValue(convertedValue));
            });
         }
    }
}
