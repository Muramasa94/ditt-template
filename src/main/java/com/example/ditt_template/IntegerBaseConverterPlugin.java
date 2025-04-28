package com.example.ditt_template;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class IntegerBaseConverterPlugin extends Plugin {

    public IntegerBaseConverterPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        // TODO: Add startup logic if needed
        System.out.println("YourPlugin started.");
    }

    @Override
    public void stop() {
        // TODO: Add cleanup logic if needed
        System.out.println("YourPlugin stopped.");
    }
}
