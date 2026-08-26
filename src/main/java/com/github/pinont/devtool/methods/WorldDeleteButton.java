package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.menu.submenu.DeleteWorldApprovalMenu;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Creates a delete button for world management.
 */
public class WorldDeleteButton {

    public static Layout worldDeleteButton(World world) {
        if (world.hasMetadata("loader")) {
            return new Layout('d', new Button() {
                @Override
                public ItemStack getItem() {
                    return new ItemCreator(CorePlugin.getInstance(), Material.RED_STAINED_GLASS).setName(ChatColor.RED +"Delete").addLore(ChatColor.RED + "Click here to delete this world").create();
                }

                @Override
                public void onClick(Player player) {
                    DeleteWorldApprovalMenu.showDeleteWorldApproval(player, world);
                }
            });
        }
        return new Layout('d', new Button() {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(CorePlugin.getInstance(), Material.AIR).create();
            }

            @Override
            public void onClick(Player player) {

            }
        });
    }
}
