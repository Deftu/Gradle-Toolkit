package xyz.unifycraft.gradle

plugins {
    id("xyz.unifycraft.gradle.loomconfig")
}

val extension = extensions.create("multiversion", MultiversionExtension::class.java, project)
val mcVersion = extension.mcVersion.getOrElse(MCVersion.from(project))

val loomConfigExtension = extensions["loomConfig"] as LoomConfigExtension
loomConfigExtension.forge.set(mcVersion.isForge)
loomConfigExtension.version.set(mcVersion.versionStr)