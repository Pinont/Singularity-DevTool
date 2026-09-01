package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.devtool.methods.ExportSnippet;
import com.github.pinont.devtool.methods.ItemSnippet;
import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Result screen after picking a material in Item Studio: give the item and
 * export ItemCreator / CustomItem Java snippets.
 */
public final class ItemStudioResultMenu {

    private ItemStudioResultMenu() {
    }

    public static void open(Player player, ItemStack built, Material material) {
        ItemStack preview = built.clone();
        preview.setAmount(1);
        Menu menu = new Menu(CorePlugin.getInstance(), "Studio Result", 27)
                .setLayout("=========", "==p=e=c==", "====b====")
                .setKey(
                        Blank.getLayout(),
                        new Layout('p', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return preview;
                            }

                            @Override
                            public void onClick(Player p) {
                                p.getInventory().addItem(built.clone());
                                p.sendMessage(Component.text("Added the studio item to your inventory.",
                                        NamedTextColor.GREEN));
                            }
                        }),
                        new Layout('e', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.WRITABLE_BOOK)
                                        .setName(Component.text("Export ItemCreator", NamedTextColor.AQUA))
                                        .addLore(Component.text("Ready-to-paste Java snippet", NamedTextColor.GRAY),
                                                Component.text("Click to copy / book", NamedTextColor.DARK_GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                ExportSnippet.toPlayer(p, "ItemCreator snippet",
                                        ItemSnippet.studioItemCreator(material));
                            }
                        }),
                        new Layout('c', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.BOOK)
                                        .setName(Component.text("Export CustomItem", NamedTextColor.LIGHT_PURPLE))
                                        .addLore(Component.text("Full CustomItem subclass", NamedTextColor.GRAY),
                                                Component.text("Click to copy / book", NamedTextColor.DARK_GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                ExportSnippet.toPlayer(p, "CustomItem snippet",
                                        ItemSnippet.studioCustomItem(material));
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
                                ItemStudioMenu.open(p);
                            }
                        })
                );
        menu.show(player);
    }
}
