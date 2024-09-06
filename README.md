# Deftu's Gradle Toolkit
A simple Gradle plugin ecosystem used to manage your Gradle projects — Automatically setting up project metadata, configuring your plugins, configuring your language compilers.

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/gradle/gradle-toolkit?color=c33f3f&name=Gradle+Toolkit)

[![wakatime](https://wakatime.com/badge/user/25be8ed5-7461-4fcf-93f7-0d88a7692cca/project/c079fa6c-67b4-4144-9436-fd9a37ee66b2.svg)](https://wakatime.com/badge/user/25be8ed5-7461-4fcf-93f7-0d88a7692cca/project/c079fa6c-67b4-4144-9436-fd9a37ee66b2)

---

## Usage
For the most basic use, DGT has `dev.deftu.gradle.tools`, which will automatically set up your project metadata and repositories, as well as configuring the Java and Kotlin compilers if their respective plugins are present.

### Resources plugin
The `dev.deftu.gradle.tools.resources` plugin will automatically configure the `processResources` extension in your project, replacing tokens for project metadata, Minecraft metadata and mod metadata.

#### Normal projects
- `project_name`
- `project_version`
- `project_description`
- `project_group`

#### Minecraft mods
- `mod_id`
- `mod_name`
- `mod_version`
- `mod_description`
- `mod_group`
- `file.jarVersion` (For Forge)
- `forge_loader_version` (For Forge)
- `mc_version`
- `minor_mc_version`
- `format_mc_version`
- `java_version`

### Shadow plugin
The `dev.deftu.gradle.tools.shadow` plugin will automatically configure the Shadow plugin in your project, adding a `fatJar` task which is automatically configured to work with Fabric/Architectury Loom (whereas, normal Shadow tasks don't).  
Using it is as simple as applying the plugin to your project and using the `shade` configuration for your dependencies. Be aware that `shade` does not configure the dependency for use in your project, it simply tells Shadow to include it in the JAR.

### Dokka plugin
The `dev.deftu.gradle.tools.dokka` plugin will automatically put your Dokka documentation inside of your Javadoc JAR, and requires nothing more than being applied to your project.

### GitHub publishing plugin
The `dev.deftu.gradle.tools.publishing.github` plugin will automatically configure your project to publish to GitHub releases, and has maximal configuration options. It's easiest to check the `GitHubPublishingExtension` class for all the properties.

### Maven publishing plugin
The `dev.deftu.gradle.tools.publishing.maven` plugin will automatically configure your project to publish to a Maven repository, and has maximal configuration options. It's easiest to check the `MavenPublishingExtension` class for all the properties.

### Minecraft API plugin
The `dev.deftu.gradle.tools.minecraft.api` plugin will add a `toolkitLoomApi` extension to your project, which allows you to set up a test mod environment for libraries.

### Minecraft Loom plugin
The `dev.deftu.gradle.tools.minecraft.loom` plugin will automatically configure the Loom plugin in your project, setting up the versioning of all required dependencies for the given Minecraft version and mod loader your project uses.

You can configure it to not set up certain aspects of your project with the following properties:
- `dgt.loom.minecraft.setup`
- `dgt.loom.mappings.use`
- `dgt.loom.loader.use`

An entirely different `minecraft` dependency can be configured with `dgt.loom.minecraft`.

It is also possible to pick from several options for your mappings using `dgt.loom.mappings`. The default is to use the recommended mappings set for your version and loader (f.ex, Yarn for Fabric, official for Forge 1.16+, etc). The possible options are:
- `official`/`mojang`/`mojmap` for official mappings
- `official-like` to use the most similar mappings to the official ones (MCP for legacy Forge/Fabric, official for 1.16+)
- Alternatively you can outright provide your own dependency notation.

There are also mapping flavors which you can choose from using `dgt.loom.mappings.flavor`. At the moment, the only supported flavor is `parchment`, which is available for official mappings. When used, the `parchment` flavor will only apply to Minecraft versions which support official mappings (1.16.5+).

### Dependencies

You can get versions (or full dependency notations) for multiple different loader-specific common dependencies via `mcData#dependencies`.

The return values of all of the properties inside of the `MCDependency` objects are configurable via several properties:
- `dgt.fabric.loader.version`
- `dgt.fabric.yarn.version` (also works for Legacy Fabric!)
- `dgt.fabric.api.version` (also works for Legacy Fabric!)
- `dgt.fabric.language.kotlin.version`
- `dgt.fabric.modmenu.version`
- `dgt.forge.version`
- `dgt.forge.mcp.dependency`
- `dgt.neoforge.version`

### Minecraft Releases plugin
The `dev.deftu.gradle.tools.minecraft.releases` plugin will automatically configure your project to publish to both CurseForge and Modrinth, and has maximal configuration options. It's easiest to check the `ReleasingExtension` class for all the properties.

- Publishing for Modrinth can be setup via configuring the `dgt.publish.modrinth.token` property in your global Gradle properties file and setting your project ID via `modrinth.projectId` in `toolkitReleases`
- Publishing for CurseForge can be setup via configuring the `dgt.publish.curseforge.apikey` property in your global Gradle properties file and setting your project ID via `curseforge.projectId` in `toolkitReleases`

#### How do I locate my global Gradle properties?

By default, for Windows users, they will be located at `C:\Users\YOU\.gradle\gradle.properties`. **But**, this can be configured using the `GRADLE_HOME` environment variable, so if setting these properties in the default location does not work, check if you have the environment variable set up.

---

**This project is licensed under [LGPL-3.0][lgpl3].**\
**&copy; 2024 Deftu**

[lgpl3]: https://www.gnu.org/licenses/lgpl-3.0.en.html
