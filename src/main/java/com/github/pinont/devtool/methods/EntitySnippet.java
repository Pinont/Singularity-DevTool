package com.github.pinont.devtool.methods;

import org.bukkit.entity.EntityType;

import java.util.Locale;

/**
 * Builds ready-to-paste Java that spawns the entity the studio built.
 */
public final class EntitySnippet {

    private EntitySnippet() {
    }

    public static String spawn(EntityType type) {
        String pretty = pretty(type);
        return """
                import com.github.pinont.singularitylib.plugin.CorePlugin;
                import net.kyori.adventure.text.Component;
                import net.kyori.adventure.text.format.NamedTextColor;
                import org.bukkit.entity.Entity;
                import org.bukkit.entity.EntityType;
                import org.bukkit.entity.Player;

                // Paste inside a player-scoped method (Player player = ...).
                Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.%s);
                entity.customName(Component.text("Studio: %s", NamedTextColor.AQUA));
                entity.setCustomNameVisible(true);
                // CorePlugin.getInstance() is available if you need to schedule a follow-up:
                // CorePlugin.getInstance().getServer();
                """.formatted(type.name(), ItemSnippet.javaString(pretty));
    }

    public static String pretty(EntityType type) {
        String n = type.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }
}
