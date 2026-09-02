package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.StartConversation;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.manager.CommentConfigManager;
import com.github.pinont.singularitylib.api.ui.Button;
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
 * DevTool v2: LIVE config editor for a plugin's {@code config.yml}.
 *
 * <p>Renders every key as a paginated button:
 * <ul>
 *   <li><b>Boolean</b> → click toggles the value;</li>
 *   <li><b>Number</b> → left-click +1, right-click −1 (stepless ± by shift);</li>
 *   <li><b>String</b> → a chat prompt appears to type the new value.</li>
 * </ul>
 * <b>Save &amp; Reload</b> persists via {@link CommentConfigManager#save()} and triggers
 * the plugin's {@code reloadReloadables()} hook; <b>Revert</b> closes without saving.
 */
public final class ConfigEditorMenu {

    private ConfigEditorMenu() {
    }

    public static void open(Player player, CorePlugin plugin) {
        new Editor(plugin, new CommentConfigManager(plugin, "config.yml")).show(player);
    }

    private static int[] contentSlots() {
        int[] s = new int[27];
        for (int i = 0; i < 27; i++) s[i] = 10 + (i / 7) * 9 + (i % 7); // 3x7 grid in the middle
        return s;
    }

    static final class Editor extends PaginatedMenu<String> {

        private final CorePlugin target;
        private final CommentConfigManager manager;

        Editor(CorePlugin target, CommentConfigManager manager) {
            super(target, "Config: " + target.getName() + "/config.yml", 54, 27,
                    ConfigEditorMenu.contentSlots(), new ArrayList<>(), null, null);
            this.target = target;
            this.manager = manager;
        }

        @Override
        public void show(Player player) {
            List<String> keys = new ArrayList<>(manager.getConfig().getValues(false).keySet());
            updateItems(keys, this::renderKey, this::handleClick);
            super.show(player);
        }

        private void handleClick(Player player, String key) {
            Object value = manager.get(key);
            if (value instanceof Boolean b) {
                manager.set(key, !b);
                player.sendMessage(Component.text(key + " -> " + (!b), NamedTextColor.GREEN));
                show(player);
            } else if (value instanceof Number n) {
                manager.set(key, n.intValue() + (player.isSneaking() ? -1 : 1));
                player.sendMessage(Component.text(key + " -> " + manager.get(key), NamedTextColor.GREEN));
                show(player);
            } else if (value instanceof String s) {
                player.closeInventory();
                StartConversation.ask(player,
                        "Type the new value for '" + key + "' (current: '" + s
                                + "'), or 'cancel' to abort.",
                        input -> {
                            manager.set(key, input);
                            player.sendMessage(Component.text(key + " -> '" + input
                                    + "' (use Save to persist).", NamedTextColor.GREEN));
                            show(player);
                        },
                        () -> {
                            player.sendMessage(Component.text("Edit cancelled.", NamedTextColor.GRAY));
                            show(player);
                        });
            } else {
                player.sendMessage(Component.text("Cannot edit key '" + key + "' (" + value + ")", NamedTextColor.RED));
            }
        }

        private ItemStack renderKey(String key) {
            Object v = manager.get(key);
            Material mat = v instanceof Boolean ? (Boolean) v ? Material.GREEN_WOOL : Material.RED_WOOL
                    : v instanceof Number ? Material.REPEATER
                    : v instanceof String ? Material.PAPER
                    : Material.PAPER;
            String val = v == null ? "null" : v.toString();
            String type = v instanceof Boolean ? "boolean"
                    : v instanceof Number ? "number"
                    : v instanceof String ? "string"
                    : v.getClass().getSimpleName();
            return new ItemCreator(target, mat)
                    .setName(Component.text(key, NamedTextColor.WHITE))
                    .addLore(Component.text("Type: " + type, NamedTextColor.GRAY),
                            Component.text("Value: " + val, NamedTextColor.YELLOW),
                            Component.text("Click to edit", NamedTextColor.DARK_GRAY))
                    .create();
        }

        @Override
        protected void addExtraBottomRowInto(List<Button> buttons) {
            buttons.add(StandardButtons.confirm(target, Material.EMERALD_BLOCK, "Save & Reload", this::save));
            buttons.add(StandardButtons.cancel(target, Material.REDSTONE_BLOCK, "Revert", this::revert));
            buttons.add(StandardButtons.close(target));
        }

        private void save() {
            manager.save();
            target.reloadReloadables();
        }

        private void revert() {
            // no-op: just closes (staged manager discarded)
        }
    }
}