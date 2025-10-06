package com.github.pinont.devtool.utils;

import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the specific player manager interface for managing a particular player.
 */
public class showSpecificPlayerManager {

    public static void showSpecificPlayerManager(Player origin, Player target) {
        Menu playerManager = new Menu("Player Manager", 9 * 5);
        playerManager.setLayout("====p====", "=========", "==t=i=o==", "==b=k=n==", "====v====", "=========");
        playerManager.setKey(
                blank.blank(),
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
                                String firstPlayedDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                        .format(new java.util.Date(target.getFirstPlayed()));
                                return new ItemHeadCreator(new ItemStack(Material.PLAYER_HEAD))
                                        .setOwner(target.getName())
                                        .setName(target.getName())
                                        .addLore(ChatColor.BOLD + "" + ChatColor.GRAY + "First Joined: " + ChatColor.YELLOW + firstPlayedDate)
                                        .create();
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
                                player.teleport(target.getLocation());
                                player.closeInventory();
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'i'; // player Inventory
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'b';
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.ANVIL).setName(ChatColor.RED + "Ban").addLore(ChatColor.RED + "Click to ban.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showBanPlayerApproval.showBanPlayerApproval(player, target);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'k';
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.REDSTONE).setName(ChatColor.RED + "Kick").addLore(ChatColor.RED + "Click to kick.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showKickPlayerApproval.showKickPlayerApproval(player, target);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'o'; // op Player
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'n'; // invincibility
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.TOTEM_OF_UNDYING).setName("God: " + target.isInvulnerable()).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                target.setInvulnerable(!target.isInvulnerable());
                                if (Bukkit.getServer().getAllowFlight()) {
                                    target.setAllowFlight(target.isInvulnerable());
                                }
                                else
                                    player.sendMessage(ChatColor.RED + "You need to enable flight to use flying feature.");
                                showSpecificPlayerManager(player, target);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() { // vanish
                        return 'v';
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                }
        ).show(origin);
    }
}
