package xyz.unifycraft.gradle.tools

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.github.breadmoirai.githubreleaseplugin.GithubReleasePlugin
import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseGradlePlugin
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.Minotaur
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.jar
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData
import xyz.unifycraft.gradle.utils.isLoomPresent
import xyz.unifycraft.gradle.utils.isMultiversionProject
import xyz.unifycraft.gradle.utils.propertyOr
import java.nio.charset.StandardCharsets

plugins {
    java
    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("releases", ReleasingExtension::class)

afterEvaluate {
    val modrinthToken = propertyOr("publish.modrinth.token", "")!!
    val curseForgeApiKey = propertyOr("publish.curseforge.apikey", "")!!
    val githubToken = propertyOr("publish.github.token", "")!!

    val publishToModrinth by tasks.registering { group = "unifycraft" }
    val publishToCurseForge by tasks.registering { group = "unifycraft" }
    val publishToGitHubRelease by tasks.registering { group = "unifycraft" }
    tasks.register("releaseMod") {
        group = "unifycraft"
        if (modrinthToken.isNotBlank()) dependsOn(publishToModrinth)
        if (curseForgeApiKey.isNotBlank()) dependsOn(publishToCurseForge)
        if (githubToken.isNotBlank()) dependsOn(publishToGitHubRelease)
    }

    if (extension.changelogFile.isPresent) {
        val changelogFile = extension.changelogFile.get()
        logger.lifecycle("Set changelog to contents of ${changelogFile.name}")
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
    val projectId = extension.modrinth.projectId.orNull
    if (projectId.isNullOrBlank()) return
    tasks["publishToModrinth"].dependsOn(tasks["modrinth"])
    apply<Minotaur>()
    configure<ModrinthExtension> {
        failSilently.set(true)
        this.token.set(token)
        this.projectId.set(projectId)
        versionName.set(extension.releaseName.getOrElse("${modData.name} ${modData.version}"))
        versionNumber.set(extension.version.getOrElse(if (isMultiversionProject()) "${mcData.versionStr}-${modData.version}" else modData.version))
        versionType.set(extension.versionType.getOrElse(VersionType.RELEASE).value)
        uploadFile.set(extension.file.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").get()))
        changelog.set(extension.changelog.get())
        gameVersions.addAll(extension.gameVersions.getOrElse(listOf(mcData.versionStr)))
        loaders.addAll(extension.loaders.getOrElse(listOf(mcData.loader.name)))
        dependencies.addAll(extension.modrinth.dependencies.getOrElse(listOf()))
    }
}

fun setupCurseForge(apiKey: String) {
    val projectId = extension.curseforge.projectId.orNull
    if (projectId.isNullOrBlank()) return
    apply<CurseGradlePlugin>()
    configure<CurseExtension> {
        this.apiKey = apiKey
        project(closureOf<CurseProject> {
            id = projectId
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

            uploadTask.dependsOn(tasks["remapJar"])
            mainArtifact(extension.file.getOrElse(tasks["remapJar"] as org.gradle.jvm.tasks.Jar), closureOf<CurseArtifact> {
                displayName = extension.releaseName.getOrElse("${modData.name} ${modData.version}")
            })

            if (project.isLoomPresent()) options(closureOf<Options> {
                forgeGradleIntegration = false
            })
        })
    }
    tasks["publishToCurseForge"].dependsOn(tasks["curseforge"])
}

fun setupGitHub(token: String) {
    val owner = extension.github.owner.orNull
    val repo = extension.github.repository.orNull
    if (owner.isNullOrBlank() || repo.isNullOrBlank()) return
    tasks["publishToGitHubRelease"].dependsOn("githubRelease")
    apply<GithubReleasePlugin>()
    configure<GithubReleaseExtension> {
        setToken(token)
        this.owner.set(owner)
        this.repo.set(repo)
        val version = extension.version.getOrElse(project.version.toString())
        tagName.set(version)
        releaseName.set(extension.github.releaseName.getOrElse(version))
        body.set(extension.changelog.get())
        draft.set(extension.github.draft.getOrElse(false))
        prerelease.set(extension.versionType.getOrElse(VersionType.RELEASE) != VersionType.RELEASE)
        releaseAssets(extension.file.getOrElse(tasks.jar.get()))
    }
}
