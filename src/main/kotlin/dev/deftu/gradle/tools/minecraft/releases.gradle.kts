package dev.deftu.gradle.tools.minecraft

import com.modrinth.minotaur.Minotaur
import com.modrinth.minotaur.ModrinthExtension
import dev.deftu.gradle.ToolkitConstants
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.kotlin.dsl.*
import dev.deftu.gradle.utils.*
import dev.deftu.gradle.utils.version.MinecraftVersion
import java.nio.charset.StandardCharsets

plugins {
    java
}

val taskName = "publishMod"

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("toolkitReleases", ReleasingExtension::class)

fun ReleasingExtension.getReleaseName(): String {
    val configuredReleaseName = releaseName.orNull
    if (!configuredReleaseName.isNullOrBlank()) return configuredReleaseName

    val prefix = buildString {
        var content = ""
        if (isMultiversionProject()) {
            content += buildString {
                if (mcData.isFabric && describeFabricWithQuilt.get()) {
                    append("Fabric/Quilt")
                } else {
                    append(mcData.loader.friendlyName)
                }

                append(" ").append(mcData.version)
            }
        }

        if (content.isNotBlank()) {
            append("[").append(content).append("] ")
        }
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
                if (includingGitData) append("+")
                append(mcData.version)
                append("-")
                append(mcData.loader.friendlyString)
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

fun ReleasingExtension.getGameVersions() = gameVersions.getOrElse(listOf(mcData.version))

fun ReleasingExtension.getLoaders() = loaders.getOrElse(listOf(mcData.loader))

fun ReleasingExtension.getVersionType() = versionType.getOrElse(VersionType.RELEASE)

fun ReleasingExtension.shouldAddSourcesJar() = useSourcesJar.getOrElse(false) && tasks.findByName("sourcesJar") != null
fun ReleasingExtension.shouldAddJavadocJar() = useJavadocJar.getOrElse(false) && tasks.findByName("javadocJar") != null

fun ReleasingExtension.getSourcesJar() = sourcesJar.getOrElse(getSourcesJarTask().get())
fun ReleasingExtension.getJavadocJar() = javadocJar.getOrElse(tasks.named<Jar>("javadocJar").get())

afterEvaluate {
    val modrinthToken = propertyOr("publish.modrinth.token", "")
    val curseForgeApiKey = propertyOr("publish.curseforge.apikey", "")

    tasks.register(taskName) {
        group = ToolkitConstants.TASK_GROUP
        dependsOn("build")
    }

    if (extension.changelogFile.isPresent) {
        val changelogFile = extension.changelogFile.get()
        logger.lifecycle("Setting Minecraft release changelog to contents of ${changelogFile.name}")
        val changelog = changelogFile.readText(StandardCharsets.UTF_8)
        extension.changelog.set(changelog)
        extension.curseforge.changelogType.set(when (changelogFile.extension) {
            "md" -> "markdown"
            "html" -> "html"
            else -> "text"
        })
    }

    if (modData.isPresent) {
        if (extension.detectVersionType.getOrElse(false)) {
            val version = extension.version.getOrElse(modData.version)

            // If the version starts with "0.0.", it can be assumed to be an alpha version.
            // Otherwise, if it starts with 0.x.x, it can be assumed to be a beta version.
            // If both of these checks fail, it can be assumed to be a release version.
            extension.versionType.set(when {
                version.startsWith("0.0.") -> VersionType.ALPHA
                version.startsWith("0.") -> VersionType.BETA
                else -> VersionType.RELEASE
            })
        }

        if (modrinthToken.isNotBlank()) {
            setupModrinth(modrinthToken)
        }

        if (curseForgeApiKey.isNotBlank()) {
            setupCurseForge(curseForgeApiKey)
        }
    }
}

fun setupModrinth(token: String) {
    val projectId = extension.modrinth.projectId.orNull
    if (projectId.isNullOrBlank()) {
        return
    }

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

        if (extension.shouldAddSourcesJar()) {
            additionalFiles.add(extension.getSourcesJar())
        }

        if (extension.shouldAddJavadocJar()) {
            additionalFiles.add(extension.getJavadocJar())
        }

        changelog.set(extension.changelog.get())
        gameVersions.addAll(extension.getGameVersions().map(MinecraftVersion<*>::toString))
        loaders.addAll(extension.getLoaders().map(ModLoader::toString))
        if (mcData.isFabric && extension.describeFabricWithQuilt.getOrElse(false)) {
            loaders.add("quilt")
        }

        dependencies.addAll(extension.modrinth.dependencies.getOrElse(listOf()))
    }

    val publishToModrinth by tasks.registering {
        group = ToolkitConstants.TASK_GROUP

        dependsOn("modrinth")
    }

    tasks[taskName].dependsOn(publishToModrinth)
    publishToModrinth.get().mustRunAfter(tasks["build"])
}

fun setupCurseForge(apiKey: String) {
    val projectId = extension.curseforge.projectId.orNull
    if (projectId.isNullOrBlank()) {
        return
    }

    val publishToCurseForge by tasks.registering(TaskPublishCurseForge::class) {
        group = ToolkitConstants.TASK_GROUP

        this.apiToken = apiKey
        this.debugMode = extension.curseforge.debug.getOrElse(false)

        upload(projectId, extension.getUploadFile()).apply {
            disableVersionDetection()
            displayName = extension.getReleaseName()
            version = extension.getReleaseVersion()
            releaseType = extension.getVersionType().value
            changelog = extension.changelog.get()
            changelogType = extension.curseforge.changelogType.getOrElse("text")
            extension.getLoaders().map(ModLoader::friendlyName).forEach(this::addModLoader)
            if (mcData.isFabric && extension.describeFabricWithQuilt.getOrElse(false)) {
                addModLoader("Quilt")
            }

            extension.getGameVersions().forEach(this::addGameVersion)
            extension.curseforge.relations.getOrElse(listOf()).forEach { relation ->
                relation.applyTo(this)
            }

            if (extension.shouldAddSourcesJar()) {
                withAdditionalFile(extension.getSourcesJar())
            }

            if (extension.shouldAddJavadocJar()) {
                withAdditionalFile(extension.getJavadocJar())
            }
        }
    }

    tasks[taskName].dependsOn(publishToCurseForge)
    publishToCurseForge.get().mustRunAfter(tasks["build"])
}
