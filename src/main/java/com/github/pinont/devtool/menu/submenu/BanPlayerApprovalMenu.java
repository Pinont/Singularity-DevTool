package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

/**
 * Shows the ban player approval confirmation dialog.
 */
public class BanPlayerApprovalMenu {

    public static void showBanPlayerApproval(Player origin, Player target) {
        new Menu(ChatColor.RED + "Are you sure to ban " + target.getName() + "?", 9 * 5)
                .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
                .setKey(Blank.getLayout(),
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
                                        return new ItemHeadCreator(new ItemStack(Material.PLAYER_HEAD)).setOwner(target.getName()).setName(ChatColor.RED + "Are you sure to ban " + target.getName() + "?").create();
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
                                        target.ban("You have been banned from this server.", (Date) null, player.getName(), true);
                                        ServerPlayerManagerMenu.showServerPlayerManager(origin);
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
                                        SpecificPlayerManagerMenu.showSpecificPlayerManager(player, target);
                                    }
                                };
                            }
                        }
                ).show(origin);
    }
}
