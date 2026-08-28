# AGENTS.md — Singularity-DevTool

## What this is
Singularity-DevTool is an in-game **debugging/admin tool plugin** for PaperMC ("a better way to debug your plugins"). Part of the Singularity Project:

| Repo | Role |
|---|---|
| `SingularityLib` | Core framework library (this tool depends on it) |
| `SingularityPlugin` | Starter template plugin |
| **Singularity-DevTool** (this repo) | In-game dev tools: GUI menus, player/world management, custom item manager |

> **Branch note:** active development happens on `rework/v2` (v2 line). Never push `main`.

## ⚠️ Rewrite note (from owner)
The owner's plan says to "rework DevTool from Kotlin back to Java." **Fact-check result: there is no Kotlin in this repo.** All 26 source files are `.java`; no `.kt` files exist anywhere in the full git history, branches, or the pom (no Kotlin Maven plugin). The Kotlin→Java rewrite requirement appears already satisfied or was based on a misremembering. Confirm with the owner before planning a port; treat "pure Java" as the standing rule for new code either way.

## Build & run
```bash
mvn clean package          # shaded jar into target/ (JDK 25 required)
```
- Requires **JDK 25** (`java.version=25`), targets **Paper API `26.2.build.N-stable`**.
- Depends on `io.github.pinont:singularitylib:2.0.0-SNAPSHOT` via the `${singularity.version}` property — pinned to the lib's v2 line.
- Resolves SingularityLib from the **public registry** (`https://raw.githubusercontent.com/Pinont/singularity-maven/gh-pages/`) — no auth required; Paper from `repo.papermc.io`. Source compiles against lib `2.0.0-SNAPSHOT` from this registry as of the v2 API migration (see below).
- **Bootstrap model:** SingularityLib is NOT bundled into this jar. Dependency scope is `provided`, and `maven-shade-plugin` `artifactSet` excludes `com.github.pinont:SingularityLib`. At runtime the lib ships as its own plugin on the server.

## Code layout
Root package: `com.github.pinont.devtool` (26 files)

- `DevTool.java` — entry point; extends `CorePlugin`, empty `onPluginStart/Stop` (everything registers via lib mechanisms)
- `api/CItemManager.java` — wrapper over lib's CustomItemManager used by menus
- `items/Tool.java` — the dev tool item players hold to open the tool
- `commands/` — `/devtool`, `/flyspeed`, `/vanish` (`SimpleCommand` style)
- `events/ChatEvent.java` — chat capture (used by world-name input flows)
- `methods/` — one-off helpers: `CreateWorld`, `ProperWorldName`, `SendChat`, `Blank`, world-creator UI builders, `WorldDeleteButton`
- `menu/DevToolMenu.java` + `menu/submenu/*` (11 menus) — the whole GUI surface: main menu, player management (server/specific player, kick/ban approvals), world management (server worlds, single world, creator, delete approval), custom items, other tools

Resource: `src/main/resources/paper-plugin.yml` — declares `name: SingularityDevTool`, `main: com.github.pinont.devtool.DevTool`, `api-version: '26.2'`, and `folia-supported: true`.

## Conventions
- Pure Java only (no Kotlin).
- Follows lib patterns: extend `CorePlugin`, use `Menu`/`Button`/`Layout` for GUIs, commands as `SimpleCommand`.
- Class-per-action style in `methods/` — keep new utilities consistent with that granularity.

## Lib 2.0.0-SNAPSHOT API migration (2026-08, rework/v2)
Source migrated to the lib v2 signatures:
- `Menu(String title[, int size])` → `Menu(Plugin, String title[, int size])`
- `ItemCreator(Material)` / `ItemCreator(ItemStack)` → `ItemCreator(Plugin, Material)` / `ItemCreator(Plugin, ItemStack)` (+ `(Plugin, Material, int amount)` overload)
- `ItemHeadCreator(ItemStack)` → `ItemHeadCreator(Plugin, ItemStack)`
- `Layout` is now a concrete class: anonymous `new Layout(){getKey()/getButton()}` overrides replaced by `new Layout(char key, Button button)` (Button bodies moved inline unchanged)
- Plugin argument everywhere = `CorePlugin.getInstance()`
- `WorldManager.create(...)/delete(...)` were removed from the lib (marked "WIP: move to Dev tool") — world creation inlined in `methods/CreateWorld.java` and deletion inlined in `DeleteWorldApprovalMenu` using plain Bukkit, preserving the `loader` metadata contract checked by `WorldDeleteButton`

## Known issues / tech debt (observed)
1. ~~`paper-plugin.yml` name mismatch~~ — fixed on `rework/v2`: renamed to `SingularityDevTool`.
2. ~~Stale dependency version~~ — fixed on `rework/v2`: `${singularity.version}=2.0.0-SNAPSHOT` tracking the lib v2 line.
3. Chat-input flows (`ChatEvent` + `SendChat`) are fragile global state — replace with conversation API (Paper `ConversationFactory`) during rework.
4. Menus rebuild state imperatively per open; no shared pagination abstraction yet (lib roadmap item).
5. Test coverage: MockBukkit smoke + command-dispatch + registration tests exist under `src/test/java` (see `DevToolCommandTest`, `DevToolRegistrationTest`, `DevToolTest`). Deeper menu/click-simulation coverage is still thin by design (MockBukkit inventory clicks are flaky).

## Agent guidance
- New features in Java, wired through SingularityLib APIs (`CorePlugin`, `SimpleCommand`, `Menu`) rather than raw Bukkit where possible.
- Don't rename public classes casually — but DO fix `plugin.yml` naming and dependency pinning early in the rework. *(Descriptor naming + dep pinning now done on `rework/v2`.)*
- When touching menus, keep the menu → submenu navigation pattern; consider extracting shared confirmation-dialog logic (three near-identical approval menus exist).
- World creation/deletion code touches disk — always validate names (`ProperWorldName`) before use.

## v2 / bootstrap model notes
- Coordinates: `com.example:singularityplugin:1.0-SNAPSHOT` (Maven coordinates unchanged this pass — rename deferred to the DevTool v2 rebuild).
- **Provided scope rationale:** bootstrap model = SingularityLib runs as its own standalone server plugin; consumers join its classpath instead of shading it in. Bundling would duplicate classes and break shared state between plugins.
- **paper-plugin.yml conversion is DEFERRED to the DevTool v2 rebuild (Phase 4):** `paper-plugin.yml` has no `commands:` section, so commands must be registered programmatically (`LifecycleEvents.COMMANDS` / BootstrapContext API). Doing that now would mean touching every command class for zero mechanical gain; the full rebuild will handle it alongside the new command surface.
