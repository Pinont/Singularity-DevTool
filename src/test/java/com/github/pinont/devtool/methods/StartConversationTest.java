package com.github.pinont.devtool.methods;

import com.github.pinont.devtool.DevTool;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Conversation start without inventory clicks: {@link StartConversation} should
 * put the player into a Paper conversation and deliver the next chat input.
 */
public class StartConversationTest {

    private ServerMock server;

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        MockBukkit.load(DevTool.class);
        server = MockBukkit.getMock();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("ask() begins a conversation on the player")
    public void askBeginsConversation() {
        Player player = server.addPlayer("Pinont");
        Assertions.assertFalse(player.isConversing());

        StartConversation.ask(player, "Please send a world name into chat (or type cancel).",
                input -> {
                },
                () -> {
                });

        Assertions.assertTrue(player.isConversing(), "player should be in a ConversationFactory prompt");
    }

    @Test
    @DisplayName("acceptConversationInput delivers the typed value")
    public void inputIsDelivered() {
        Player player = server.addPlayer("Pinont");
        AtomicReference<String> captured = new AtomicReference<>();

        StartConversation.ask(player, "Please send a world name into chat (or type cancel).",
                captured::set,
                () -> {
                });
        Assertions.assertTrue(player.isConversing());

        player.acceptConversationInput("qa_world");

        Assertions.assertEquals("qa_world", captured.get(), "conversation should deliver the typed world name");
        Assertions.assertFalse(player.isConversing(), "conversation should end after a valid answer");
    }

    @Test
    @DisplayName("paginate splits a long snippet into book-sized pages")
    public void paginateSplits() {
        String text = "a".repeat(500);
        var pages = ExportSnippet.paginate(text, 240);
        Assertions.assertEquals(3, pages.size());
        Assertions.assertEquals(240, pages.get(0).length());
        Assertions.assertEquals(20, pages.get(2).length());
    }
}
