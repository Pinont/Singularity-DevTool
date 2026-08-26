package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.devtool.methods.GetWorldEnvironmentBlock;
import com.github.pinont.devtool.methods.ProperWorldName;
import com.github.pinont.devtool.methods.WorldDeleteButton;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the single world manager interface for a specific world.
 */
public class SingleWorldManagerMenu {

    public static void showSingleWorldManager(World world, Player player) {
        Menu worldManagerMenu = new Menu(CorePlugin.getInstance(), world.getName() + ": World Manager");
        worldManagerMenu.setLayout("=========", "====w====", "=========", "==t=d=r==", "=========");
        worldManagerMenu.setKey(
                Blank.getLayout(),
                new Layout('w', new Button() { // world info

                            @Override
                            public ItemStack getItem() {

                                return new ItemCreator(CorePlugin.getInstance(), GetWorldEnvironmentBlock.getWorldEnvironmentBlock(world)).setName(ChatColor.GREEN + "World Info").addLore(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + ProperWorldName.properWorldName(world), ChatColor.GRAY + "Difficulty: " + ChatColor.YELLOW + world.getDifficulty(), ChatColor.GRAY + "Environment Type: " + ChatColor.YELLOW + world.getEnvironment()).create();
                            }

                            @Override
                            public void onClick(Player player) {

                            }
                        }),
                new Layout('t', new Button() { // teleport
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), new ItemStack(Material.BEACON)).setName("Teleport").addLore(ChatColor.BOLD + "" + ChatColor.YELLOW + "Click to Teleport").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                if (player.getWorld() != world) {
                                    player.sendMessage(ChatColor.GRAY + "Teleporting to " + ProperWorldName.properWorldName(world) + "...");
                                    player.teleport(world.getSpawnLocation());
                                } else {
                                    player.sendMessage(ChatColor.RED + "You are already in this world!");
                                }
                            }
                        }),
                new Layout('r', null), // gamerules
                WorldDeleteButton.worldDeleteButton(world)
        ).show(player);
    }
}
