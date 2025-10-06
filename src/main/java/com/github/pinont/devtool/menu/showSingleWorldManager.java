package com.github.pinont.devtool.menu;

import com.github.pinont.devtool.utils.blank;
import com.github.pinont.devtool.utils.getWorldEnvironmentBlock;
import com.github.pinont.devtool.utils.properWorldName;
import com.github.pinont.devtool.utils.worldDeleteButton;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the single world manager interface for a specific world.
 */
public class showSingleWorldManager {

    public static void showSingleWorldManager(World world, Player player) {
        Menu worldManagerMenu = new Menu(world.getName() + ": World Manager");
        worldManagerMenu.setLayout("=========", "====w====", "=========", "==t=d=r==", "=========");
        worldManagerMenu.setKey(
                blank.blank(),
                new Layout() {

                    @Override
                    public char getKey() {
                        return 'w';
                    }

                    @Override
                    public Button getButton() {
                        return new Button() { // world info

                            @Override
                            public ItemStack getItem() {

                                return new ItemCreator(getWorldEnvironmentBlock.getWorldEnvironmentBlock(world)).setName(ChatColor.GREEN + "World Info").addLore(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + properWorldName.properWorldName(world), ChatColor.GRAY + "Difficulty: " + ChatColor.YELLOW + world.getDifficulty(), ChatColor.GRAY + "Environment Type: " + ChatColor.YELLOW + world.getEnvironment()).create();
                            }

                            @Override
                            public void onClick(Player player) {

                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 't'; // teleport
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(new ItemStack(Material.BEACON)).setName("Teleport").addLore(ChatColor.BOLD + "" + ChatColor.YELLOW + "Click to Teleport").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                if (player.getWorld() != world) {
                                    player.sendMessage(ChatColor.GRAY + "Teleporting to " + properWorldName.properWorldName(world) + "...");
                                    player.teleport(world.getSpawnLocation());
                                } else {
                                    player.sendMessage(ChatColor.RED + "You are already in this world!");
                                }
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'r'; // gamerules
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                },
                worldDeleteButton.worldDeleteButton(world)
        ).show(player);
    }
}
