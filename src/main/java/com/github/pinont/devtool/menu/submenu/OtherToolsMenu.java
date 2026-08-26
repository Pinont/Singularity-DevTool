package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.entity.Player;

/**
 * Shows other development tools in a menu interface.
 */
public class OtherToolsMenu {

    public static void showOtherTools(Player player) {
        new Menu(CorePlugin.getInstance(), "Heldable Tool", 9 * 3).setLayout("=========", "==m=w=o==", "=========").setKey(
                Blank.getLayout(),
                new Layout('m', null), // mobCreator
                new Layout('o', null), // itemCreator
                new Layout('w', null)  // later
        ).show(player);
    }
}
