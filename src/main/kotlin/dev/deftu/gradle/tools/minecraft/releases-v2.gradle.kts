package dev.deftu.gradle.tools.minecraft

import com.modrinth.minotaur.Minotaur
import com.modrinth.minotaur.ModrinthExtension
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*
import dev.deftu.gradle.utils.version.MinecraftVersion
import net.darkhax.curseforgegradle.TaskPublishCurseForge

@Suppress("PropertyName")
val ALL_PLATFORM_TASK_NAME = "publishModToAllPlatforms"

@Suppress("PropertyName")
val MODRINTH_TASK_NAME = "publishModToModrinth"

@Suppress("PropertyName")
val CURSEFORGE_TASK_NAME = "publishModToCurseForge"

plugins {
    java
}

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("toolkitReleasesV2", ReleasingV2Extension::class, project.objects)

afterEvaluate {
    tasks.register(ALL_PLATFORM_TASK_NAME) {
        group = ToolkitConstants.TASK_GROUP
        dependsOn("build")
    }

    if (modData.isPresent) {
        if (extension.detectVersionType.getOrElse(false)) {
            val version = extension.releaseVersion.getOrElse(modData.version)

            // If the version starts with "0.0.", it can be assumed to be an alpha version.
            // Otherwise, if it starts with 0.x.x, it can be assumed to be a beta version.
            // If both of these checks fail, it can be assumed to be a release version.
            extension.versionType.set(when {
                version.startsWith("0.0.") -> VersionType.ALPHA
                version.startsWith("0.") -> VersionType.BETA
                else -> VersionType.RELEASE
            })
        }

        extension.tokens.modrinthToken.orNull?.takeIf(String::isNotBlank)?.let(::setupModrinth)
        extension.tokens.curseForgeToken.orNull?.takeIf(String::isNotBlank)?.let(::setupCurseForge)
    }
}

fun setupModrinth(token: String) {
    val projectId = extension.projectIds.modrinth.orNull ?: return
    apply<Minotaur>()
    configure<ModrinthExtension> {
        debugMode.set(extension.debugMode)
        failSilently.set(true)
        detectLoaders.set(false)

        this.token.set(token)
        this.projectId.set(projectId)

        this.versionName.set(extension.getReleaseName())
        this.versionNumber.set(extension.getReleaseVersion())
        this.versionType.set(extension.versionType.getOrElse(VersionType.RELEASE).value)
        this.changelog.set(extension.changelog.content)

        this.uploadFile.set(extension.file)
        if (extension.isUsingSourcesJar) this.additionalFiles.add(extension.jars.sourcesJar.orElse(getSourcesJarTask()))
        if (extension.isUsingJavadocJar) this.additionalFiles.add(extension.jars.javadocJar.orElse(tasks.named<Jar>("javadocJar")))

        this.gameVersions.addAll(extension.gameVersions.getOrElse(listOf(mcData.version)).map(MinecraftVersion<*>::toString))
        this.loaders.addAll(extension.loaders.getOrElse(listOf(mcData.loader)).map(ModLoader::toString))

        this.dependencies.addAll(extension.dependencies.map { dependency ->
            val type = when (dependency.type.getOrElse(DependencyType.OPTIONAL)) {
                DependencyType.REQUIRED -> com.modrinth.minotaur.dependencies.DependencyType.REQUIRED
                DependencyType.OPTIONAL -> com.modrinth.minotaur.dependencies.DependencyType.OPTIONAL
                DependencyType.INCOMPATIBLE -> com.modrinth.minotaur.dependencies.DependencyType.INCOMPATIBLE
                DependencyType.EMBEDDED -> com.modrinth.minotaur.dependencies.DependencyType.EMBEDDED
                else -> throw IllegalArgumentException("Could not map DGT dependency type to Minotaur")
            }

            ModDependency(dependency.projectId.get(), type)
        })
    }

    tasks.register(MODRINTH_TASK_NAME) {
        group = ToolkitConstants.TASK_GROUP
        dependsOn("modrinth")
    }.also { tasks[ALL_PLATFORM_TASK_NAME].dependsOn(it) }
}

fun setupCurseForge(token: String) {
    val projectId = extension.projectIds.curseforge.orNull ?: return
    tasks.register<TaskPublishCurseForge>(CURSEFORGE_TASK_NAME) {
        group = ToolkitConstants.TASK_GROUP

        this.debugMode = extension.debugMode.getOrElse(false)
        this.apiToken = token

        upload(projectId, extension.file) {
            disableVersionDetection()

            this.displayName = extension.getReleaseName()
            // There is no concept of a "version number" on CurseForge
            this.releaseType = extension.versionType.getOrElse(VersionType.RELEASE).value
            this.changelog = extension.changelog.content
            this.changelogType = extension.changelog.type

            if (extension.isUsingSourcesJar) withAdditionalFile(extension.jars.sourcesJar.orElse(getSourcesJarTask()))
            if (extension.isUsingJavadocJar) withAdditionalFile(extension.jars.javadocJar.orElse(tasks.named<Jar>("javadocJar")))

            extension.gameVersions.getOrElse(listOf(mcData.version)).map(MinecraftVersion<*>::toString).forEach(this::addGameVersion)
            extension.loaders.getOrElse(listOf(mcData.loader)).map(ModLoader::toString).forEach(this::addModLoader)

            extension.dependencies.forEach { dependency ->
                val dependencyProjectId = dependency.projectId.orNull ?: return@forEach
                val type = dependency.type.getOrElse(DependencyType.OPTIONAL)
                when (type) {
                    DependencyType.REQUIRED -> addRequirement(dependencyProjectId)
                    DependencyType.OPTIONAL -> addOptional(dependencyProjectId)
                    DependencyType.INCOMPATIBLE -> addIncompatibility(dependencyProjectId)
                    DependencyType.EMBEDDED -> addEmbedded(dependencyProjectId)
                    else -> throw IllegalStateException("Could not attain CurseForge dependency type for $dependencyProjectId")
                }
            }
        }
    }
}

val ReleasingV2Extension.isUsingSourcesJar: Boolean
    get() = jars.includeSourcesJar.getOrElse(false) && tasks.findByName("sourcesJar").let { it != null && it.enabled }

val ReleasingV2Extension.isUsingJavadocJar: Boolean
    get() = jars.includeJavadocJar.getOrElse(false) && tasks.findByName("javadocJar").let { it != null && it.enabled }

fun ReleasingV2Extension.getReleaseName(): String {
    val configuredReleaseName = releaseName.orNull
    if (!configuredReleaseName.isNullOrBlank()) {
        return configuredReleaseName
    }

    val prefix = buildString {
        var content = ""
        if (isMultiversionProject()) {
            content += buildString {
                append(mcData.loader.friendlyName).append(" ").append(mcData.version)
            }
        }

        if (content.isNotBlank()) {
            append("[").append(content).append("] ")
        }
    }

    return "${prefix}${modData.name} ${modData.version}"
}

// 1.0.0+main-12345-1.12.2-forge
fun ReleasingV2Extension.getReleaseVersion(): String {
    val version = releaseVersion.getOrElse(modData.version)
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
                if (includingGitData) {
                    append("-")
                }

                append(mcData.version)
                append("-")
                append(mcData.loader.friendlyString)
            }
        }

        if (content.isNotBlank()) {
            if (!version.endsWith("+")) {
                append("+")
            }

            append(content)
        }
    }

    return "${version}${suffix}"
}
