package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.EntitySnippet;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.PaginatedMenu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * DevTool v2: Entity Studio — pick an entity type and export a spawn snippet.
 */
public final class EntityStudioMenu {

    private EntityStudioMenu() {
    }

    public static void open(Player player) {
        List<EntityType> types = new ArrayList<>();
        for (EntityType type : EntityType.values()) {
            if (isStudioType(type)) {
                types.add(type);
            }
        }
        types.sort(Comparator.comparing(EntityType::name));
        new StudioMenu(types).show(player);
    }

    private static boolean isStudioType(EntityType type) {
        Class<? extends Entity> clazz = type.getEntityClass();
        if (clazz == null || Player.class.isAssignableFrom(clazz)) {
            return false;
        }
        try {
            return type.isSpawnable();
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static final class StudioMenu extends PaginatedMenu<EntityType> {

        StudioMenu(List<EntityType> types) {
            super(CorePlugin.getInstance(),
                    "Entity Studio (pick a type)",
                    54,
                    36,
                    contentSlots(),
                    types,
                    EntityStudioMenu::render,
                    EntityStudioMenu::onPick
            );
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

    private static ItemStack render(EntityType type) {
        return new ItemCreator(CorePlugin.getInstance(), eggOrPaper(type))
                .setName(Component.text(EntitySnippet.pretty(type), NamedTextColor.WHITE))
                .addLore(Component.text(type.name(), NamedTextColor.GRAY))
                .create();
    }

    private static void onPick(Player player, EntityType type) {
        EntityStudioResultMenu.open(player, type);
    }

    private static Material eggOrPaper(EntityType type) {
        Material egg = Material.matchMaterial(type.name() + "_SPAWN_EGG");
        return egg != null ? egg : Material.EGG;
    }
}
