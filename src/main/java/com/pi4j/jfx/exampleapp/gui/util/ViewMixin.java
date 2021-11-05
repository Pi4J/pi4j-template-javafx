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
		setupValueChangedListeners();
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
     * the method name says it all
     */
	default void setupEventHandlers() {
	}

    /**
     * the method name says it all
     */
	default void setupValueChangedListeners() {
	}

    /**
     * the method name says it all
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
