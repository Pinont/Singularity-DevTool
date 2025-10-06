package com.github.pinont.devtool.utils;

import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.manager.WorldManager;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the delete world approval confirmation dialog.
 */
public class showDeleteWorldApproval {

    public static void showDeleteWorldApproval(Player player, World targetWorld) {
        new Menu(ChatColor.RED + "Are you sure to delete " + targetWorld.getName() + "?")
                .setLayout("=========", "====w====", "=========", "==a===d==", "=========")
                .setKey(blank.blank(),
                        new Layout() {
                            @Override
                            public char getKey() {
                                return 'w';
                            }

                            @Override
                            public Button getButton() {
                                return new Button() {
                                    @Override
                                    public ItemStack getItem() {
                                        return new ItemCreator(new ItemStack(getWorldEnvironmentBlock.getWorldEnvironmentBlock(targetWorld))).setName(ChatColor.RED + "Are you sure to delete " + targetWorld.getName() + "?").create();
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
                                return 'a';
                            }

                            @Override
                            public Button getButton() {
                                return new Button() {
                                    @Override
                                    public ItemStack getItem() {
                                        return new ItemCreator(Material.GREEN_STAINED_GLASS).setName(ChatColor.GREEN + "ACCEPT").create();
                                    }

                                    @Override
                                    public void onClick(Player player) {
                                        WorldManager.delete(targetWorld.getName());
                                        player.sendMessage(ChatColor.RED + targetWorld.getName() + " is now mark for removal!");
                                        showServerWorldManger.showServerWorldManger(player);
                                    }
                                };
                            }
                        },
                        new Layout() {
                            @Override
                            public char getKey() {
                                return 'd';
                            }

                            @Override
                            public Button getButton() {
                                return new Button() {
                                    @Override
                                    public ItemStack getItem() {
                                        return new ItemCreator(Material.RED_STAINED_GLASS).setName(ChatColor.RED + "DENY").create();
                                    }

                                    @Override
                                    public void onClick(Player player) {
                                        showSingleWorldManager.showSingleWorldManager(targetWorld, player);
                                    }
                                };
                            }
                        }
                ).show(player);
    }
}
