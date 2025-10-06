package com.github.pinont.devtool.utils;

import com.github.pinont.devtool.menu.showDeleteWorldApproval;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Creates a delete button for world management.
 */
public class worldDeleteButton {

    public static Layout worldDeleteButton(World world) {
        if (world.hasMetadata("loader")) {
            return new Layout() {
                @Override
                public char getKey() {
                    return 'd';
                }

                @Override
                public Button getButton() {
                    return new Button() {
                        @Override
                        public ItemStack getItem() {
                            return new ItemCreator(Material.RED_STAINED_GLASS).setName(ChatColor.RED +"Delete").addLore(ChatColor.RED + "Click here to delete this world").create();
                        }

                        @Override
                        public void onClick(Player player) {
                            showDeleteWorldApproval.showDeleteWorldApproval(player, world);
                        }
                    };
                }
            };
        }
        return new Layout() {
            @Override
            public char getKey() {
                return 'd';
            }

            @Override
            public Button getButton() {
                return new Button() {
                    @Override
                    public ItemStack getItem() {
                        return new ItemCreator(Material.AIR).create();
                    }

                    @Override
                    public void onClick(Player player) {

                    }
                };
            }
        };
    }
}
