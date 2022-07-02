package xyz.unifycraft.gradle.tools

import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.publishing
import gradle.kotlin.dsl.accessors._89edf7476531f98eff64d602eb448354.signing
import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.java
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData

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
                artifactId = modData.name
                groupId = modData.group
                version = modData.version
                if (pluginManager.hasPlugin("xyz.unifycraft.gradle.tools.shadow")) {
                    val unishadowJar by tasks.getting
                    artifact(unishadowJar) {
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
            if (project.hasProperty("unifycraft.publishing.username") && project.hasProperty("unifycraft.publishing.password")) {
                fun MavenArtifactRepository.applyCredentials() {
                    credentials {
                        username = property("unifycraft.publishing.username")?.toString()
                        password = property("unifycraft.publishing.password")?.toString()
                    }
                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                }

                maven {
                    name = "UnifyCraftRelease"
                    url = uri("https://maven.unifycraft.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "UnifyCraftSnapshots"
                    url = uri("https://maven.unifycraft.xyz/snapshots")
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
