package com.github.pinont.devtool.methods;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;

/**
 * Interface for capturing world creator content and user input.
 * Used to maintain state during world creation process when players input values via chat.
 */
public interface WorldCreatorContent {
    /**
     * Gets the type of input content being processed.
     *
     * @return the input content type
     */
    String getInputContent();

    /**
     * Gets the name of the world being created.
     *
     * @return the world name
     */
    String getWorldName();

    /**
     * Gets the world environment type.
     *
     * @return the world environment
     */
    World.Environment getEnvironment();

    /**
     * Gets the world type for generation.
     *
     * @return the world type
     */
    WorldType getWorldType();

    /**
     * Gets whether structures should be generated in the world.
     *
     * @return true if structures should be generated, false otherwise
     */
    boolean getGenerateStructure();

    /**
     * Gets the world border size.
     *
     * @return the border size in blocks
     */
    int getBorderSize();

    /**
     * Gets the world difficulty setting.
     *
     * @return the difficulty level
     */
    Difficulty getDifficulty();

    /**
     * Gets the world seed for generation.
     *
     * @return the world seed
     */
    Long getSeed();
}
