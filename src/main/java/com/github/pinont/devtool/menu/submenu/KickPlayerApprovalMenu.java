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
 * Shows the kick player approval confirmation dialog.
 */
public class KickPlayerApprovalMenu {

    public static void showKickPlayerApproval(Player origin, Player target) {
        new Menu(CorePlugin.getInstance(), ChatColor.RED + "Are you sure to kick " + target.getName() + "?", 9 * 5)
                .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
                .setKey(Blank.getLayout(),
                        new Layout('p', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemHeadCreator(CorePlugin.getInstance(), new ItemStack(Material.PLAYER_HEAD)).setOwner(target.getName()).setName(ChatColor.RED + "Are you sure to kick " + target.getName() + "?").create();
                            }

                            @Override
                            public void onClick(Player player) {

                            }
                        }),
                        new Layout('a', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.GREEN_STAINED_GLASS).setName(ChatColor.GREEN + "ACCEPT").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                target.kick();
                                ServerPlayerManagerMenu.showServerPlayerManager(origin);
                            }
                        }),
                        new Layout('d', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.RED_STAINED_GLASS).setName(ChatColor.RED + "DENY").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                SpecificPlayerManagerMenu.showSpecificPlayerManager(player, target);
                            }
                        })
                ).show(origin);
    }
}
