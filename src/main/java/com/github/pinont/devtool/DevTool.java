package com.github.pinont.devtool;

import com.github.pinont.devtool.commands.DevToolCommand;
import com.github.pinont.singularitylib.plugin.CorePlugin;

public class DevTool extends CorePlugin {

    @Override
    public void onPluginStart() {
        // Explicit registration (v2 DSL) — no @AutoRegister classpath scanning.
        registerComponents(new DevToolCommand());
    }

    @Override
    public void onPluginStop() {
    }
}