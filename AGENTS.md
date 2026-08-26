# AGENTS.md — Singularity-DevTool

## What this is
Singularity-DevTool is an in-game **debugging/admin tool plugin** for PaperMC ("a better way to debug your plugins"). Part of the Singularity Project:

| Repo | Role |
|---|---|
| `SingularityLib` | Core framework library (this tool depends on it) |
| `SingularityPlugin` | Starter template plugin |
| **Singularity-DevTool** (this repo) | In-game dev tools: GUI menus, player/world management, custom item manager |

## ⚠️ Rewrite note (from owner)
The owner's plan says to "rework DevTool from Kotlin back to Java." **Fact-check result: there is no Kotlin in this repo.** All 26 source files are `.java`; no `.kt` files exist anywhere in the full git history, branches, or the pom (no Kotlin Maven plugin). The Kotlin→Java rewrite requirement appears already satisfied or was based on a misremembering. Confirm with the owner before planning a port; treat "pure Java" as the standing rule for new code either way.

## Build & run
```bash
mvn clean package          # shaded jar into target/
```
- Requires **JDK 23**, targets **Paper API 1.21.8-R0.1-SNAPSHOT**.
- Depends on `com.github.pinont:SingularityLib:${singularity.version}` (**pinned to 1.0.0** via property — stale vs lib's 1.3.x line; update when the lib releases).
- Resolves SingularityLib from JitPack; Paper from `repo.papermc.io`.

## Code layout
Root package: `com.github.pinont.devtool` (26 files)

- `DevTool.java` — entry point; extends `CorePlugin`, empty `onPluginStart/Stop` (everything registers via lib mechanisms)
- `api/CItemManager.java` — wrapper over lib's CustomItemManager used by menus
- `items/Tool.java` — the dev tool item players hold to open the tool
- `commands/` — `/devtool`, `/flyspeed`, `/vanish` (`SimpleCommand` style)
- `events/ChatEvent.java` — chat capture (used by world-name input flows)
- `methods/` — one-off helpers: `CreateWorld`, `ProperWorldName`, `SendChat`, `Blank`, world-creator UI builders, `WorldDeleteButton`
- `menu/DevToolMenu.java` + `menu/submenu/*` (11 menus) — the whole GUI surface: main menu, player management (server/specific player, kick/ban approvals), world management (server worlds, single world, creator, delete approval), custom items, other tools

Resource: `src/main/resources/plugin.yml` — **note: declares `name: SingularityPlugin`** and `main: com.github.pinont.devtool.DevTool`. The name is a copy-paste leftover from the template; should be renamed during rework.

## Conventions
- Pure Java only (no Kotlin).
- Follows lib patterns: extend `CorePlugin`, use `Menu`/`Button`/`Layout` for GUIs, commands as `SimpleCommand`.
- Class-per-action style in `methods/` — keep new utilities consistent with that granularity.

## Known issues / tech debt (observed)
1. `plugin.yml` name mismatch (`SingularityPlugin` instead of something like `SingularityDevTool`).
2. Stale dependency version (`singularity.version=1.0.0`) while lib is at 1.3.x.
3. Chat-input flows (`ChatEvent` + `SendChat`) are fragile global state — replace with conversation API (Paper `ConversationFactory`) during rework.
4. Menus rebuild state imperatively per open; no shared pagination abstraction yet (lib roadmap item).
5. No tests at all.

## Agent guidance
- New features in Java, wired through SingularityLib APIs (`CorePlugin`, `SimpleCommand`, `Menu`) rather than raw Bukkit where possible.
- Don't rename public classes casually — but DO fix `plugin.yml` naming and dependency pinning early in the rework.
- When touching menus, keep the menu → submenu navigation pattern; consider extracting shared confirmation-dialog logic (three near-identical approval menus exist).
- World creation/deletion code touches disk — always validate names (`ProperWorldName`) before use.
