package com.github.pinont.devtool.menu.submenu;

import com.github.pinont.devtool.methods.Blank;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.entity.Player;

/**
 * Shows other development tools in a menu interface.
 */
public class OtherToolsMenu {

    public static void showOtherTools(Player player) {
        new Menu("Heldable Tool", 9 * 3).setLayout("=========", "==m=w=o==", "=========").setKey(
                Blank.getLayout(),
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'm'; // mobCreator
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'o'; // itemCreator
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'w'; // later
                    }

                    @Override
                    public Button getButton() {
                        return null;
                    }
                }
        ).show(player);
    }
}
