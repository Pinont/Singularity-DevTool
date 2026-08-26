package com.github.pinont.devtool.methods;

import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Creates a new world with specified parameters.
 */
public class CreateWorld {

    public static void createWorld(String name, World.Environment environment, WorldType worldType, boolean generate_structure, int borderSize, Difficulty difficulty, Long seed) {
        if (name == null) {
            name = "custom_world_" + environment.name() + "_" + worldType.getName() + "_" + System.currentTimeMillis();
        }
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.type(worldType);
        worldCreator.generateStructures(generate_structure);
        worldCreator.environment(environment);
        if (seed != null) {
            worldCreator.seed(seed);
        }
        World world = Bukkit.createWorld(worldCreator);
        assert world != null;
        world.setDifficulty(difficulty);
        world.setGameRuleValue("doMobSpawning", "false");
        world.getWorldBorder().setSize(borderSize);
        world.setMetadata("loader", new FixedMetadataValue(CorePlugin.getInstance(), CorePlugin.getInstance().getName()));
    }
}
