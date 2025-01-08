package com.pi4j.mvc.templatepuiapp;

import com.pi4j.context.Context;
import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.templatepuiapp.view.SomePUI;
import com.pi4j.mvc.util.mvcbase.MvcLogger;

public class AppStarter {

    private static final MvcLogger LOGGER = new MvcLogger();

    public static void main(String[] args) {
        SomeController controller = new SomeController(new SomeModel());
        SomePUI pui = new SomePUI(controller);

        LOGGER.logInfo("App started");

        // This will ensure Pi4J is properly finished. All I/O instances are
        // released by the system and shutdown in the appropriate
        // manner. It will also ensure that any background
        // threads/processes are cleanly shutdown and any used memory
        // is returned to the system.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            controller.shutdown();
            pui.shutdown();
        }));

    }
}
