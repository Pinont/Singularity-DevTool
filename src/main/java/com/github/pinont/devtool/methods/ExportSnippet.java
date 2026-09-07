package com.github.pinont.devtool.methods;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Delivers a generated Java snippet to the player: click-to-copy when Paper
 * allows it, plus a written book (and a chat dump) as fallback.
 */
public final class ExportSnippet {

    /** Adventure/Paper click-event payload cap; keep well under 32k. */
    private static final int CLIPBOARD_CAP = 24_000;
    private static final int BOOK_PAGE_CHARS = 240;
    private static final int CHAT_PREVIEW_LINES = 12;

    private ExportSnippet() {
    }

    public static void toPlayer(Player player, String title, String snippet) {
        player.closeInventory();
        player.sendMessage(Component.text("—— " + title + " ——", NamedTextColor.GOLD));

        String copyPayload = snippet.length() <= CLIPBOARD_CAP
                ? snippet
                : snippet.substring(0, CLIPBOARD_CAP);
        player.sendMessage(Component.text("[Click to copy snippet]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.copyToClipboard(copyPayload))
                .hoverEvent(HoverEvent.showText(Component.text("Copy Java to clipboard", NamedTextColor.GRAY))));
        if (snippet.length() > CLIPBOARD_CAP) {
            player.sendMessage(Component.text("Snippet is long — clipboard has the first "
                    + CLIPBOARD_CAP + " chars; full text is in the book.", NamedTextColor.YELLOW));
        }

        String[] lines = snippet.split("\n", -1);
        int preview = Math.min(CHAT_PREVIEW_LINES, lines.length);
        for (int i = 0; i < preview; i++) {
            player.sendMessage(Component.text(lines[i], NamedTextColor.GRAY));
        }
        if (lines.length > CHAT_PREVIEW_LINES) {
            player.sendMessage(Component.text("… (" + (lines.length - CHAT_PREVIEW_LINES)
                    + " more lines in the book)", NamedTextColor.DARK_GRAY));
        }

        ItemStack book = writtenBook(title, snippet);
        if (book != null) {
            var leftover = player.getInventory().addItem(book);
            if (!leftover.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), book);
                player.sendMessage(Component.text("Inventory full — dropped the snippet book at your feet.",
                        NamedTextColor.YELLOW));
            } else {
                player.sendMessage(Component.text("Gave you a written book with the full snippet.",
                        NamedTextColor.GREEN));
            }
        }
    }

    public static void fromHeldItem(Player player) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType().isAir()) {
            player.sendMessage(Component.text("Hold an item to export a snippet.", NamedTextColor.RED));
            return;
        }
        toPlayer(player, "ItemCreator snippet", ItemSnippet.fromItem(held));
    }

    static ItemStack writtenBook(String title, String snippet) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null) {
            return null;
        }
        String shortTitle = title.length() > 32 ? title.substring(0, 32) : title;
        meta.setTitle(shortTitle);
        meta.setAuthor("DevTool");
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        List<Component> pages = new ArrayList<>();
        for (String page : paginate(snippet, BOOK_PAGE_CHARS)) {
            pages.add(Component.text(page));
        }
        if (pages.isEmpty()) {
            pages.add(Component.text("// empty snippet"));
        }
        meta.addPages(pages.toArray(Component[]::new));
        book.setItemMeta(meta);
        return book;
    }

    static List<String> paginate(String text, int pageSize) {
        List<String> pages = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return pages;
        }
        int i = 0;
        while (i < text.length() && pages.size() < 100) {
            int end = Math.min(text.length(), i + pageSize);
            pages.add(text.substring(i, end));
            i = end;
        }
        return pages;
    }
}
