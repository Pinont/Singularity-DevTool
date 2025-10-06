package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.singularitylib.api.items.ItemHeadCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Shows the server player manager interface.
 */
public class ServerPlayerManagerMenu {

    public static void showServerPlayerManager(Player origin) {
        Menu playerManager = new Menu("Player Manager", 9); // temp
        for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
            Player player = (Player) Bukkit.getOnlinePlayers().toArray()[i];
            int finalI = i;
            playerManager.addButton(new Button() {
                @Override
                public int getSlot() {
                    return finalI;
                }

                @Override
                public ItemStack getItem() {
                    return new ItemHeadCreator(new ItemStack(Material.PLAYER_HEAD)).setOwner(player.getName()).setName(player.getName()).create();
                }

                @Override
                public void onClick(Player player) {
                    SpecificPlayerManagerMenu.showSpecificPlayerManager(origin, player);
                }
            });
        }
        playerManager.show(origin);
    }
}
