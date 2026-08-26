package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the specific player manager interface for managing a particular player.
 */
public class SpecificPlayerManagerMenu {

    public static void showSpecificPlayerManager(Player origin, Player target) {
        Menu playerManager = new Menu(CorePlugin.getInstance(), "Player Manager", 9 * 5);
        playerManager.setLayout("====p====", "=========", "==t=i=o==", "==b=k=n==", "====v====", "=========");
        playerManager.setKey(
                Blank.getLayout(),
                new Layout('p', new Button() {
                            @Override
                            public ItemStack getItem() {
                                String firstPlayedDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                        .format(new java.util.Date(target.getFirstPlayed()));
                                return new ItemHeadCreator(CorePlugin.getInstance(), new ItemStack(Material.PLAYER_HEAD))
                                        .setOwner(target.getName())
                                        .setName(target.getName())
                                        .addLore(ChatColor.BOLD + "" + ChatColor.GRAY + "First Joined: " + ChatColor.YELLOW + firstPlayedDate)
                                        .create();
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
                                player.teleport(target.getLocation());
                                player.closeInventory();
                            }
                        }),
                new Layout('i', null), // player Inventory
                new Layout('b', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.ANVIL).setName(ChatColor.RED + "Ban").addLore(ChatColor.RED + "Click to ban.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                BanPlayerApprovalMenu.showBanPlayerApproval(player, target);
                            }
                        }),
                new Layout('k', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.REDSTONE).setName(ChatColor.RED + "Kick").addLore(ChatColor.RED + "Click to kick.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                KickPlayerApprovalMenu.showKickPlayerApproval(player, target);
                            }
                        }),
                new Layout('o', null), // op Player
                new Layout('n', new Button() { // invincibility
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.TOTEM_OF_UNDYING).setName("God: " + target.isInvulnerable()).create();
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
                        }),
                new Layout('v', null)  // vanish
        ).show(origin);
    }
}
