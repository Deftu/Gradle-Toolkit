package xyz.enhancedpixel.gradle.tools

import xyz.enhancedpixel.gradle.utils.propertyBoolOr

repositories {
    mavenCentral()
    mavenLocal()

    optionalMaven("repo.jitpack", "JitPack", "https://jitpack.io/")
    optionalMaven("repo.essential", "Essential", "https://repo.essential.gg/repository/maven-public/")
    optionalMaven("repo.sponge", "SpongePowered", "https://repo.spongepowered.org/maven/")
    maven("https://maven.enhancedpixel.xyz/releases")
    maven("https://maven.enhancedpixel.xyz/snapshots")
}

fun RepositoryHandler.optionalMaven(propertyName: String, name: String? = null, url: String) {
    if (project.hasProperty(propertyName) && project.propertyBoolOr(propertyName, false)) return
    maven(url) {
        name?.let { setName(it) }
    }
}
