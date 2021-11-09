package com.pi4j.jfx.mvcapp.view.gui.util;

import java.util.List;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;

import com.pi4j.jfx.mvcapp.model.util.ControllerBase;
import com.pi4j.jfx.mvcapp.model.util.ObservableValue;
import com.pi4j.jfx.mvcapp.model.util.ValueChangeListener;

/**
 * Use this interface for all of your UI-parts to assure implementation consistency
 */
public interface ViewMixin<M, T extends ControllerBase<M>>  {

    /**
     * needs to be called inside the constructor of your UI-part
     */
	default void init(T controller) {
        initializeSelf();
        initializeParts();
		layoutParts();
		setupEventHandlers(controller);
		setupGUIUpdates(controller.getModel());
	}

    /**
     * everything that needs to be done to initialize the ui-part itself
     *
     * Loading stylesheet-files or additional fonts are typical examples
     */
    default void initializeSelf(){
    }

    /**
     * completely initialize all necessary ui-elements (so called 'controls', like buttons, text-fields, labels etc.)
     */
    void initializeParts();

    /**
     * the method name says it all
     */
	void layoutParts();

    /**
     * Triggering some action if the user interacts with the GUI is done via EventHandlers.
     *
     * All EventHandlers will call methods on the Controller.
     */
	default void setupEventHandlers(T controller) {
	}

    /**
     * Whenever an 'ObservableValue' in 'model' changes, the GUI must be updated.
     *
     * Register all necessary observers here
     */
	default void setupGUIUpdates(M model) {
	}

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
    class Converter<T>{
        private final ObservableValue<T> observableValue;

        Converter(ObservableValue<T> observableValue) {
            this.observableValue = observableValue;
        }

        /**
         * Second (optional) step for registering an observer to specify a converter-function
         *
         * @param converter the function converting the type of 'ObservableValue' into the type of the 'Property'
         *
         * @return an Updater to specify the 'GUI-Property' that needs to be updated if 'ObservableValue' changes
         */
        public <R> Updater<T, R> convertedBy(Function<T, R> converter){
            return new Updater<>(observableValue, converter);
        }

        /**
         * Registers an observer without any type conversion that will keep property-value and observableValue in sync.
         *
         * @param property GUI-Property that will be updated when observableValue changes
         */
        public void update(Property<? super T> property) {
            execute((oldValue, newValue) -> property.setValue(newValue));
        }

        /**
         * Registers an observer.
         *
         * @param listener whatever needs to be done on GUI when observableValue changes
         */
        public void execute(ValueChangeListener<T> listener){
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> listener.update(oldValue, newValue)));
        }

    }

    class Updater<T, R> {
        private final ObservableValue<T> observableValue;
        private final Function<T, R>     converter;

        Updater(ObservableValue<T> observableValue, Function<T, R> converter){
            this.observableValue = observableValue;
            this.converter = converter;
        }

        /**
         * Registers an observer that will keep observableValue and GUI-Property in sync by applying the specified converter
         *
         * @param property GUI-Property that will be updated when observableValue changes
         */
        public void update(Property<R> property){
            observableValue.onChange((oldValue, newValue) -> {
                R convertedValue = converter.apply(newValue);
                Platform.runLater(() -> property.setValue(convertedValue));
            });
         }
    }
}
