package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.devtool.methods.EntitySnippet;
import com.github.pinont.devtool.methods.ExportSnippet;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Result screen after picking an entity type: export a spawn snippet.
 */
public final class EntityStudioResultMenu {

    private EntityStudioResultMenu() {
    }

    public static void open(Player player, EntityType type) {
        Menu menu = new Menu(CorePlugin.getInstance(), "Entity Studio Result", 27)
                .setLayout("=========", "==p=e=x==", "====b====")
                .setKey(
                        Blank.getLayout(),
                        new Layout('p', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), spawnEgg(type))
                                        .setName(Component.text("Studio: " + EntitySnippet.pretty(type),
                                                NamedTextColor.AQUA))
                                        .addLore(Component.text(type.name(), NamedTextColor.GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                            }
                        }),
                        new Layout('e', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.WRITABLE_BOOK)
                                        .setName(Component.text("Export spawn snippet", NamedTextColor.AQUA))
                                        .addLore(Component.text("Ready-to-paste Java", NamedTextColor.GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                ExportSnippet.toPlayer(p, "Entity spawn snippet", EntitySnippet.spawn(type));
                            }
                        }),
                        new Layout('x', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.BARRIER)
                                        .setName(Component.text("Close", NamedTextColor.RED))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                p.closeInventory();
                            }
                        }),
                        new Layout('b', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.ARROW)
                                        .setName(Component.text("Back", NamedTextColor.YELLOW))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                EntityStudioMenu.open(p);
                            }
                        })
                );
        menu.show(player);
    }

    private static Material spawnEgg(EntityType type) {
        try {
            Material egg = Material.matchMaterial(type.name() + "_SPAWN_EGG");
            return egg != null ? egg : Material.EGG;
        } catch (IllegalArgumentException e) {
            return Material.EGG;
        }
    }
}
