# Deftu's Gradle Toolkit
A simple Gradle plugin ecosystem used to manage your Gradle projects — Automatically setting up project metadata, configuring your plugins, configuring your language compilers.

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/gradle/gradle-toolkit?color=c33f3f&name=Gradle+Toolkit)

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

### Minecraft Releases plugin
The `dev.deftu.gradle.tools.minecraft.releases` plugin will automatically configure your project to publish to both CurseForge and Modrinth, and has maximal configuration options. It's easiest to check the `ReleasingExtension` class for all the properties.

---

**This project is licensed under [LGPL-3.0][lgpl3].**\
**&copy; 2024 Deftu**

[lgpl3]: https://www.gnu.org/licenses/lgpl-3.0.en.html
