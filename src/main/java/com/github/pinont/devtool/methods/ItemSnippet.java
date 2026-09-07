package com.github.pinont.devtool.methods;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Builds ready-to-paste Java that reconstructs an item with SingularityLib APIs.
 */
public final class ItemSnippet {

    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    private ItemSnippet() {
    }

    /**
     * Snippet matching what Item Studio actually builds for {@code material}.
     */
    public static String studioItemCreator(Material material) {
        String pretty = pretty(material);
        StringBuilder sb = new StringBuilder();
        appendImports(sb, false);
        sb.append('\n');
        sb.append("ItemStack item = new ItemCreator(CorePlugin.getInstance(), Material.")
                .append(material.name()).append(")\n");
        sb.append("        .setName(Component.text(\"Studio: ").append(javaString(pretty))
                .append("\", NamedTextColor.LIGHT_PURPLE))\n");
        sb.append("        .addLore(Component.text(\"Made in the DevTool Item Studio\", NamedTextColor.GRAY))\n");
        sb.append("        .create();\n");
        sb.append("item = Attributes.setAttribute(item, AttributeType.ATTACK_DAMAGE, 5.0,\n");
        sb.append("        AttributeModifier.Operation.ADD_NUMBER);\n");
        return sb.toString();
    }

    /**
     * Full {@code CustomItem} subclass wrapping the studio item.
     */
    public static String studioCustomItem(Material material) {
        String className = className("Studio", material.name());
        String pretty = pretty(material);
        StringBuilder sb = new StringBuilder();
        appendImports(sb, true);
        sb.append('\n');
        sb.append("public class ").append(className).append(" extends CustomItem {\n\n");
        sb.append("    @Override\n");
        sb.append("    public ItemCreator register() {\n");
        sb.append("        return new ItemCreator(CorePlugin.getInstance(), Material.")
                .append(material.name()).append(")\n");
        sb.append("                .setName(Component.text(\"Studio: ").append(javaString(pretty))
                .append("\", NamedTextColor.LIGHT_PURPLE))\n");
        sb.append("                .addLore(Component.text(\"Made in the DevTool Item Studio\", NamedTextColor.GRAY));\n");
        sb.append("    }\n\n");
        sb.append("    @Override\n");
        sb.append("    public ItemInteraction getInteraction() {\n");
        sb.append("        return null;\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Best-effort reverse-engineer of an arbitrary ItemStack.
     */
    public static String fromItem(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return "// No item to export.\n";
        }
        Material material = item.getType();
        StringBuilder sb = new StringBuilder();
        appendImports(sb, false);
        sb.append('\n');
        if (item.getAmount() != 1) {
            sb.append("ItemStack item = new ItemCreator(CorePlugin.getInstance(), Material.")
                    .append(material.name()).append(", ").append(item.getAmount()).append(")\n");
        } else {
            sb.append("ItemStack item = new ItemCreator(CorePlugin.getInstance(), Material.")
                    .append(material.name()).append(")\n");
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) {
                String name = PLAIN.serialize(meta.displayName());
                sb.append("        .setName(Component.text(\"").append(javaString(name)).append("\"))\n");
            }
            if (meta.hasLore()) {
                List<Component> lore = meta.lore();
                if (lore != null) {
                    for (Component line : lore) {
                        sb.append("        .addLore(Component.text(\"")
                                .append(javaString(PLAIN.serialize(line))).append("\"))\n");
                    }
                }
            }
            if (meta.isUnbreakable()) {
                sb.append("        .setUnbreakable(true)\n");
            }
            for (Map.Entry<Enchantment, Integer> ench : meta.getEnchants().entrySet()) {
                String key = ench.getKey().getKey().getKey();
                sb.append("        .addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(\"")
                        .append(javaString(key)).append("\")), ").append(ench.getValue())
                        .append(", true)\n");
            }
        }
        sb.append("        .create();\n");
        return sb.toString();
    }

    public static String pretty(Material material) {
        String n = material.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }

    public static String className(String prefix, String raw) {
        StringBuilder sb = new StringBuilder(prefix);
        for (String part : raw.toLowerCase(Locale.ROOT).split("[^a-z0-9]+")) {
            if (part.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    static String javaString(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private static void appendImports(StringBuilder sb, boolean customItem) {
        sb.append("import com.github.pinont.singularitylib.api.enums.AttributeType;\n");
        sb.append("import com.github.pinont.singularitylib.api.items.Attributes;\n");
        sb.append("import com.github.pinont.singularitylib.api.items.ItemCreator;\n");
        if (customItem) {
            sb.append("import com.github.pinont.singularitylib.api.items.CustomItem;\n");
            sb.append("import com.github.pinont.singularitylib.api.items.ItemInteraction;\n");
        }
        sb.append("import com.github.pinont.singularitylib.plugin.CorePlugin;\n");
        sb.append("import net.kyori.adventure.text.Component;\n");
        sb.append("import net.kyori.adventure.text.format.NamedTextColor;\n");
        sb.append("import org.bukkit.Material;\n");
        sb.append("import org.bukkit.NamespacedKey;\n");
        sb.append("import org.bukkit.attribute.AttributeModifier;\n");
        sb.append("import org.bukkit.enchantments.Enchantment;\n");
        sb.append("import org.bukkit.inventory.ItemStack;\n");
    }
}
