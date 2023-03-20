package xyz.deftu.gradle.tools.minecraft

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.github.breadmoirai.githubreleaseplugin.GithubReleasePlugin
import com.modrinth.minotaur.Minotaur
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.jar
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.kotlin.dsl.*
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData
import xyz.deftu.gradle.utils.isMultiversionProject
import xyz.deftu.gradle.utils.propertyOr
import java.nio.charset.StandardCharsets

plugins {
    java
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)
val extension = extensions.create("releases", ReleasingExtension::class)

afterEvaluate {
    val modrinthToken = propertyOr("publish.modrinth.token", "")
    val curseForgeApiKey = propertyOr("publish.curseforge.apikey", "")
    val githubToken = propertyOr("publish.github.token", "")

    val releaseProject by tasks.registering { group = "publishing" }
    releaseProject.get().dependsOn(tasks["build"])

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

    if (modData.present && modrinthToken.isNotBlank())
        setupModrinth(modrinthToken)
    if (modData.present && curseForgeApiKey.isNotBlank())
        setupCurseForge(curseForgeApiKey)
    if ((modData.present || projectData.present) && githubToken.isNotBlank())
        setupGitHub(githubToken)
}

fun setupModrinth(token: String) {
    val projectId = extension.modrinth.projectId.orNull
    if (projectId.isNullOrBlank()) return
    apply<Minotaur>()
    configure<ModrinthExtension> {
        failSilently.set(true)
        this.token.set(token)
        this.projectId.set(projectId)
        versionName.set(extension.releaseName.getOrElse("${if (isMultiversionProject()) "[${mcData.versionStr}] " else ""}${modData.name} ${modData.version}"))
        versionNumber.set(extension.version.getOrElse(if (isMultiversionProject()) "${mcData.versionStr}-${modData.version}" else modData.version))
        versionType.set(extension.versionType.getOrElse(VersionType.RELEASE).value)
        uploadFile.set(extension.file.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").get()))
        changelog.set(extension.changelog.get())
        gameVersions.addAll(extension.gameVersions.getOrElse(listOf(mcData.versionStr)))
        loaders.addAll(extension.loaders.getOrElse(listOf(mcData.loader.name)))
        dependencies.addAll(extension.modrinth.dependencies.getOrElse(listOf()))
    }

    val publishToModrinth by tasks.registering {
        group = "publishing"
        dependsOn("modrinth")
    }

    tasks["releaseProject"].dependsOn(publishToModrinth)
    publishToModrinth.get().mustRunAfter(tasks["build"])
}

fun setupCurseForge(apiKey: String) {
    val projectId = extension.curseforge.projectId.orNull
    if (projectId.isNullOrBlank()) return

    val publishToCurseForge by tasks.registering(TaskPublishCurseForge::class) {
        group = "publishing"
        this.apiToken = apiKey

        upload(projectId, extension.file.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").get())).apply {
            disableVersionDetection()
            displayName = extension.releaseName.getOrElse("${if (isMultiversionProject()) "[${mcData.versionStr}] " else ""}${modData.name} ${modData.version}")
            releaseType = extension.versionType.getOrElse(VersionType.RELEASE).value
            changelog = extension.changelog.get()
            changelogType = extension.curseforge.changelogType.getOrElse("text")
            extension.loaders.getOrElse(listOf(mcData.loader.name)).forEach(this::addModLoader)
            extension.gameVersions.getOrElse(listOf(mcData.versionStr)).forEach(this::addGameVersion)
            extension.curseforge.relations.getOrElse(listOf()).forEach { relation ->
                relation.applyTo(this)
            }
        }
    }

    tasks["releaseProject"].dependsOn(publishToCurseForge)
    publishToCurseForge.get().mustRunAfter(tasks["build"])
}

fun setupGitHub(token: String) {
    val owner = extension.github.owner.orNull
    val repo = extension.github.repository.orNull
    if (owner.isNullOrBlank() || repo.isNullOrBlank()) return
    apply<GithubReleasePlugin>()
    configure<GithubReleaseExtension> {
        setToken(token)
        this.owner.set(owner)
        this.repo.set(repo)
        val version = extension.version.getOrElse(project.version.toString())
        tagName.set(version)
        releaseName.set(extension.releaseName.getOrElse(buildString {
            if (mcData.present) {
                append(if (isMultiversionProject()) "[${mcData.versionStr}] " else "" + modData.name + " " + modData.version)
            }

            if (projectData.present) {
                append(projectData.name + " " + projectData.version)
            }
        }))
        body.set(extension.changelog.get())
        draft.set(extension.github.draft.getOrElse(false))
        prerelease.set(extension.versionType.getOrElse(VersionType.RELEASE) != VersionType.RELEASE)
        releaseAssets(extension.file.getOrElse(tasks.jar.get()))
    }

    val publishToGitHubRelease by tasks.registering {
        group = "publishing"
        dependsOn("githubRelease")
    }

    tasks["releaseProject"].dependsOn(publishToGitHubRelease)
    publishToGitHubRelease.get().mustRunAfter(tasks["build"])
}
