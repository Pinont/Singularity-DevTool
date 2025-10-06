package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.menu.submenu.WorldCreatorMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import static com.github.pinont.singularitylib.plugin.CorePlugin.getInstance;

/**
 * Handles player chat events for world creator input.
 * Processes user input for world creation parameters when players are in world creator mode.
 */
public class SendChat {

    /**
     * Handles player chat events for world creator input.
     * Processes user input for world creation parameters when players are in world creator mode.
     *
     * @param event the player chat event
     */
    public static void sendChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("devTool")) {
            WorldCreatorContent worldCreatorContent = (WorldCreatorContent) player.getMetadata("devTool").getFirst().value();
            if (worldCreatorContent.getInputContent() == null) {
                player.sendMessage(ChatColor.RED + "Seem like devTool World creator has occur an error.");
                return;
            }
            event.setCancelled(true);
            switch (worldCreatorContent.getInputContent()) {
                case "worldName": {
                    WorldCreatorMenu.showWorldCreator(player, event.getMessage(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), worldCreatorContent.getBorderSize(), worldCreatorContent.getDifficulty(), worldCreatorContent.getSeed());
                    break;
                }
                case "worldBorder": {
                    try {
                        int borderSize = Integer.parseInt(event.getMessage());
                        if (borderSize <= 0) {
                            player.sendMessage(ChatColor.RED + "World border size must be greater than 0");
                            WorldCreatorMenu.showWorldCreator(player, worldCreatorContent.getWorldName(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), worldCreatorContent.getBorderSize(), worldCreatorContent.getDifficulty(), worldCreatorContent.getSeed());
                            return;
                        }
                        WorldCreatorMenu.showWorldCreator(player, worldCreatorContent.getWorldName(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), borderSize, worldCreatorContent.getDifficulty(), worldCreatorContent.getSeed());
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "World border size must be a number.");
                        WorldCreatorMenu.showWorldCreator(player, worldCreatorContent.getWorldName(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), worldCreatorContent.getBorderSize(), worldCreatorContent.getDifficulty(), worldCreatorContent.getSeed());
                    }
                    break;
                }
                case "worldSeed": {
                    try {
                        long seed = Long.parseLong(event.getMessage());
                        WorldCreatorMenu.showWorldCreator(player, worldCreatorContent.getWorldName(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), worldCreatorContent.getBorderSize(), worldCreatorContent.getDifficulty(), seed);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "World border size must be a number.");
                        WorldCreatorMenu.showWorldCreator(player, worldCreatorContent.getWorldName(), worldCreatorContent.getEnvironment(), worldCreatorContent.getWorldType(), worldCreatorContent.getGenerateStructure(), worldCreatorContent.getBorderSize(), worldCreatorContent.getDifficulty(), worldCreatorContent.getSeed());
                    }
                    break;
                }
            }
            if (player.hasMetadata("devTool")) {
                player.removeMetadata("devTool", getInstance());
            }
        }
    }
}
