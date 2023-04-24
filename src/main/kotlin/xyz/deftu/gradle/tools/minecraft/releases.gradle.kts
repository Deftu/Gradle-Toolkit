package xyz.deftu.gradle.tools.minecraft

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.github.breadmoirai.githubreleaseplugin.GithubReleasePlugin
import com.modrinth.minotaur.Minotaur
import com.modrinth.minotaur.ModrinthExtension
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.jar
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.*
import xyz.deftu.gradle.GitData
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.utils.isMultiversionProject
import xyz.deftu.gradle.utils.propertyBoolOr
import xyz.deftu.gradle.utils.propertyOr
import java.nio.charset.StandardCharsets

plugins {
    java
}

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("releases", ReleasingExtension::class)

fun ReleasingExtension.getReleaseName(): String {
    val configuredReleaseName = releaseName.orNull
    if (!configuredReleaseName.isNullOrBlank()) return configuredReleaseName

    val prefix = buildString {
        append("[")
        if (isMultiversionProject()) {
            if (mcData.isFabric && describeFabricWithQuilt.get()) {
                append("Fabric/Quilt")
            } else {
                append(mcData.loader.name.capitalize())
            }

            append(" ").append(mcData.versionStr)
        }

        append("] ")
    }

    return "$prefix${modData.name} ${modData.version}"
}

fun ReleasingExtension.getReleaseVersion(): String {
    val suffix = buildString {
        var content = ""

        val includingGitData = gitData.shouldAppendVersion(project)
        if (includingGitData) {
            content += buildString {
                append(gitData.branch)
                append("-")
                append(gitData.commit)
            }
        }

        if (isMultiversionProject()) {
            content += buildString {
                if (includingGitData) append(".")
                append(mcData.versionStr)
                append("-")
                append(mcData.loader.name)
            }
        }

        if (content.isNotBlank()) {
            append("+")
            append(content)
        }
    }

    return "${version.getOrElse(modData.version)}${suffix}"
}

fun ReleasingExtension.getUploadFile() = file.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").get())

fun ReleasingExtension.getGameVersions() = gameVersions.getOrElse(listOf(mcData.versionStr))
fun ReleasingExtension.getLoaders(capitalized: Boolean) = loaders.getOrElse(listOf(mcData.loader.name)).map { loader ->
    if (capitalized) loader.capitalize() else loader
}

fun ReleasingExtension.getVersionType() = versionType.getOrElse(VersionType.RELEASE)

fun ReleasingExtension.shouldAddSourcesJar() = useSourcesJar.getOrElse(false) && tasks.findByName("sourcesJar") != null
fun ReleasingExtension.shouldAddJavadocJar() = useJavadocJar.getOrElse(false) && tasks.findByName("javadocJar") != null

fun ReleasingExtension.getSourcesJar() = sourcesJar.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").get())
fun ReleasingExtension.getJavadocJar() = javadocJar.getOrElse(tasks.named<org.gradle.jvm.tasks.Jar>("javadocJar").get())

afterEvaluate {
    val modrinthToken = propertyOr("publish.modrinth.token", "")
    val curseForgeApiKey = propertyOr("publish.curseforge.apikey", "")
    val githubToken = propertyOr("publish.github.token", "")

    val publishProject by tasks.registering { group = "publishing" }
    publishProject.get().dependsOn(tasks["build"])

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

    if (mcData.isFabric && extension.describeFabricWithQuilt.get()) extension.loaders.add("quilt")

    if (modData.present) {
        if (modrinthToken.isNotBlank()) setupModrinth(modrinthToken)
        if (curseForgeApiKey.isNotBlank()) setupCurseForge(curseForgeApiKey)
        if (githubToken.isNotBlank()) setupGitHub(githubToken)
    }
}

fun setupModrinth(token: String) {
    val projectId = extension.modrinth.projectId.orNull
    if (projectId.isNullOrBlank()) return
    apply<Minotaur>()
    configure<ModrinthExtension> {
        failSilently.set(true)
        detectLoaders.set(false)
        debugMode.set(extension.modrinth.debug.getOrElse(false))

        this.token.set(token)
        this.projectId.set(projectId)
        versionName.set(extension.getReleaseName())
        versionNumber.set(extension.getReleaseVersion())
        versionType.set(extension.getVersionType().value)
        uploadFile.set(extension.getUploadFile())

        if (extension.shouldAddSourcesJar()) additionalFiles.add(extension.getSourcesJar())
        if (extension.shouldAddJavadocJar()) additionalFiles.add(extension.getJavadocJar())

        changelog.set(extension.changelog.get())
        gameVersions.addAll(extension.getGameVersions())
        loaders.addAll(extension.getLoaders(false))
        dependencies.addAll(extension.modrinth.dependencies.getOrElse(listOf()))
    }

    val publishToModrinth by tasks.registering {
        group = "publishing"
        dependsOn("modrinth")
    }

    tasks["publishProject"].dependsOn(publishToModrinth)
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
            displayName = extension.getReleaseName()
            version = extension.getReleaseVersion()
            releaseType = extension.getVersionType().value
            changelog = extension.changelog.get()
            changelogType = extension.curseforge.changelogType.getOrElse("text")
            extension.getLoaders(true).forEach(this::addModLoader)
            extension.getGameVersions().forEach(this::addGameVersion)
            extension.curseforge.relations.getOrElse(listOf()).forEach { relation ->
                relation.applyTo(this)
            }

            if (extension.shouldAddSourcesJar()) withAdditionalFile(extension.getSourcesJar())
            if (extension.shouldAddJavadocJar()) withAdditionalFile(extension.getJavadocJar())
        }
    }

    tasks["publishProject"].dependsOn(publishToCurseForge)
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
        tagName.set(extension.getReleaseVersion())
        releaseName.set(extension.getReleaseName())
        body.set(extension.changelog.get())
        draft.set(extension.github.draft.getOrElse(false))
        prerelease.set(extension.getVersionType() != VersionType.RELEASE)
        generateReleaseNotes.set(extension.github.autogenerateReleaseNotes.getOrElse(false))

        val usedAssets = mutableListOf<Any>()
        usedAssets.add(extension.getUploadFile())

        if (extension.shouldAddSourcesJar()) usedAssets.add(extension.getSourcesJar())
        if (extension.shouldAddJavadocJar()) usedAssets.add(extension.getJavadocJar())

        releaseAssets(*usedAssets.toTypedArray())
    }

    val publishToGitHubRelease by tasks.registering {
        group = "publishing"
        dependsOn("githubRelease")
    }

    tasks["publishProject"].dependsOn(publishToGitHubRelease)
    publishToGitHubRelease.get().mustRunAfter(tasks["build"])
}
