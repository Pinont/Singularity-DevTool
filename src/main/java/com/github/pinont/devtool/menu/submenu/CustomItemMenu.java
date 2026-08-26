package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.entity.Player;

public class CustomItemMenu {
    public static void showCustomItemList(Player player) {
        Menu menu = new Menu(CorePlugin.getInstance(), "Custom Items");
        menu.setLayout(
                "===================",
                "=                 =",
                "=                 =",
                "=                 =",
                "=                 =",
                "==================="
        );
        menu.setKey(Blank.getLayout());
        menu.show(player);
    }
}
