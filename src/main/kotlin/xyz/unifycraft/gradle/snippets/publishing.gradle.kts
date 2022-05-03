package xyz.unifycraft.gradle.snippets

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.jar
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.utils.propertyOr
import java.nio.charset.StandardCharsets

plugins {
    java
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
}

val mcData = MCData.fromExisting(project)
val extension = extensions.create("releases", ModPublishingExtension::class)
val task = tasks.create("publishMod") {
    group = "unifycraft-gradle-toolkit"
    dependsOn(tasks["modrinth"])
    dependsOn(tasks["curseforge"])
}

if (extension.changelogFile.isPresent) {
    val changelogFile = extension.changelogFile.get()
    val changelog = changelogFile.readText(StandardCharsets.UTF_8)
    extension.changelog.set(changelog)
    extension.curseforge.changelogType.set(when (changelogFile.extension) {
        "md" -> "markdown"
        "html" -> "html"
        else -> "text"
    })
}

afterEvaluate {
    val modrinthToken = propertyOr("publish", "modrinth.token", "")
    val curseForgeApiKey = propertyOr("publish", "curseforge.apikey", "")
    val githubToken = propertyOr("publish", "github.token", "")

    if (modrinthToken.isNotBlank())
        setupModrinth(modrinthToken)
    if (curseForgeApiKey.isNotBlank())
        setupCurseForge(curseForgeApiKey)
    if (githubToken.isNotBlank())
        setupGitHub(githubToken)
}

fun setupModrinth(token: String) {
    configure<ModrinthExtension> {
        this.token.set(token)
        projectId.set(extension.modrinth.projectId.get())
        versionNumber.set(extension.version.getOrElse(project.version.toString()))
        versionType.set(extension.versionType.getOrElse(VersionType.RELEASE).value)
        uploadFile.set(extension.file.getOrElse(tasks.jar.get()))
        changelog.set(extension.changelog.get())
        gameVersions.set(extension.gameVersions.getOrElse(listOf(mcData.versionStr)))
        loaders.set(extension.loaders.getOrElse(listOf(mcData.loader.name)))
        dependencies.set(extension.modrinth.dependencies.getOrElse(listOf()))
    }
}

fun setupCurseForge(apiKey: String) {
    configure<CurseExtension> {
        this.apiKey = apiKey
        project(closureOf<CurseProject> {
            id = extension.curseforge.projectId.get()
            releaseType = extension.versionType.getOrElse(VersionType.RELEASE).value
            extension.gameVersions.getOrElse(listOf(mcData.versionStr)).forEach(::addGameVersion)

            changelog = extension.changelog.get()
            changelogType = extension.curseforge.changelogType.getOrElse("text")

            relations(closureOf<CurseRelation> {
                extension.curseforge.dependencies.get().forEach { dependency ->
                    if (dependency.isRequired) requiredDependency(dependency.name)
                    else optionalDependency(dependency.name)
                }
            })

            mainArtifact(extension.file.getOrElse(tasks.jar.get()), closureOf<CurseArtifact> {
                displayName = extension.curseforge.releaseName.getOrElse(project.name)
            })

            options(closureOf<Options> {
                forgeGradleIntegration = false
            })
        })
    }
}

fun setupGitHub(token: String) {
    configure<GithubReleaseExtension> {
        setToken(token)
        owner.set(extension.github.owner.get())
        repo.set(extension.github.repository.get())
        val version = extension.version.getOrElse(project.version.toString())
        tagName.set(version)
        releaseName.set(extension.github.releaseName.getOrElse(version))
        body.set(extension.changelog.get())
        draft.set(extension.github.draft.getOrElse(false))
        prerelease.set(extension.versionType.getOrElse(VersionType.RELEASE) != VersionType.RELEASE)
        releaseAssets(extension.file.getOrElse(tasks.jar.get()))
    }
}
