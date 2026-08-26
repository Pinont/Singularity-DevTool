package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.GetWorldEnvironmentBlock;
import com.github.pinont.devtool.methods.ProperWorldName;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the server world manager interface.
 */
public class ServerWorldMangerMenu {

    public static void showServerWorldManger(Player p) {
        Menu worldManagerMenu = new Menu(CorePlugin.getInstance(), "World Manager", 9);
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            int finalCount = count;
            worldManagerMenu.addButton(new Button() {
                @Override
                public int getSlot() {
                    return finalCount;
                }

                @Override
                public ItemStack getItem() {
                    return new ItemCreator(CorePlugin.getInstance(), GetWorldEnvironmentBlock.getWorldEnvironmentBlock(world)).setName(ProperWorldName.properWorldName(world)).addLore(ChatColor.BOLD + "" + ChatColor.YELLOW + "Click to edit").create();
                }

                @Override
                public void onClick(Player player) {
                    SingleWorldManagerMenu.showSingleWorldManager(world, player);
                }
            });
            count++;
        }
        int finalCount = count;
        worldManagerMenu.addButton(new Button() {
            @Override
            public int getSlot() {
                return finalCount;
            }

            @Override
            public ItemStack getItem() {
                return new ItemCreator(CorePlugin.getInstance(), new ItemStack(Material.BEDROCK)).setName(ChatColor.YELLOW + "Click to create new world").create();
            }

            @Override
            public void onClick(Player player) {
                WorldCreatorMenu.showWorldCreator(player);
            }
        });
        worldManagerMenu.show(p);
    }
}
