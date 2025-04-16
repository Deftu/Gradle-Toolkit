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

        this.versionName.set(extension.releaseName)
        this.versionNumber.set(extension.releaseVersion)
        this.versionType.set(extension.versionType.map(VersionType::value))
        this.changelog.set(extension.changelog.content)

        this.uploadFile.set(extension.file)
        if (extension.jars.isUsingSourcesJar) this.additionalFiles.add(extension.jars.uploadedSourcesJar)
        if (extension.jars.isUsingJavadocJar) this.additionalFiles.add(extension.jars.uploadedJavadocJar)

        this.gameVersions.addAll(extension.gameVersions.get().map(MinecraftVersion<*>::toString))
        this.loaders.addAll(extension.loaders.get().map(ModLoader::toString))

        this.dependencies.addAll(extension.dependencies.map { dependency ->
            val dependencyProjectId = dependency.projectId.orNull
                ?: dependency.modrinth.projectId.orNull
                ?: throw IllegalStateException("Could not attain Modrinth dependency project ID for ${dependency.name}")
            val type = when (dependency.type.get()) {
                DependencyType.REQUIRED -> com.modrinth.minotaur.dependencies.DependencyType.REQUIRED
                DependencyType.OPTIONAL -> com.modrinth.minotaur.dependencies.DependencyType.OPTIONAL
                DependencyType.INCOMPATIBLE -> com.modrinth.minotaur.dependencies.DependencyType.INCOMPATIBLE
                DependencyType.EMBEDDED -> com.modrinth.minotaur.dependencies.DependencyType.EMBEDDED
                else -> throw IllegalArgumentException("Could not map DGT dependency type to Minotaur")
            }

            ModDependency(dependencyProjectId, type)
        })
    }

    tasks.register(MODRINTH_TASK_NAME) {
        group = ToolkitConstants.TASK_GROUP
        dependsOn("modrinth")
    }.also { tasks[ALL_PLATFORM_TASK_NAME].dependsOn(it) }
}

fun setupCurseForge(token: String) {
    val projectId = extension.projectIds.curseforge.orNull ?: return
    val task = tasks.register<TaskPublishCurseForge>(CURSEFORGE_TASK_NAME) {
        group = ToolkitConstants.TASK_GROUP

        this.debugMode = extension.debugMode.getOrElse(false)
        this.apiToken = token

        upload(projectId, extension.file) {
            disableVersionDetection()

            this.displayName = extension.releaseName
            // There is no concept of a "version number" on CurseForge
            this.releaseType = extension.versionType.getOrElse(VersionType.RELEASE).value
            this.changelog = extension.changelog.content
            this.changelogType = extension.changelog.type.map(ChangelogExtension.ChangelogType::toString).map(String::lowercase)

            if (extension.jars.isUsingSourcesJar) withAdditionalFile(extension.jars.uploadedSourcesJar)
            if (extension.jars.isUsingJavadocJar) withAdditionalFile(extension.jars.uploadedJavadocJar)

            extension.gameVersions.get().map(MinecraftVersion<*>::toString).forEach(this::addGameVersion)
            extension.loaders.get().map(ModLoader::toString).forEach(this::addModLoader)

            extension.dependencies.forEach { dependency ->
                val dependencyProjectId = dependency.projectId.orNull
                    ?: dependency.curseforge.projectId.orNull
                    ?: throw IllegalStateException("Could not attain CurseForge dependency project ID for ${dependency.name}")

                val type = dependency.type.get()
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

    tasks[ALL_PLATFORM_TASK_NAME].dependsOn(task)
}
