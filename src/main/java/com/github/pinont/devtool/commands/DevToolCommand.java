package com.github.pinont.devtool.commands;

import com.github.pinont.devtool.menu.DevToolMenu;
import com.github.pinont.devtool.menu.submenu.PluginPanelMenu;
import com.github.pinont.singularitylib.api.command.CommandGroup;
import com.github.pinont.singularitylib.api.command.SubCommand;
import com.github.pinont.singularitylib.api.registry.PluginRegistry;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * DevTool v2 root command.
 *
 * <p>{@code /devtool} → opens the plugin auto-discovery menu.
 * {@code /devtool <plugin>} → opens that plugin's panel.
 * {@code /devtool help} → lists discovered plugins + usage.
 */
public class DevToolCommand extends CommandGroup {

    public DevToolCommand() {
        registerSubcommand(new HelpSub());
    }

    @Override
    public String getName() {
        return "devtool:dt";
    }

    @Override
    public String usage(Boolean bool) {
        return "/devtool [plugin]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // Root: open the auto-discovery plugin menu
            if (sender instanceof Player player) {
                DevToolMenu.openDevTool(player);
            } else {
                sender.sendMessage(Component.text("DevTool: /devtool <plugin> — players only for the GUI.", NamedTextColor.YELLOW));
            }
            return;
        }

        // First arg is a known subcommand (help)? Dispatch normally.
        if (getSubcommandNames().contains(args[0].toLowerCase())) {
            super.execute(sender, args);
            return;
        }

        // Otherwise treat first arg as a plugin name to open its panel.
        CorePlugin plugin = PluginRegistry.get(args[0]);
        if (plugin == null) {
            sender.sendMessage(Component.text("Unknown Singularity plugin: " + args[0]
                    + ". Use /devtool help to list them.", NamedTextColor.RED));
            return;
        }
        if (sender instanceof Player player) {
            PluginPanelMenu.open(player, plugin);
        } else {
            sender.sendMessage(Component.text(plugin.getName() + " v" + plugin.getPluginMeta().getVersion(), NamedTextColor.YELLOW));
        }
    }

    /**
     * /devtool help — lists discovered plugins + usage.
     */
    static class HelpSub extends SubCommand {

        @Override
        public String getName() {
            return "help";
        }

        @Override
        public String getDescription() {
            return "List discovered plugins and usage";
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            sender.sendMessage(Component.text("—— Singularity plugins (" + PluginRegistry.count() + ") ——", NamedTextColor.GOLD));
            for (CorePlugin plugin : PluginRegistry.plugins()) {
                sender.sendMessage(Component.text("• " + plugin.getName(), NamedTextColor.YELLOW)
                        .append(Component.text(" v" + plugin.getPluginMeta().getVersion(), NamedTextColor.GRAY)));
            }
            sender.sendMessage(Component.text("Usage: /devtool <plugin>", NamedTextColor.WHITE));
        }
    }
}