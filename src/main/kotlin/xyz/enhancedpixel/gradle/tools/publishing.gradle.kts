package xyz.enhancedpixel.gradle.tools

import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.publishing
import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.signing
import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.java
import xyz.enhancedpixel.gradle.MCData
import xyz.enhancedpixel.gradle.ModData
import xyz.enhancedpixel.gradle.utils.isMultiversionProject

plugins {
    `maven-publish`
    signing
    java
}

val mcData = MCData.from(project)
val modData = ModData.from(project)

pluginManager.withPlugin("java") {
    java {
        withSourcesJar()
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = if (isMultiversionProject()) "${modData.name}-${mcData.versionStr}" else modData.name
                groupId = modData.group
                version = modData.version
                if (pluginManager.hasPlugin("xyz.enhancedpixel.gradle.tools.shadow")) {
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
            if (project.hasProperty("enhancedpixel.publishing.username") && project.hasProperty("enhancedpixel.publishing.password")) {
                fun MavenArtifactRepository.applyCredentials() {
                    authentication.create<BasicAuthentication>("basic")
                    credentials {
                        username = property("enhancedpixel.publishing.username")?.toString()
                        password = property("enhancedpixel.publishing.password")?.toString()
                    }
                }

                maven {
                    name = "EnhancedPixelReleases"
                    url = uri("https://maven.enhancedpixel.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "EnhancedPixelSnapshots"
                    url = uri("https://maven.enhancedpixel.xyz/snapshots")
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
