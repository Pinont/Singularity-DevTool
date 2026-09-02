package com.github.pinont.devtool.tools;

import com.github.pinont.singularitylib.api.nms.NmsBridge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * DevTool v2: vanilla NBT inspector.
 *
 * <p>{@code /devtool inspect} shows the raw vanilla NBT of the held item (via the lib's
 * {@link NmsBridge} — soft-loads SingularityNMS when present). With a {@code --entity}
 * flag it targets the entity the player is looking at instead.
 *
 * <p>This is read-only; write/edit NBT flows come in the builder pass (step 6).
 */
public final class NbtInspector {

    private NbtInspector() {
    }

    /**
     * Prints the held item's vanilla NBT to the player's chat.
     */
    public static void inspectItem(Player player) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType().isAir()) {
            player.sendMessage(Component.text("Hold an item to inspect its vanilla NBT.", NamedTextColor.RED));
            return;
        }
        player.sendMessage(Component.text("Vanilla NBT of " + held.getType(), NamedTextColor.GOLD));
        if (!NmsBridge.isAvailable()) {
            player.sendMessage(Component.text("SingularityNMS is not installed — vanilla NBT unavailable."
                    + " Install the SingularityNMS plugin to inspect raw NBT.", NamedTextColor.YELLOW));
            return;
        }
        Map<String, String> nbt = NmsBridge.readItemNbtAsMap(held);
        if (nbt.isEmpty()) {
            player.sendMessage(Component.text("(no vanilla custom_data keys)", NamedTextColor.GRAY));
            return;
        }
        for (Map.Entry<String, String> e : nbt.entrySet()) {
            player.sendMessage(Component.text("  " + e.getKey() + ": ", NamedTextColor.WHITE)
                    .append(Component.text(e.getValue(), NamedTextColor.AQUA)));
        }
    }

    /**
     * Prints the targeted entity's NBT (best-effort; entity tags via CraftEntity).
     */
    public static void inspectEntity(Player player) {
        org.bukkit.entity.Entity target = player.getTargetEntity(8);
        if (target == null) {
            player.sendMessage(Component.text("Look at an entity within 8 blocks to inspect it.", NamedTextColor.RED));
            return;
        }
        player.sendMessage(Component.text("Entity: " + target.getType() + " (" + target.getUniqueId() + ")", NamedTextColor.GOLD));
        if (!NmsBridge.isAvailable()) {
            player.sendMessage(Component.text("SingularityNMS is not installed — entity NBT unavailable.", NamedTextColor.YELLOW));
            return;
        }
        // Entity NBT read via the NMS NbtCompound wrapper (entity.save path) — 
        // best-effort through the bridge's raw read; extended in step 6.
        player.sendMessage(Component.text("(entity NBT dump coming via NMS Entities facade)", NamedTextColor.GRAY));
    }
}