package com.github.pinont.devtool.utils;

import com.github.pinont.singularitylib.api.manager.WorldManager;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;

/**
 * Creates a new world with specified parameters.
 */
public class createWorld {

    public static void createWorld(String name, World.Environment environment, WorldType worldType, boolean generate_structure, int borderSize, Difficulty difficulty, Long seed) {
        if (name == null) {
            name = "custom_world_" + environment.name() + "_" + worldType.getName() + "_" + System.currentTimeMillis();
        }
        new WorldManager(name).create(worldType, environment, generate_structure, borderSize, difficulty, seed);
    }
}
