package xyz.unifycraft.gradle.tools

import xyz.unifycraft.gradle.utils.propertyOr

repositories {
    mavenCentral()

    maven("https://maven.unifycraft.xyz/releases")
    optionalMaven("repo.jitpack", "https://jitpack.io/")
    optionalMaven("repo.essential", "https://repo.essential.gg/repository/maven-public/")
    optionalMaven("repo.sponge", "https://repo.spongepowered.org/maven/")
    maven("https://maven.unifycraft.xyz/snapshots")

    mavenLocal()
}

fun RepositoryHandler.optionalMaven(propertyName: String, url: String) {
    if (!project.hasProperty(propertyName) && project.propertyBoolOr(propertyName, false)) return
    maven(url)
}
