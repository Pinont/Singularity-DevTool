package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.entity.Player;

public class CustomItemMenu {
    public static void showCustomItemList(Player player) {
        Menu menu = new Menu("Custom Items");
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
