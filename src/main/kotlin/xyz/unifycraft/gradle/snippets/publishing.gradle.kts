package xyz.unifycraft.gradle.snippets

import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseProject
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.jar
import xyz.unifycraft.gradle.utils.propertyOr

plugins {
    java
    id("com.modrinth.minotaur")
    //id("com.matthewprenger.cursegradle")
}

val extension = extensions.create("modpub", ModPublishingExtension::class)

afterEvaluate {
    val modrinthExtension = extension.getModrinth()
    val modrinthToken = propertyOr("publish", "modrinth.token", modrinthExtension.token.get())
    //val curseForgeApiKey = propertyOr("publish", "curseforge.apikey", "")

    if (modrinthToken.isNotBlank())
        setupModrinth(modrinthToken, modrinthExtension)
    //if (curseForgeApiKey.isNotBlank())
    //    setupCurseForge(curseForgeApiKey)
}

fun setupModrinth(token: String, extension: PublishingModrinthExtension) {
    configure<ModrinthExtension> {
        this.token.set(token)
        projectId.set(propertyOr("publish", "modrinth.projectId", extension.projectId.get()))
        versionNumber.set(extension.version.getOrElse(project.version.toString()))
        versionType.set(propertyOr("publish", "modrinth.version.type", extension.versionType.get().value))
        uploadFile.set(extension.uploadFile.getOrElse(tasks.jar.get()))
        gameVersions.set(propertyOr("publish", "modrinth.game.versions", extension.gameVersions.get().joinToString(",")).split(","))
        loaders.set(propertyOr("publish", "modrinth.game.loaders", extension.loaders.get().joinToString(",")).split(","))
        // TODO dependencies
    }
}

/*fun setupCurseForge(apiKey: String) {
    configure<CurseExtension> {
        this.apiKey = apiKey
        project(closureOf<CurseProject> {
            id = propertyOr("publish", "curseforge.project.id")
            releaseType = propertyOr("publish", "curseforge.version.type")
            propertyOr("publish", "curseforge.game.versions").split(",").forEach { addGameVersion(it) }
            // TODO:
            // mainArtifact(jar) {
            //     displayName = ""
            // }
        })
    }
}*/
