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
    optionalMaven("repo.essential", "https://repo.essential.gg/repository/maven-public/", "Essential")
    optionalMaven("repo.kff", "https://thedarkcolour.github.io/KotlinForForge/", "KotlinForForge")

    optionalMaven("repo.deftu.mirror", "https://maven.deftu.dev/mirror", "Deftu Mirror") {
        exclusiveContent {
            filter {
                includeGroup("com.terraformersmc") // Mod Menu
            }
        }
    }

    optionalMaven("repo.sponge", "https://repo.spongepowered.org/maven/", "SpongePowered") {
        exclusiveContent {
            filter {
                includeGroup("org.spongepowered")
            }
        }
    }
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
