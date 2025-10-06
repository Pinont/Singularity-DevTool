package com.github.pinont.devtool.utils;

import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.pinont.singularitylib.plugin.CorePlugin.getAPIVersion;

/**
 * Opens the developer tool interface for the specified player.
 */
public class openDevTool {

    /**
     * Opens the developer tool interface for the specified player.
     *
     * @param player the player to open the interface for
     */
    public static void openDevTool(Player player) {
        String version = getAPIVersion();
        Menu devMenu = new Menu(ChatColor.DARK_RED + "Developer Tools " + ChatColor.GRAY + "(" + version + ")", 9*5);
        devMenu.setLayout("=========", "====i====", "=========", "==w=p=t==", "=========");
        devMenu.setKey(
                blank.blank(),
                new Layout() {

                    @Override
                    public char getKey() {
                        return 'i';
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {

                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(new ItemStack(Material.GRASS_BLOCK)).setName(ChatColor.GREEN + "Server Info").addLore(ChatColor.GRAY + "Server: " + ChatColor.YELLOW + Bukkit.getServer().getName(), ChatColor.GRAY + "Version: " + ChatColor.YELLOW + Bukkit.getServer().getVersion(), ChatColor.GRAY + "Plugins (" + ChatColor.YELLOW + Bukkit.getServer().getPluginManager().getPlugins().length + ChatColor.GRAY + ")").create();
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
                        return 'p';
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemHeadCreator(new ItemStack(Material.PLAYER_HEAD)).setOwner(player.getName()).setName("Player List").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showServerPlayerManager.showServerPlayerManager(player);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'w'; // worldcreator
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(new ItemStack(Material.COARSE_DIRT)).setName("Worlds").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showServerWorldManger.showServerWorldManger(player);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 't'; // tools
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.STICK).setName("Tools").addLore("More Tools").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showOtherTools.showOtherTools(player);
                            }
                        };
                    }
                }
        );
        devMenu.show(player);
    }
}
