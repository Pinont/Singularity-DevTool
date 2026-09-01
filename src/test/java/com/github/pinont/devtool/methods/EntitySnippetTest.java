package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.DevTool;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

public class EntitySnippetTest {

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
    @DisplayName("entity spawn snippet uses EntityType and CorePlugin")
    public void spawnSnippet() {
        String snippet = EntitySnippet.spawn(EntityType.ZOMBIE);

        Assertions.assertTrue(snippet.contains("EntityType.ZOMBIE"), snippet);
        Assertions.assertTrue(snippet.contains("spawnEntity"), snippet);
        Assertions.assertTrue(snippet.contains("CorePlugin.getInstance()"), snippet);
        Assertions.assertTrue(snippet.contains("NamedTextColor.AQUA"), snippet);
    }
}
