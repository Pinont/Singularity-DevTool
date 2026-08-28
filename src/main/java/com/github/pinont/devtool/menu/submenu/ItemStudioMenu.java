package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.singularitylib.api.items.Attributes;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.api.ui.PaginatedMenu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * DevTool v2: Item Studio — a paginated material picker that lets a dev "try
 * create stuff": pick a material, get a pre-built item with the lib's ItemCreator
 * + Attributes, and copy the recipe to… (export snippet comes in a later pass).
 *
 * <p>Demonstrates the lib's new PaginatedMenu + Attributes + ItemCreator toolkit.
 */
public final class ItemStudioMenu {

    private ItemStudioMenu() {
    }

    public static void open(Player player) {
        List<Material> materials = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m.isItem() && !m.isAir()) {
                materials.add(m);
            }
        }
        new StudioMenu(materials).show(player);
    }

    private static final class StudioMenu extends PaginatedMenu<Material> {

        StudioMenu(List<Material> materials) {
            super(CorePlugin.getInstance(),
                    "Item Studio (pick a material)",
                    54,
                    36,
                    contentSlots(),
                    materials,
                    ItemStudioMenu::renderMaterial,
                    ItemStudioMenu::onPick
            );
            // bottom-right close
            addButton(new Button() {
                @Override public int getSlot() { return 53; }
                @Override public ItemStack getItem() {
                    return new ItemCreator(CorePlugin.getInstance(), Material.BARRIER)
                            .setName(Component.text("Close", NamedTextColor.RED)).create();
                }
                @Override public void onClick(Player p) { p.closeInventory(); }
            });
        }

        private static int[] contentSlots() {
            int[] s = new int[36];
            for (int i = 0; i < 36; i++) s[i] = 9 + i;
            return s;
        }
    }

    private static ItemStack renderMaterial(Material m) {
        return new ItemCreator(CorePlugin.getInstance(), m)
                .setName(Component.text(format(m), NamedTextColor.WHITE))
                .create();
    }

    private static void onPick(Player player, Material m) {
        // "try create stuff": give a starter item with a couple of attributes
        ItemStack base = new ItemCreator(CorePlugin.getInstance(), m)
                .setName(Component.text("Studio: " + format(m), NamedTextColor.LIGHT_PURPLE))
                .addLore(Component.text("Made in the DevTool Item Studio", NamedTextColor.GRAY))
                .create();
        // Add a generic damage/armor attribute where applicable (harmless if unsupported)
        ItemStack boosted = Attributes.setAttribute(base,
                com.github.pinont.singularitylib.api.enums.AttributeType.ATTACK_DAMAGE,
                5.0,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER);
        player.getInventory().addItem(boosted);
        player.sendMessage(Component.text("Added " + format(m) + " to your inventory (via ItemCreator + Attributes).", NamedTextColor.GREEN));
    }

    private static String format(Material m) {
        String n = m.name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }
}