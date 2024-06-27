package dev.deftu.gradle.tools.publishing

import dev.deftu.gradle.utils.*
import java.util.*

plugins {
    `maven-publish`
    signing
    java
}

val RELEASES_REPO_NAME = "DeftuReleases"
val SNAPSHOT_REPO_NAME = "DeftuSnapshots"
val INTERNAL_REPO_NAME = "DeftuInternal"

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)

val extension = extensions.create("toolkitMavenPublishing", MavenPublishingExtension::class)

fun MavenPublishingExtension.getArtifactName(
    isMod: Boolean
) = artifactName.getOrElse(if (isMod) modData.name else projectData.name)

pluginManager.withPlugin("java") {
    java {
        withSourcesJar()
    }
}

afterEvaluate {
    publishing {
        if (extension.setupPublication.getOrElse(true)) {
            publications {
                create<MavenPublication>("mavenJava") {
                    if (modData.isPresent) {
                        artifactId = (if (isMultiversionProject()) {
                            "${extension.getArtifactName(true)}-${mcData.version}-${mcData.loader.friendlyString}"
                        } else extension.getArtifactName(true))
                        if (extension.forceLowercase.getOrElse(false)) {
                            artifactId = artifactId.lowercase(Locale.US)
                        }

                        groupId = modData.group
                        version = modData.version
                    } else if (projectData.isPresent) {
                        artifactId = extension.getArtifactName(false)
                        groupId = projectData.group
                        version = projectData.version
                    }

                    if (pluginManager.hasPlugin("dev.deftu.gradle.tools.shadow")) {
                        val fatJar by tasks.getting
                        artifact(fatJar) {
                            classifier = null
                        }

                        artifact(getSourcesJarTask())

                        pluginManager.withPlugin("java") {
                            val javadocJar = project.tasks.findByName("javadocJar") as Jar?
                            if (javadocJar != null && javadocJar.enabled) {
                                artifact(javadocJar)
                            }
                        }
                    } else from(components["java"])
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

            rootProject.tasks.create(name) {
                group = "publishing"
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
