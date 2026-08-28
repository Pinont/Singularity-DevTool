# Singularity DevTool Plugin
[![](https://img.shields.io/github/license/Pinont/Singularity-DevTool)](https://github.com/Pinont/Singularity-DevTool/blob/main/LICENSE)

A Minecraft plugin that introduces a better way to debug your plugins.

## Requirements
- **Paper 26.2+** or **Folia 26.x** (JDK 25)
- **SingularityLib** installed as a server plugin (bootstrap model — see below)

## Build
```bash
mvn clean package     # requires JDK 25
```
Place the jar from `target/` into your server's `plugins/` folder.

## Dependencies
Built on **SingularityLib** via the bootstrap plugin model — the lib runs as its own
plugin; DevTool compiles against it with `provided` scope (never bundled):

```xml
<dependency>
    <groupId>io.github.pinont</groupId>
    <artifactId>singularitylib</artifactId>
    <version>2.0.0</version>
    <scope>provided</scope>
</dependency>
```

```yaml
# paper-plugin.yml
dependencies:
  server:
    SingularityLib:
      load: BEFORE
      required: true
      join-classpath: true
```

Get the latest lib release from
[central.sonatype.com/artifact/io.github.pinont/singularitylib](https://central.sonatype.com/artifact/io.github.pinont/singularitylib),
or dev snapshots from https://maven.pinont.me.

## Development roadmap (v2)
DevTool v2 becomes an in-server IDE for Singularity plugins: auto-discovers all loaded
Singularity plugins, bridges their commands under `/devtool <plugin> <cmd>`, GUI config
editing with live reload, item/entity inspectors, and builders that export code snippets.
See the repo's AGENTS.md for the current state.