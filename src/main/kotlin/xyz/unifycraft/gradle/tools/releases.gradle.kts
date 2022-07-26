package xyz.unifycraft.gradle.tools

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import me.hypherionmc.cursegradle.CurseArtifact
import me.hypherionmc.cursegradle.CurseExtension
import me.hypherionmc.cursegradle.CurseProject
import me.hypherionmc.cursegradle.CurseRelation
import me.hypherionmc.cursegradle.Options
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.jar
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData
import xyz.unifycraft.gradle.utils.propertyOr
import java.nio.charset.StandardCharsets

plugins {
    java
    id("com.modrinth.minotaur")
    id("me.hypherionmc.cursegradle")
    id("com.github.breadmoirai.github-release")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("releases", ReleasingExtension::class)

afterEvaluate {
    val modrinthToken = propertyOr("publish.modrinth.token", "")!!
    val curseForgeApiKey = propertyOr("publish.curseforge.apikey", "")!!
    val githubToken = propertyOr("publish.github.token", "")!!

    val task = tasks.register("releaseMod") {
        group = "unifycraft-gradle-toolkit"
        if (modrinthToken.isNotBlank()) dependsOn("modrinth")
        if (curseForgeApiKey.isNotBlank()) dependsOn("curseforge")
        if (githubToken.isNotBlank()) dependsOn("githubRelease")
    }

    if (extension.changelogFile.isPresent) {
        val changelogFile = extension.changelogFile.get()
        logger.lifecycle("Set changelog to contents of ${changelogFile.nameWithoutExtension}")
        val changelog = changelogFile.readText(StandardCharsets.UTF_8)
        extension.changelog.set(changelog)
        extension.curseforge.changelogType.set(when (changelogFile.extension) {
            "md" -> "markdown"
            "html" -> "html"
            else -> "text"
        })
    }

    if (modrinthToken.isNotBlank())
        setupModrinth(modrinthToken)
    if (curseForgeApiKey.isNotBlank())
        setupCurseForge(curseForgeApiKey)
    if (githubToken.isNotBlank())
        setupGitHub(githubToken)
}

fun setupModrinth(token: String) {
    configure<ModrinthExtension> {
        failSilently.set(true)
        this.token.set(token)
        projectId.set(extension.modrinth.projectId.get())
        versionName.set(extension.releaseName.getOrElse("${modData.name} ${modData.version}"))
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
                displayName = extension.releaseName.getOrElse("${modData.name} ${modData.version}")
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
