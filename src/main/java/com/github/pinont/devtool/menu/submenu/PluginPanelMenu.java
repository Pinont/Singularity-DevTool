package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.api.ui.PaginatedMenu;
import com.github.pinont.singularitylib.api.ui.StandardButtons;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * DevTool v2: panel for a single auto-discovered Singularity plugin.
 * Uses the lib's PaginatedMenu to show plugin info + a placeholder
 * component list (commands/listeners/items come from the lib registry in a
 * later iteration — slot is reserved).
 */
public final class PluginPanelMenu {

    private PluginPanelMenu() {
    }

    public static void open(Player player, CorePlugin plugin) {
        List<String> rows = new ArrayList<>();
        rows.add("Name: " + plugin.getName());
        rows.add("Version: " + plugin.getPluginMeta().getVersion());
        rows.add("Class: " + plugin.getClass().getSimpleName());
        rows.add("Status: enabled=" + plugin.isEnabled());
        rows.add("—");
        rows.add("Components (coming with lib registry v2):");
        rows.add("  commands, listeners, custom items, scheduled tasks");

        PluginPanel panel = new PluginPanel(plugin, rows);
        panel.show(player);
    }

    /**
     * Concrete PaginatedMenu over the info rows.
     */
    private static final class PluginPanel extends PaginatedMenu<String> {

        PluginPanel(CorePlugin plugin, List<String> rows) {
            super(
                    com.github.pinont.singularitylib.plugin.CorePlugin.getInstance(),
                    "Plugin: " + plugin.getName(),
                    27, // 3 rows
                    21 - 9 + 7, // room for nav row; 7 content slots
                    new int[]{10, 11, 12, 13, 14, 15, 16},
                    rows,
                    PluginPanel::renderRow,
                    (p, row) -> {
                    }
            );
            addButton(StandardButtons.close(com.github.pinont.singularitylib.plugin.CorePlugin.getInstance()));
        }

        private static ItemStack renderRow(String row) {
            return new ItemCreator(
                    com.github.pinont.singularitylib.plugin.CorePlugin.getInstance(),
                    Material.PAPER
            ).setName(Component.text(row, NamedTextColor.WHITE)).create();
        }
    }
}