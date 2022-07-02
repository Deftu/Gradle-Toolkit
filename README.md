<div align="center">

# [`Gradle Toolkit`]
UnifyCraft's full Gradle toolkit, used
to create, improve and manage projects.

</div>

![Repository badge](https://maven.unifycraft.xyz/api/badge/latest/releases/xyz/unifycraft/gradle/gradle-toolkit?color=912fed&name=Gradle+Toolkit)

---

## Notice
Some of these utilities, and some
of their code was taken from the
[Essential Gradle Toolkit][egt] under
the [GPL-3.0 license][gpl3].

---

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
