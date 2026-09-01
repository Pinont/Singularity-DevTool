package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.ExportSnippet;
import com.github.pinont.singularitylib.api.items.Attributes;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.PaginatedMenu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * DevTool v2: Item Studio — a paginated material picker that lets a dev "try
 * create stuff": pick a material, get a pre-built item with the lib's ItemCreator
 * + Attributes, then export a ready-to-paste Java snippet.
 *
 * <p>Demonstrates the lib's PaginatedMenu + Attributes + ItemCreator toolkit.
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

    static ItemStack buildStudioItem(Material m) {
        ItemStack base = new ItemCreator(CorePlugin.getInstance(), m)
                .setName(Component.text("Studio: " + format(m), NamedTextColor.LIGHT_PURPLE))
                .addLore(Component.text("Made in the DevTool Item Studio", NamedTextColor.GRAY))
                .create();
        return Attributes.setAttribute(base,
                com.github.pinont.singularitylib.api.enums.AttributeType.ATTACK_DAMAGE,
                5.0,
                AttributeModifier.Operation.ADD_NUMBER);
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
            addButton(new Button() {
                @Override public int getSlot() { return 45; }
                @Override public ItemStack getItem() {
                    return new ItemCreator(CorePlugin.getInstance(), Material.WRITABLE_BOOK)
                            .setName(Component.text("Export held item", NamedTextColor.AQUA))
                            .addLore(Component.text("Snippet from the item in your hand", NamedTextColor.GRAY))
                            .create();
                }
                @Override public void onClick(Player p) {
                    ExportSnippet.fromHeldItem(p);
                }
            });
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
            for (int i = 0; i < 36; i++) {
                s[i] = 9 + i;
            }
            return s;
        }
    }

    private static ItemStack renderMaterial(Material m) {
        return new ItemCreator(CorePlugin.getInstance(), m)
                .setName(Component.text(format(m), NamedTextColor.WHITE))
                .create();
    }

    private static void onPick(Player player, Material m) {
        ItemStack boosted = buildStudioItem(m);
        player.getInventory().addItem(boosted.clone());
        player.sendMessage(Component.text("Added " + format(m)
                + " to your inventory (via ItemCreator + Attributes).", NamedTextColor.GREEN));
        ItemStudioResultMenu.open(player, boosted, m);
    }

    private static String format(Material m) {
        String n = m.name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }
}
