package com.github.pinont.devtool.menu;

import com.github.pinont.devtool.menu.submenu.CustomItemMenu;
import com.github.pinont.devtool.menu.submenu.OtherToolsMenu;
import com.github.pinont.devtool.menu.submenu.ServerPlayerManagerMenu;
import com.github.pinont.devtool.menu.submenu.ServerWorldMangerMenu;
import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.pinont.singularitylib.api.utils.Common.getAPIVersion;

/**
 * Opens the developer tool interface for the specified player.
 */
public class DevToolMenu {

    /**
     * Opens the developer tool interface for the specified player.
     *
     * @param player the player to open the interface for
     */
    public static void openDevTool(Player player) {
        String version = getAPIVersion();
        Menu devMenu = new Menu(CorePlugin.getInstance(), ChatColor.DARK_RED + "Developer Tools " + ChatColor.GRAY + "(" + version + ")", 9*5);
        devMenu.setLayout("=========", "====i====", "=========", "==w=p=t==", "====c====", "=========");
        devMenu.setKey(
                Blank.getLayout(),
                new Layout('i', new Button() {

                            @Override
                            public ItemStack getItem() {
                                // Components (not legacy ChatColor strings): ItemCreator's string
                                // setters run MiniMessage, which rejects legacy § codes.
                                return new ItemCreator(CorePlugin.getInstance(), new ItemStack(Material.GRASS_BLOCK))
                                        .setName(Component.text("Server Info", NamedTextColor.GREEN))
                                        .addLore(
                                                Component.text("Server: ", NamedTextColor.GRAY).append(Component.text(Bukkit.getServer().getName(), NamedTextColor.YELLOW)),
                                                Component.text("Version: ", NamedTextColor.GRAY).append(Component.text(Bukkit.getServer().getVersion(), NamedTextColor.YELLOW)),
                                                Component.text("Plugins (", NamedTextColor.GRAY).append(Component.text(Bukkit.getServer().getPluginManager().getPlugins().length, NamedTextColor.YELLOW)).append(Component.text(")", NamedTextColor.GRAY))
                                        ).create();
                            }

                            @Override
                            public void onClick(Player player) {

                            }
                        }),
                new Layout('p', new Button() { // player list
                            @Override
                            public ItemStack getItem() {
                                return new ItemHeadCreator(CorePlugin.getInstance(), new ItemStack(Material.PLAYER_HEAD))
                                        .setOwner(player.getName())
                                        .setName(Component.text("Player List")).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                ServerPlayerManagerMenu.showServerPlayerManager(player);
                            }
                        }),
                new Layout('w', new Button() { // worldcreator
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), new ItemStack(Material.COARSE_DIRT)).setName(Component.text("Worlds")).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                ServerWorldMangerMenu.showServerWorldManger(player);
                            }
                        }),
                new Layout('t', new Button() { // tools
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.STICK)
                                        .setName(Component.text("Tools"))
                                        .addLore(Component.text("More Tools")).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                OtherToolsMenu.showOtherTools(player);
                            }
                        }),
                new Layout('c', new Button() { // customItem
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.CHEST)
                                        .setName(Component.text("Custom Items"))
                                        .addLore(Component.text("Open Custom Item Creator")).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                CustomItemMenu.showCustomItemList(player);
                            }
                        })
        );
        devMenu.show(player);
    }
}
