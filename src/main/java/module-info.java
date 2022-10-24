open module com.pi4j.mvc {
     // Pi4J Modules
    requires com.pi4j;
    requires com.pi4j.library.pigpio;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.mock;
    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    // for logging
    requires org.slf4j;


    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires java.logging;


    // Module Exports


}
