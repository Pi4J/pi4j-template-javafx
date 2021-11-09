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
     * completely initialize all the necessary ui-elements (so called controls, like buttons, text-fields, labels etc.)
     */
    void initializeParts();

    /**
     * the method name says it all
     */
	void layoutParts();

    /**
     * Triggering some action if the user interacts with the GUI is done via EventHandlers.
     *
     * All EventHandlers will call methods on the Presentation-Model.
     */
	default void setupEventHandlers(T controller) {
	}


    /**
     * Bindings are a very convenient way to react on property-changes.
     *
     * All bindings are implemented in this method.
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

    default <T> Converter<T> onChangeOf(ObservableValue<T> observableValue){
        return new Converter<>(observableValue);
    }

    class Converter<T>{
        private final ObservableValue<T> observableValue;

        Converter(ObservableValue<T> observableValue) {
            this.observableValue = observableValue;
        }

        public <R> Updater<T, R> convertedBy(Function<T, R> converter){
            return new Updater<>(observableValue, converter);
        }

        public void update(StringProperty property) {
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> property.setValue((String) newValue)));
        }

        public void update(DoubleProperty property) {
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> property.setValue((Double) newValue)));
        }

        public void update(IntegerProperty property) {
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> property.setValue((Integer) newValue)));
        }

        public void update(BooleanProperty property) {
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> property.setValue((Boolean) newValue)));
        }

        public void execute(ValueChangeListener<T> updater){
            observableValue.onChange((oldValue, newValue) -> Platform.runLater(() -> updater.update(oldValue, newValue)));
        }

    }

    class Updater<T, R> {
        private final ObservableValue<T> observableValue;
        private final Function<T, R>     converter;

        Updater(ObservableValue<T> observableValue, Function<T, R> converter){
            this.observableValue = observableValue;
            this.converter = converter;
        }

        public void update(Property<R> property){
            observableValue.onChange((oldValue, newValue) -> {
                R convertedValue = converter.apply(newValue);
                Platform.runLater(() -> property.setValue(convertedValue));
            });
         }
    }
}
