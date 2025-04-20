package dev.deftu.gradle.tools.publishing

import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*
import gradle.kotlin.dsl.accessors._76a779107637b25b34866585d88a55c4.publishing
import gradle.kotlin.dsl.accessors._76a779107637b25b34866585d88a55c4.signing
import java.util.*

plugins {
    `maven-publish`
    signing
}

val RELEASES_REPO_NAME = "DeftuReleases"
val SNAPSHOT_REPO_NAME = "DeftuSnapshots"
val INTERNAL_REPO_NAME = "DeftuInternal"
val INTERNAL_EXPOSED_REPO_NAME = "DeftuInternalExposed"

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)

val extension = extensions.create("toolkitMavenPublishing", MavenPublishingExtension::class)

fun MavenPublishingExtension.getArtifactName(
    isMod: Boolean
) = artifactName.getOrElse(if (isMod) modData.name else projectData.name)

afterEvaluate {
    publishing {
        pluginManager.withPlugin("java") {
            if (extension.setupPublication.getOrElse(true)) {
                publications {
                    create<MavenPublication>("mavenJava") {
                        if (modData.isPresent) {
                            artifactId = buildString {
                                val artifactName = extension.getArtifactName(true)
                                if (artifactName.isNotEmpty()) {
                                    append(artifactName).append("-")
                                }

                                if (isMultiversionProject()) {
                                    append(mcData.version).append("-").append(mcData.loader.friendlyString)
                                }
                            }.ifEmpty(project::getName)
                            groupId = modData.group
                            version = modData.version
                        } else if (projectData.isPresent) {
                            artifactId = extension.getArtifactName(false)
                            groupId = projectData.group
                            version = projectData.version
                        }

                        if (extension.forceLowercase.getOrElse(false)) {
                            artifactId = artifactId.lowercase(Locale.US)
                        }

                        from(components["java"])
                    }
                }
            }
        }

        if (extension.setupRepositories.getOrElse(true)) {
            repositories {
                mavenLocal()

                val publishingUsername = getDgtPublishingUsername()
                val publishingPassword = getDgtPublishingPassword()
                if (publishingUsername != null && publishingPassword != null) {
                    maven {
                        name = RELEASES_REPO_NAME
                        url = uri("https://maven.deftu.dev/releases")
                        applyBasicCredentials(publishingUsername, publishingPassword)
                    }

                    maven {
                        name = SNAPSHOT_REPO_NAME
                        url = uri("https://maven.deftu.dev/snapshots")
                        applyBasicCredentials(publishingUsername, publishingPassword)
                    }

                    maven {
                        name = INTERNAL_REPO_NAME
                        url = uri("https://maven.deftu.dev/internal")
                        applyBasicCredentials(publishingUsername, publishingPassword)
                    }

                    maven {
                        name = INTERNAL_EXPOSED_REPO_NAME
                        url = uri("https://maven.deftu.dev/internal-exposed")
                        applyBasicCredentials(publishingUsername, publishingPassword)
                    }
                }
            }
        }
    }

    if (project.hasProperty("signing.password")) {
        signing {
            publishing.publications.forEach(::sign)
        }
    }

    if (isMultiversionProject()) {
        publishing.repositories.names.map {
            "${it}Repository"
        }.forEach { repo ->
            val name = "publishAllModVersionsTo$repo"
            if (rootProject.tasks.any { task ->
                return@any task.name == name
            }) return@forEach

            rootProject.tasks.register(name) {
                group = ToolkitConstants.TASK_GROUP
                description = "Publish all versions of the mod to $repo"

                project.subprojects.map(Project::getName).filter { name ->
                    ModLoader.values().any { loader ->
                        name.endsWith("-${loader.name}", true)
                    }
                }.forEach { version ->
                    dependsOn(":$version:publishAllPublicationsTo$repo")
                }
            }
        }
    }
}
