package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.publishing
import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.signing
import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.java
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData
import xyz.deftu.gradle.utils.isMultiversionProject

plugins {
    `maven-publish`
    signing
    java
}

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
        publications {
            create<MavenPublication>("mavenJava") {
                if (modData.present) {
                    artifactId = if (isMultiversionProject()) "${extension.getArtifactName(true)}-${mcData.versionStr}-${mcData.loader.name}" else extension.getArtifactName(true)
                    groupId = modData.group
                    version = modData.version
                } else if (projectData.present) {
                    artifactId = extension.getArtifactName(false)
                    groupId = projectData.group
                    version = projectData.version
                }

                if (pluginManager.hasPlugin("xyz.deftu.gradle.tools.shadow")) {
                    val fatJar by tasks.getting
                    artifact(fatJar) {
                        classifier = null
                    }
                    val sourcesJar by tasks.getting
                    artifact(sourcesJar)

                    pluginManager.withPlugin("java") {
                        val javadocJar = project.tasks.findByName("javadocJar") as Jar?
                        if (javadocJar != null && javadocJar.enabled) {
                            artifact(javadocJar)
                        }
                    }
                } else from(components["java"])
            }
        }

        repositories {
            mavenLocal()

            fun getPublishingUsername(): String? {
                val property = project.findProperty("deftu.publishing.username")
                return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_USERNAME")
            }

            fun getPublishingPassword(): String? {
                val property = project.findProperty("deftu.publishing.password")
                return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_PASSWORD")
            }

            val publishingUsername = getPublishingUsername()
            val publishingPassword = getPublishingPassword()
            if (publishingUsername != null && publishingPassword != null) {
                fun MavenArtifactRepository.applyCredentials() {
                    authentication.create<BasicAuthentication>("basic")
                    credentials {
                        username = publishingUsername
                        password = publishingPassword
                    }
                }

                maven {
                    name = "DeftuReleases"
                    url = uri("https://maven.deftu.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "DeftuSnapshots"
                    url = uri("https://maven.deftu.xyz/snapshots")
                    applyCredentials()
                }
            }
        }
    }

    if (project.hasProperty("signing.password")) {
        signing {
            publishing.publications.forEach(::sign)
        }
    }
}
