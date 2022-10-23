open module com.pi4j.mvc {
     // Pi4J Modules
    requires com.pi4j;
    requires com.pi4j.library.pigpio;
    requires com.pi4j.library.linuxfs;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.mock;
    requires com.pi4j.plugin.linuxfs;
    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    // for logging
    requires java.logging;

    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    // needed for Charts and TilesFX
    requires javafx.swing;
    // needed for TilesFX
    requires javafx.media;
    requires javafx.web;


    // 3rd Party
    // needed for Charts and TilesFX
    requires transitive eu.hansolo.jdktools;
    requires transitive eu.hansolo.toolbox;
    requires transitive eu.hansolo.toolboxfx;
    requires transitive eu.hansolo.fx.heatmap;
    requires transitive eu.hansolo.fx.countries;

    // needed for TilesFX
    requires transitive eu.hansolo.tilesfx;


    // Module Exports


}
