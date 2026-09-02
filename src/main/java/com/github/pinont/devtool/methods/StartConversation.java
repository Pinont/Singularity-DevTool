package com.github.pinont.devtool.methods;

import com.github.pinont.singularitylib.plugin.CorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * Starts a per-player Paper conversation prompt.
 *
 * <p>Replaces the old global {@code ChatEvent} / {@code SendChat} / {@code ChatPrompt}
 * hack: each prompt owns its own conversation state instead of stealing every
 * chat message via a plugin-wide listener.
 *
 * <p>{@code ConversationFactory} is deprecated-for-removal on Paper 26.2 in
 * favor of Dialogs, but it is still the conversation API present on this
 * target and is what MockBukkit can exercise without flaky inventory clicks.
 */
@SuppressWarnings({"deprecation", "removal"})
public final class StartConversation {

    public static final String ESCAPE = "cancel";

    private StartConversation() {
    }

    /**
     * Closes the current inventory and begins a modal-off chat prompt.
     * Type {@code cancel} (or wait out the timeout) to abort.
     *
     * @return the begun conversation (useful for tests)
     */
    public static Conversation ask(Player player, String promptText,
                                   Consumer<String> onAnswer, Runnable onCancel) {
        Plugin plugin = CorePlugin.getInstance();
        player.closeInventory();

        ConversationFactory factory = new ConversationFactory(plugin)
                .withModality(false)
                .withLocalEcho(true)
                .withTimeout(60)
                .withEscapeSequence(ESCAPE)
                .withPrefix(context -> ChatColor.GRAY + "DevTool » ")
                .thatExcludesNonPlayersWithMessage("Players only.")
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext context) {
                        return ChatColor.YELLOW + promptText;
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext context, String input) {
                        if (input == null || input.isBlank()) {
                            return this;
                        }
                        onAnswer.accept(input.trim());
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit() && onCancel != null) {
                        onCancel.run();
                    }
                });

        Conversation conversation = factory.buildConversation(player);
        conversation.begin();
        return conversation;
    }
}
