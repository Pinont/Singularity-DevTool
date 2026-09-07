package com.github.pinont.devtool.commands;

import com.github.pinont.devtool.DevTool;
import com.github.pinont.singularitylib.api.registry.PluginRegistry;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.command.MessageTarget;

/**
 * Exercises {@link DevToolCommand} through the {@code CommandGroup}
 * sender-based dispatch path ({@code execute(CommandSender, String[])})
 * — no CommandSourceStack / brigadier needed.
 *
 * <p>Goal: cover the real dispatch + {@link PluginRegistry} lookup logic
 * (root menu, help subcommand, unknown-plugin failure path, known-plugin
 * panel path) with robust no-crash assertions. Deep MockBukkit
 * inventory/click simulation is deliberately avoided.
 *
 * <p>DevTool is loaded in {@code setUp} so {@code onPluginStart}'s explicit
 * registration puts "SingularityDevTool" into {@link PluginRegistry} — that
 * is what the {@code /devtool <known>} path resolves against.
 */
public class DevToolCommandTest {

    private ServerMock server;
    private DevToolCommand command;

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        PluginRegistry.clear();
        // Loading DevTool runs CorePlugin.onEnable -> PluginRegistry.register
        // ("SingularityDevTool") and DevTool.onPluginStart ->
        // registerComponents(new DevToolCommand()).
        MockBukkit.load(DevTool.class);
        server = MockBukkit.getMock();
        command = new DevToolCommand();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
        PluginRegistry.clear();
    }

    // -- helpers -----------------------------------------------------------

    private Player addPlayer(String name) {
        return server.addPlayer(name);
    }

    /**
     * Scans the console's queued messages for one containing {@code needle}.
     * The queue also holds startup chatter from CorePlugin.onEnable, so we
     * search instead of assuming queue position.
     */
    private static boolean sawMessageContaining(MessageTarget target, String needle) {
        String message;
        while ((message = target.nextMessage()) != null) {
            if (message.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    // -- tests -------------------------------------------------------------

    @Test
    @DisplayName("/devtool (no args) by a player opens the dev menu without crashing")
    public void noArgsByPlayerOpensMenu() {
        Player player = addPlayer("Pinont");

        Assertions.assertDoesNotThrow(
                () -> command.execute(player, new String[]{}),
                "/devtool with no args by a player should not throw");

        // DevToolMenu builds a 9x5 = 45-slot chest and opens it on success.
        Assertions.assertEquals(
                45,
                player.getOpenInventory().getTopInventory().getSize(),
                "player should be viewing the 45-slot developer tools menu");
    }

    @Test
    @DisplayName("/devtool help by console sends the plugin listing without crashing")
    public void helpByConsoleSendsListing() {
        ConsoleCommandSender console = server.getConsoleSender();

        Assertions.assertDoesNotThrow(
                () -> command.execute(console, new String[]{"help"}),
                "/devtool help should not throw");

        Assertions.assertTrue(
                sawMessageContaining((MessageTarget) console, "Singularity plugins"),
                "console should have received the /devtool help plugin listing");
    }

    @Test
    @DisplayName("/devtool <unknown> by console reports unknown plugin without crashing")
    public void unknownPluginByConsoleDoesNotThrow() {
        ConsoleCommandSender console = server.getConsoleSender();

        Assertions.assertDoesNotThrow(
                () -> command.execute(console, new String[]{"unknownplugin"}),
                "/devtool <unknown> should not throw");

        Assertions.assertTrue(
                sawMessageContaining((MessageTarget) console, "Unknown Singularity plugin"),
                "console should have received the unknown-plugin failure message");
    }

    @Test
    @DisplayName("/devtool SingularityDevTool by console reports name + version")
    public void knownPluginByConsoleSendsInfo() {
        ConsoleCommandSender console = server.getConsoleSender();

        Assertions.assertDoesNotThrow(
                () -> command.execute(console, new String[]{"SingularityDevTool"}),
                "/devtool <known> by console should not throw");

        Assertions.assertTrue(
                sawMessageContaining((MessageTarget) console, "SingularityDevTool v"),
                "console should have received the discovered plugin name + version");
    }

    @Test
    @DisplayName("/devtool SingularityDevTool by a player opens the plugin panel without crashing")
    public void knownPluginByPlayerOpensPanel() {
        Player player = addPlayer("Pinont");

        Assertions.assertDoesNotThrow(
                () -> command.execute(player, new String[]{"SingularityDevTool"}),
                "/devtool <known> by a player should not throw");

        // PluginPanelMenu is a 27-slot chest (3 rows).
        Assertions.assertEquals(
                27,
                player.getOpenInventory().getTopInventory().getSize(),
                "player should be viewing the 27-slot plugin panel");
    }

    @Test
    @DisplayName("/devtool itemstudio by a player opens the 54-slot studio without crashing")
    public void itemStudioByPlayerOpensMenu() {
        Player player = addPlayer("Pinont");

        Assertions.assertDoesNotThrow(
                () -> command.execute(player, new String[]{"itemstudio"}),
                "/devtool itemstudio should not throw");

        Assertions.assertEquals(
                54,
                player.getOpenInventory().getTopInventory().getSize(),
                "player should be viewing the 54-slot Item Studio");
    }

    @Test
    @DisplayName("/devtool entitystudio by a player opens the 54-slot studio without crashing")
    public void entityStudioByPlayerOpensMenu() {
        Player player = addPlayer("Pinont");

        Assertions.assertDoesNotThrow(
                () -> command.execute(player, new String[]{"entitystudio"}),
                "/devtool entitystudio should not throw");

        Assertions.assertEquals(
                54,
                player.getOpenInventory().getTopInventory().getSize(),
                "player should be viewing the 54-slot Entity Studio");
    }

    @Test
    @DisplayName("/devtool snippet with an empty hand reports hold-an-item without crashing")
    public void snippetEmptyHandDoesNotThrow() {
        Player player = addPlayer("Pinont");

        Assertions.assertDoesNotThrow(
                () -> command.execute(player, new String[]{"snippet"}),
                "/devtool snippet should not throw");
    }

    @Test
    @DisplayName("/devtool snippet by console is players-only")
    public void snippetByConsoleIsPlayersOnly() {
        ConsoleCommandSender console = server.getConsoleSender();

        Assertions.assertDoesNotThrow(
                () -> command.execute(console, new String[]{"snippet"}),
                "/devtool snippet by console should not throw");

        Assertions.assertTrue(
                sawMessageContaining((MessageTarget) console, "Players only"),
                "console should have received the players-only message");
    }
}