package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.devtool.methods.GetWorldEnvironmentBlock;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the delete world approval confirmation dialog.
 */
public class DeleteWorldApprovalMenu {

    public static void showDeleteWorldApproval(Player player, World targetWorld) {
        new Menu(CorePlugin.getInstance(), ChatColor.RED + "Are you sure to delete " + targetWorld.getName() + "?")
                .setLayout("=========", "====w====", "=========", "==a===d==", "=========")
                .setKey(Blank.getLayout(),
                        new Layout('w', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), new ItemStack(GetWorldEnvironmentBlock.getWorldEnvironmentBlock(targetWorld))).setName(ChatColor.RED + "Are you sure to delete " + targetWorld.getName() + "?").create();
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
                                targetWorld.removeMetadata("loader", CorePlugin.getInstance());
                                targetWorld.getWorldFolder().deleteOnExit();
                                Bukkit.unloadWorld(targetWorld, false);
                                player.sendMessage(ChatColor.RED + targetWorld.getName() + " is now mark for removal!");
                                ServerWorldMangerMenu.showServerWorldManger(player);
                            }
                        }),
                        new Layout('d', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.RED_STAINED_GLASS).setName(ChatColor.RED + "DENY").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                SingleWorldManagerMenu.showSingleWorldManager(targetWorld, player);
                            }
                        })
                ).show(player);
    }
}
