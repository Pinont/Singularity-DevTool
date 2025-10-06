package com.github.pinont.devtool.utils;

import org.bukkit.World;

/**
 * Utility method for formatting world names properly.
 */
public class properWorldName {

    public static String properWorldName(World world) {
        String formattedName = world.getName().replace("_", " ");
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : formattedName.toCharArray()) {
            if (capitalizeNext && Character.isLetter(c)) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
            if (c == ' ') {
                capitalizeNext = true;
            }
        }

        return result.toString();
    }
}
