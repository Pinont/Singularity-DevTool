package com.github.pinont.devtool;

import com.github.pinont.singularitylib.api.registry.PluginRegistry;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

/**
 * Verifies DevTool's lifecycle wiring under MockBukkit:
 * <ul>
 *   <li>{@code CorePlugin.onEnable} registers the plugin in
 *       {@link PluginRegistry} (auto-discovery surface);</li>
 *   <li>{@code DevTool.onPluginStart} performs the explicit v2 registration
 *       ({@code registerComponents(new DevToolCommand())}) and the command
 *       shows up in the registry's per-plugin component snapshot;</li>
 *   <li>the plugin is enabled.</li>
 * </ul>
 */
public class DevToolRegistrationTest {

    private DevTool plugin;

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        PluginRegistry.clear();
        this.plugin = MockBukkit.load(DevTool.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
        PluginRegistry.clear();
    }

    @Test
    @DisplayName("DevTool registers itself in PluginRegistry on load")
    public void registersItself() {
        Assertions.assertTrue(PluginRegistry.count() > 0,
                "PluginRegistry should contain at least DevTool after load");
        Assertions.assertNotNull(PluginRegistry.get("SingularityDevTool"),
                "PluginRegistry should resolve DevTool by its plugin name");
        Assertions.assertSame(plugin, PluginRegistry.get("SingularityDevTool"),
                "registry should hold the loaded plugin instance");
    }

    @Test
    @DisplayName("onPluginStart explicit registration publishes the devtool command component")
    public void registersComponentsExplicitly() {
        CorePlugin registered = PluginRegistry.get("SingularityDevTool");
        Assertions.assertNotNull(registered, "plugin registered before component snapshot check");

        var components = PluginRegistry.componentsOf(registered);
        Assertions.assertNotNull(components, "component snapshot present");
        Assertions.assertFalse(components.commands().isEmpty(),
                "DevToolCommand should be published via registerComponents");
        Assertions.assertTrue(
                components.commands().stream()
                        .anyMatch(c -> c.getName().equals("devtool:dt")),
                "the published command should be the devtool command group");
    }

    @Test
    @DisplayName("DevTool is enabled after load")
    public void isEnabled() {
        Assertions.assertTrue(plugin.isEnabled(), "plugin should be enabled");
    }
}