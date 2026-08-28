package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.singularitylib.api.manager.CommentConfigManager;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DevTool: minimal chat-prompt for editing string config values.
 *
 * <p>Used by {@link ConfigEditorMenu} — a player clicks a string key, the editor asks
 * them to type a value, and the next chat message is captured (then re-opens the menu).
 * This is a scoped, per-player conversation (no global chat hijacking like the old
 * DevTool ChatEvent). It replaces the previous all-messages-are-input approach with a
 * strict one-shot prompt.
 */
public final class ChatPrompt implements Listener {

    /** pending prompts: player -> (plugin, key, manager, menuToReopen) */
    private static final Map<UUID, Pending> PENDING = new ConcurrentHashMap<>();

    public ChatPrompt() {
    }

    public static void setPending(CorePlugin plugin, Player player, String key,
                                  CommentConfigManager manager, ConfigEditorMenu.Editor editor) {
        PENDING.put(player.getUniqueId(), new Pending(plugin, key, manager, editor));
    }

    public static boolean hasPending(Player player) {
        return PENDING.containsKey(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        Pending pending = PENDING.remove(event.getPlayer().getUniqueId());
        if (pending == null) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        String input = event.getMessage().trim();
        if (input.equalsIgnoreCase("cancel")) {
            player.sendMessage(Component.text("Edit cancelled.", NamedTextColor.GRAY));
            pending.editor.show(player);
            return;
        }
        pending.manager.set(pending.key, input);
        player.sendMessage(Component.text(pending.key + " -> '" + input + "' (use Save to persist).", NamedTextColor.GREEN));
        // Re-open on the main thread (chat is async)
        org.bukkit.Bukkit.getScheduler().runTask(pending.plugin, () -> pending.editor.show(player));
    }

    private record Pending(CorePlugin plugin, String key, CommentConfigManager manager,
                           ConfigEditorMenu.Editor editor) {
    }
}