package com.github.pinont.devtool.items;

import com.github.pinont.devtool.menu.DevToolMenu;
import com.github.pinont.singularitylib.api.annotation.AutoRegister;
import com.github.pinont.singularitylib.api.items.CustomItem;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.items.ItemInteraction;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.Set;

@AutoRegister
public class Tool extends CustomItem {
    @Override
    public ItemCreator register() {
        return new ItemCreator(Material.DIAMOND).setName(ChatColor.DARK_RED + "Developer Tool").setUnstackable(true).addInteraction(
                new ItemInteraction() {
                    @Override
                    public String getName() {
                        return "DevTool";
                    }

                    @Override
                    public Set<Action> getAction() {
                        return Set.of(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
                    }

                    @Override
                    public void execute(Player player) {
                        DevToolMenu.openDevTool(player);
                    }
                }
        );
    }

    @Override
    public ItemInteraction getInteraction() {
        return null;
    }
}
