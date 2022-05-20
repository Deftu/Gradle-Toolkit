package xyz.unifycraft.gradle.tools

import xyz.unifycraft.gradle.utils.propertyOr

repositories {
    mavenCentral()
    mavenLocal()

    optionalMaven("repo.jitpack", "https://jitpack.io/")
    optionalMaven("repo.essential", "https://repo.essential.gg/repository/maven-public/")
    optionalMaven("repo.sponge", "https://repo.spongepowered.org/maven/")
}

fun RepositoryHandler.optionalMaven(propertyName: String, url: String) {
    if (!project.hasProperty(propertyName) && project.propertyOr(propertyName, "false") == "true") return
    maven(url)
}
