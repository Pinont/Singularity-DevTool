package com.github.pinont.devtool.utils;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Shows the world creator interface.
 */
public class showWorldCreator {

    public static void showWorldCreator(Player player) {
        getWorldCreatorMenu.getWorldCreatorMenu(null, World.Environment.NORMAL, WorldType.NORMAL, true, 1000, Difficulty.EASY, new Random().nextLong(System.currentTimeMillis())).show(player);
    }

    /**
     * Shows the world creator interface with specified parameters.
     *
     * @param player the player to show the interface to
     * @param world_name the name of the world to create
     * @param environment the world environment type
     * @param worldType the world type
     * @param generate_structure whether to generate structures
     * @param borderSize the world border size
     * @param difficulty the world difficulty
     * @param seed the world seed
     */
    public static void showWorldCreator(Player player, String world_name, World.Environment environment, WorldType worldType, boolean generate_structure, int borderSize, Difficulty difficulty, long seed) {
        getWorldCreatorMenu.getWorldCreatorMenu(world_name, environment, worldType, generate_structure, borderSize, difficulty, seed).show(player);
    }
}
