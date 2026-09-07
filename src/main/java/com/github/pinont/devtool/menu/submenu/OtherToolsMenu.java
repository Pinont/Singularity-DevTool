package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.devtool.methods.ExportSnippet;
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
 * Shows other development tools in a menu interface.
 */
public class OtherToolsMenu {

    public static void showOtherTools(Player player) {
        new Menu(CorePlugin.getInstance(), "Heldable Tool", 9 * 3)
                .setLayout("=========", "==m=w=o==", "=========")
                .setKey(
                        Blank.getLayout(),
                        new Layout('m', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.ZOMBIE_HEAD)
                                        .setName(Component.text("Entity Studio", NamedTextColor.AQUA))
                                        .addLore(Component.text("Pick an entity and export a spawn snippet",
                                                NamedTextColor.GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                EntityStudioMenu.open(p);
                            }
                        }),
                        new Layout('o', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.ITEM_FRAME)
                                        .setName(Component.text("Item Studio", NamedTextColor.LIGHT_PURPLE))
                                        .addLore(Component.text("Build an item and export Java", NamedTextColor.GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                ItemStudioMenu.open(p);
                            }
                        }),
                        new Layout('w', new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(CorePlugin.getInstance(), Material.WRITABLE_BOOK)
                                        .setName(Component.text("Export held item", NamedTextColor.YELLOW))
                                        .addLore(Component.text("Snippet from the item in your hand",
                                                NamedTextColor.GRAY))
                                        .create();
                            }

                            @Override
                            public void onClick(Player p) {
                                ExportSnippet.fromHeldItem(p);
                            }
                        })
                ).show(player);
    }
}
