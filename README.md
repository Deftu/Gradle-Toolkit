<div align="center">

# [`Gradle Toolkit`]
UnifyCraft's full Gradle toolkit, used
to create, improve and manage projects.

</div>

---

## Notice
Some of these utilities, and some
of their code was taken from the
[Essential Gradle Toolkit][egt] under
the [GPL-3.0 license][gpl3].

---

## Plugins
<details>
    <summary>xyz.unifycraft.gradle.tools</summary>

Applies various utilities to your Gradle
project. This includes all plugins bundled
inside the toolkit aside from the multi-version
utilities. These plugins will be automatically
configured to suit your needs.
</details>

<details>
    <summary>xyz.unifycraft.gradle.multiversion</summary>

Sets up the preprocessor and Architectury Loom
plugins to support multi-versioning. This
is best used along with `xyz.unifycraft.gradle.tools` or
`xyz.unifycraft.gradle.tools.loom`.
</details>

<details>
    <summary>xyz.unifycraft.gradle.multiversion-root</summary>

Applies the root preprocessor plugin and
ensures you're using the correct Java
version for Architectury Loom.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.blossom</summary>

Automatically configures the Blossom
Gradle plugin, which replaces text
in your code when compiling with
specified tokens. This will automatically
configure tokens to replace text in
your code with your mod's data.

`@MC_VERSION@`: The Minecraft version you are building for.\
`@MOD_LOADER@`: The mod loader you are building for.\
`@MOD_NAME@`: The name of your mod.\
`@MOD_VERSION@`: The version of your mod.\
`@MOD_ID@`: Your mod's mod ID.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.configure</summary>

Automatically configures your Gradle
project according to the mod metadata
you have provided in your properties
file. This is useful along with other
utilities which utilize this mod
metadata to improve their functionality.

```properties
mod.name=ExampleMod
mod.id=examplemod
mod.version=1.0.0
mod.group=com.example
```
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.java</summary>

Sets up your project's Java version
and encoding options for optimal
builds and compatibility.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.kotlin</summary>

Sets up your project's Kotlin
version and encoding options for
optimal builds and compatibility.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.loom</summary>

Automatically sets up Architectury
Loom and provides helpful utilities to
better manage your project.

This plugin will automatically configure
the `minecraft`, `mappings`, `forge` and
Fabric loader configurations depending on
data you provide in your properties file
under the minecraft namespace.

```properties
minecraft.version=1.12.2
# loom.platform automatically configures minecraft.loader
loom.platform=forge
```
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.publishing</summary>

Automatically configures Maven Publishing
to conform and adapt with Architectury Loom
and `xyz.unifycraft.gradle.tools.shadow`.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.releases</summary>

Configures multiple Gradle plugins to
aide you in managing your project's
releases and automate releasing to
Modrinth, Curseforge and Github.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.repo</summary>

Sets repositories commonly used for
modding in your project.
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.resources</summary>

Automatically configures resource
processing with common replacements.

`${mod_version}`: The version of your mod.\
`${mod_id}`: Your mod's mod ID.\
`${mod_name}`: The name of your mod.\
`${mc_version}`: The current Minecraft version.\
`${format_mc_version}`: The current Minecraft version conformed to a padded integer.\
`${java_version}`: The current Java version, conformed to "JAVA_8", "JAVA_16" and "JAVA_17".
</details>

<details>
    <summary>xyz.unifycraft.gradle.tools.shadow</summary>

Sets up a custom version of Shadow which
works with Architectury Loom, as the default
version does not work as it does not remap the
resulting JAR.
</details>

<div align="center">

**This project is licensed under [LGPL-3.0][lgpl3].**\
**&copy; 2022 UnifyCraft**

</div>

[egt]: https://github.com/EssentialGG/essential-gradle-toolkit
[gpl3]: https://www.gnu.org/licenses/gpl-3.0.en.html
[lgpl3]: https://www.gnu.org/licenses/lgpl-3.0.en.html
