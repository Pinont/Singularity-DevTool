# In-game flows

Click paths in Singularity-DevTool after
[PR #1](https://github.com/Pinont/Singularity-DevTool/pull/1) merged into
`rework/v2` at
[`72ff53e`](https://github.com/Pinont/Singularity-DevTool/commit/72ff53e941eb34636f12f54e769945abe3acebfe).

Copy-paste Java for consumers: SingularityLib
[`docs/examples/`](https://github.com/Pinont/SingularityLib/blob/main/docs/examples/README.md)
(`CommandGroup`, `ConversationFactory`, ItemCreator / CustomItem export).
`/devtool` is itself a lib `CommandGroup`
([`DevToolCommand`](../../src/main/java/com/github/pinont/devtool/commands/DevToolCommand.java),
`getName()` = `devtool:dt`).

## Conversations (World Creator + config strings)

ChatEvent / `SendChat` / `ChatPrompt` are gone. Prompts use
[`StartConversation`](../../src/main/java/com/github/pinont/devtool/methods/StartConversation.java)
(`ConversationFactory.withModality(false)`, escape sequence `cancel`, 60s
timeout) and
[`PromptWorldInput`](../../src/main/java/com/github/pinont/devtool/methods/PromptWorldInput.java).

### World name / border / seed

1. Open DevTool (`/devtool` or the tool item).
2. **Worlds** → **World Creator**.
3. Click **Set World Name**. Inventory closes. Chat shows
   `DevTool » Please send a world name into chat (or type cancel).`
4. Type a name (e.g. `qa_world`). The World Creator reopens with that name.
5. Same flow for **World Border Size** (must be a positive int) and **Seed**
   (must be a long).
6. Type `cancel` (or wait 60s) to abort and reopen the menu.

Other chat is not stolen while you are **not** in a prompt (`withModality(false)`).

### Config editor string keys

1. `/devtool config SingularityDevTool` (or any discovered Singularity plugin).
2. Click a **string** key.
3. Type the new value, or `cancel` to abort. The menu reopens with the staged
   value. Use **Save & Reload** to persist.

## Export (Item Studio / Entity Studio)

Delivery is always: click-to-copy chat line (`ClickEvent.copyToClipboard`) +
first 12 lines in chat + a written book (`author: DevTool`). See
[`ExportSnippet`](../../src/main/java/com/github/pinont/devtool/methods/ExportSnippet.java)
and the **Studio Result** buttons in
[`ItemStudioResultMenu`](../../src/main/java/com/github/pinont/devtool/menu/submenu/ItemStudioResultMenu.java).

### Item Studio → Export ItemCreator / Export CustomItem

1. `/devtool itemstudio` (or DevTool → Tools → Item Studio).
2. Pick a material (e.g. diamond sword). You get the studio item and a
   **Studio Result** menu.
3. Click **Export ItemCreator** or **Export CustomItem**.
4. Click `[Click to copy snippet]` in chat, and/or open the written book.
5. The Java uses `new ItemCreator(CorePlugin.getInstance(), Material.…)`
   (and `extends CustomItem` for the class export).

Paste those snippets into a consumer that extends `CorePlugin`. Full sample
output: lib
[export-snippets.md](https://github.com/Pinont/SingularityLib/blob/main/docs/examples/export-snippets.md).

### Held item / entity

- Hold an item → `/devtool snippet`, or Tools → **Export held item**.
- Entity path: `/devtool entitystudio` → pick a type → **Export spawn snippet**.
