package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.DevTool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

/**
 * Snippet generation is pure string building over ItemStacks — no inventory clicks.
 */
public class ItemSnippetTest {

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        MockBukkit.load(DevTool.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("studio ItemCreator snippet uses lib APIs for the picked material")
    public void studioItemCreatorMentionsLibApis() {
        String snippet = ItemSnippet.studioItemCreator(Material.DIAMOND_SWORD);

        Assertions.assertTrue(snippet.contains("new ItemCreator(CorePlugin.getInstance(), Material.DIAMOND_SWORD)"),
                snippet);
        Assertions.assertTrue(snippet.contains("CorePlugin.getInstance()"), snippet);
        Assertions.assertTrue(snippet.contains("NamedTextColor.LIGHT_PURPLE"), snippet);
        Assertions.assertTrue(snippet.contains("Attributes.setAttribute"), snippet);
        Assertions.assertTrue(snippet.contains("AttributeType.ATTACK_DAMAGE"), snippet);
        Assertions.assertFalse(snippet.contains("ChatEvent"), snippet);
    }

    @Test
    @DisplayName("studio CustomItem snippet is a ready-to-paste subclass")
    public void studioCustomItemIsSubclass() {
        String snippet = ItemSnippet.studioCustomItem(Material.GOLDEN_APPLE);

        Assertions.assertTrue(snippet.contains("extends CustomItem"), snippet);
        Assertions.assertTrue(snippet.contains("class StudioGoldenApple"), snippet);
        Assertions.assertTrue(snippet.contains("public ItemCreator register()"), snippet);
        Assertions.assertTrue(snippet.contains("Material.GOLDEN_APPLE"), snippet);
        Assertions.assertTrue(snippet.contains("CorePlugin.getInstance()"), snippet);
    }

    @Test
    @DisplayName("fromItem reverse-engineers a plain ItemStack")
    public void fromItemUsesMaterialAndAmount() {
        ItemStack stack = new ItemStack(Material.STONE, 4);
        String snippet = ItemSnippet.fromItem(stack);

        Assertions.assertTrue(snippet.contains("Material.STONE"), snippet);
        Assertions.assertTrue(snippet.contains(", 4)"), snippet);
        Assertions.assertTrue(snippet.contains("new ItemCreator(CorePlugin.getInstance()"), snippet);
        Assertions.assertTrue(snippet.contains(".create();"), snippet);
    }

    @Test
    @DisplayName("fromItem on air returns a comment, not a crash")
    public void fromItemAirIsComment() {
        String snippet = ItemSnippet.fromItem(new ItemStack(Material.AIR));
        Assertions.assertTrue(snippet.startsWith("//"), snippet);
    }

    @Test
    @DisplayName("className converts material names to PascalCase")
    public void classNamePascalCase() {
        Assertions.assertEquals("StudioDiamondSword", ItemSnippet.className("Studio", "DIAMOND_SWORD"));
    }
}
