package com.github.pinont.devtool.menu;

import com.github.pinont.devtool.utils.getWorldEnvironmentBlock;
import com.github.pinont.devtool.utils.properWorldName;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the server world manager interface.
 */
public class showServerWorldManger {

    public static void showServerWorldManger(Player p) {
        Menu worldManagerMenu = new Menu("World Manager", 9);
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
                    return new ItemCreator(getWorldEnvironmentBlock.getWorldEnvironmentBlock(world)).setName(properWorldName.properWorldName(world)).addLore(ChatColor.BOLD + "" + ChatColor.YELLOW + "Click to edit").create();
                }

                @Override
                public void onClick(Player player) {
                    showSingleWorldManager.showSingleWorldManager(world, player);
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
                return new ItemCreator(new ItemStack(Material.BEDROCK)).setName(ChatColor.YELLOW + "Click to create new world").create();
            }

            @Override
            public void onClick(Player player) {
                showWorldCreator.showWorldCreator(player);
            }
        });
        worldManagerMenu.show(p);
    }
}
