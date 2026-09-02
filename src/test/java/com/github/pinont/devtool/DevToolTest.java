package com.github.pinont.devtool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

/**
 * Smoke tests: DevTool boots correctly under a mocked Paper 26.2 server
 * (bootstrap model — depends on SingularityLib for its plugin base class).
 */
public class DevToolTest {

    private DevTool plugin;

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        this.plugin = MockBukkit.load(DevTool.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("DevTool loads and is enabled")
    public void loads() {
        Assertions.assertNotNull(plugin, "plugin should load under MockBukkit");
        Assertions.assertTrue(plugin.isEnabled(), "plugin should be enabled");
    }

    @Test
    @DisplayName("DevTool's plugin name is correct")
    public void name() {
        Assertions.assertEquals("SingularityDevTool", plugin.getName());
    }

    @Test
    @DisplayName("DevTool extends CorePlugin (bootstrap model)")
    public void extendsCorePlugin() {
        Assertions.assertTrue(
                com.github.pinont.singularitylib.plugin.CorePlugin.class.isAssignableFrom(plugin.getClass()),
                "DevTool should be a CorePlugin");
    }
}