package com.github.pinont.devtool.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * Utility methods for getting world environment blocks.
 */
public class getWorldEnvironmentBlock {

    public static ItemStack getWorldEnvironmentBlock(World world) {
        return getWorldEnvironmentBlock(world.getEnvironment());
    }

    public static ItemStack getWorldEnvironmentBlock(World.Environment worldEnv) {
        return switch (worldEnv) {
            case NORMAL -> new ItemStack(Material.GRASS_BLOCK);
            case NETHER -> new ItemStack(Material.NETHERRACK);
            case THE_END -> new ItemStack(Material.END_STONE);
            default -> new ItemStack(Material.COMMAND_BLOCK);
        };
    }
}
