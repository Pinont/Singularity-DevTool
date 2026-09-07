package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.menu.submenu.WorldCreatorMenu;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

/**
 * World-creator chat prompts, backed by {@link StartConversation}.
 */
public final class PromptWorldInput {

    private PromptWorldInput() {
    }

    public static void worldName(Player player, String name, World.Environment environment,
                                 WorldType worldType, boolean generateStructure, int borderSize,
                                 Difficulty difficulty, Long seed) {
        StartConversation.ask(player,
                "Please send a world name into chat (or type cancel).",
                input -> WorldCreatorMenu.showWorldCreator(player, input, environment, worldType,
                        generateStructure, borderSize, difficulty, seed),
                () -> reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed));
    }

    public static void worldBorder(Player player, String name, World.Environment environment,
                                   WorldType worldType, boolean generateStructure, int borderSize,
                                   Difficulty difficulty, Long seed) {
        StartConversation.ask(player,
                "Please send a world border size into chat (or type cancel).",
                input -> {
                    try {
                        int parsed = Integer.parseInt(input);
                        if (parsed <= 0) {
                            player.sendMessage(ChatColor.RED + "World border size must be greater than 0");
                            reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed);
                            return;
                        }
                        WorldCreatorMenu.showWorldCreator(player, name, environment, worldType,
                                generateStructure, parsed, difficulty, seed);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "World border size must be a number.");
                        reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed);
                    }
                },
                () -> reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed));
    }

    public static void worldSeed(Player player, String name, World.Environment environment,
                                 WorldType worldType, boolean generateStructure, int borderSize,
                                 Difficulty difficulty, Long seed) {
        StartConversation.ask(player,
                "Please send a seed number into chat (or type cancel).",
                input -> {
                    try {
                        long parsed = Long.parseLong(input);
                        WorldCreatorMenu.showWorldCreator(player, name, environment, worldType,
                                generateStructure, borderSize, difficulty, parsed);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "World seed must be a number.");
                        reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed);
                    }
                },
                () -> reopen(player, name, environment, worldType, generateStructure, borderSize, difficulty, seed));
    }

    private static void reopen(Player player, String name, World.Environment environment,
                               WorldType worldType, boolean generateStructure, int borderSize,
                               Difficulty difficulty, Long seed) {
        WorldCreatorMenu.showWorldCreator(player, name, environment, worldType,
                generateStructure, borderSize, difficulty, seed);
    }
}
