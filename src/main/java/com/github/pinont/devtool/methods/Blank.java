package com.github.pinont.devtool.methods;

import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Utility method for creating blank layout elements.
 */
public class Blank {

    public static Layout getLayout() {
        return new Layout('=', new Button() {
            @Override
            public ItemStack getItem() {
                return new ItemStack(Material.AIR);
            }

            @Override
            public void onClick(Player player) {

            }
        });
    }
}
