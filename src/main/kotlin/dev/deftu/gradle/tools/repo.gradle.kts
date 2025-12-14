package dev.deftu.gradle.tools

import dev.deftu.gradle.utils.propertyBoolOr

repositories {
    mavenCentral()
    mavenLocal()

    // General
    optionalMaven("repo.deftu.releases", "https://maven.deftu.dev/releases/", "Deftu Releases")
    optionalMaven("repo.jitpack", "https://jitpack.io/", "JitPack")
    optionalMaven("repo.deftu.snapshots", "https://maven.deftu.dev/snapshots/", "Deftu Snapshots")

    // Minecraft
    optionalMaven("repo.modrinth", "https://api.modrinth.com/maven/", "Modrinth") { onlyForGroups("maven.modrinth") }
    optionalMaven("repo.essential", "https://repo.essential.gg/repository/maven-public/", "Essential")
    optionalMaven("repo.kff", "https://thedarkcolour.github.io/KotlinForForge/", "KotlinForForge")
    optionalMaven("repo.teamresourceful", "https://maven.teamresourceful.com/repository/maven-releases", "Team Resourceful Releases")
    optionalMaven("repo.bawnorton", "https://maven.bawnorton.com/releases", "Bawnorton Releases") { onlyForGroups("com.github.bawnorton") }
    optionalMaven("repo.neu", "https://maven.notenoughupdates.org/releases", "NotEnoughUpdates Releases") { onlyForGroups("org.notenoughupdates.moulconfig") }
    optionalMaven("repo.deftu.mirror", "https://maven.deftu.dev/mirror", "Deftu Mirror") { onlyForGroups("com.terraformersmc") }
    optionalMaven("repo.sponge", "https://repo.spongepowered.org/maven/", "SpongePowered") { onlyForGroups("org.spongepowered") }
}

fun RepositoryHandler.optionalMaven(
    propertyName: String,
    url: String,
    name: String? = null,
    block: ArtifactRepository.() -> Unit = {  }
) {
    if (!project.propertyBoolOr(propertyName, true)) {
        return
    }

    maven(url) {
        name?.let { name ->
            setName(name)
        }

        block()
    }
}

fun ArtifactRepository.onlyForGroups(vararg groups: String) {
    content {
        for (group in groups) {
            includeGroup(group)
        }
    }
}
