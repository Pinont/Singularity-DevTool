package com.github.pinont.devtool.commands;

import com.github.pinont.devtool.items.Tool;
import com.github.pinont.devtool.menu.DevToolMenu;
import com.github.pinont.singularitylib.api.annotation.AutoRegister;
import com.github.pinont.singularitylib.api.command.SimpleCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@AutoRegister
public class DevTool implements SimpleCommand {
    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] strings) {
        if (commandSourceStack.getSender() instanceof Player player) {
            switch (strings.length) {
                case 0: {
                    DevToolMenu.openDevTool(player);
                    break;
                }
                case 1: {
                    ItemStack devToolItem = new Tool().getItem();
                    if (strings[0].equalsIgnoreCase("get") || strings[0].equalsIgnoreCase("getItem")) {
                        player.getInventory().addItem(devToolItem);
                    }
                    break;
                }
                case 3: {
                    if (strings[0].equalsIgnoreCase("world")) {
                        if (strings[1].equalsIgnoreCase("teleport")) {
                            World world = Bukkit.getWorld(strings[2]);
                            if (world != null) {
                                player.teleport(world.getSpawnLocation());
                                player.sendMessage(ChatColor.GRAY + "Teleporting to " + world.getName() + "...");
                            } else {
                                player.sendMessage(ChatColor.RED + "World not found!");
                            }
                        }
                        break;
                    }
                }
            }
            return;
        }
        commandSourceStack.getSender().sendMessage("This command can only be executed by a player!");
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] args) {
        switch (args.length) {
            case 0, 1: {
                return List.of("get", "getItem", "world");
            }
            case 2: {
                if (args[0].equalsIgnoreCase("world")) {
                    return List.of("teleport");
                }
                break;
            }
            case 3: {
                if (args[0].equalsIgnoreCase("world") && args[1].equalsIgnoreCase("teleport")) {
                    return List.of(Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new));
                }
                break;
            }
        }
        return List.of();
    }


    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return sender.hasPermission("pinont.devtool");
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "devTool";
    }

    @Override
    public String description() {
        return "SingularityLib Developer Tools";
    }
}
