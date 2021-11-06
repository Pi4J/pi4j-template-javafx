package com.pi4j.jfx.exampleapp.gui.util;

import java.util.List;

import javafx.scene.text.Font;

/**
 * Use this interface for all of your UI-parts to assure implementation consistency
 */
public interface ViewMixin {

    /**
     * needs to be called inside the constructor of your UI-part
     */
	default void init() {
        initializeSelf();
        initializeParts();
		layoutParts();
		setupEventHandlers();
		setupValueChangeListeners();
		setupBindings();
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
	default void setupEventHandlers() {
	}

    /**
     * If properties of the PM have changed, you have to update the UI.
     *
     * All the necessary listeners observing on of the PM-properties are implemented in this method
     */
	default void setupValueChangeListeners() {
	}

    /**
     * Bindings are a very convenient way to react on property-changes.
     *
     * All bindings are implemented in this method.
     */
	default void setupBindings() {
	}

    /**
     * just a convenience method to load stylesheet files
     *
     * @param stylesheetFile name of the stylesheet file
     */
    default void addStylesheetFiles(String... stylesheetFile){
        for(String file : stylesheetFile){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    /**
     * just a convenience method to load additional fonts
     * @param font name of the font file
     */
    default void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    List<String> getStylesheets();
}
