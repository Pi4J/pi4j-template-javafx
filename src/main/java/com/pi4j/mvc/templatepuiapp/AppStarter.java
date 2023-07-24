package com.pi4j.mvc.templatepuiapp;

import com.pi4j.context.Context;
import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.templatepuiapp.view.SomePUI;
import com.pi4j.mvc.util.Pi4JContext;
import com.pi4j.mvc.util.mvcbase.MvcLogger;

public class AppStarter {

    private static final MvcLogger LOGGER = new MvcLogger();

    public static void main(String[] args) {
        Context pi4J = Pi4JContext.createContext();

        SomeController controller = new SomeController(new SomeModel());
        new SomePUI(controller, pi4J);

        LOGGER.logInfo("App started");

        // This will ensure Pi4J is properly finished. All I/O instances are
        // released by the system and shutdown in the appropriate
        // manner. It will also ensure that any background
        // threads/processes are cleanly shutdown and any used memory
        // is returned to the system.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            controller.shutdown();
            pi4J.shutdown();
        }));

    }
}
