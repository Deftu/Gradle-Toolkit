package xyz.unifycraft.gradle.tools

import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData

plugins {
    `maven-publish`
    java
}

val mcData = MCData.from(project)
val modData = ModData.from(project)

tasks {
    named<JavaPluginExtension>("java") {
        withSourcesJar()
    }

    named<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = modData.name
                groupId = modData.group
                version = modData.version
                if (pluginManager.hasPlugin("xyz.unifycraft.gradle.tools.shadow")) {
                    artifact("unishadowJar")
                    artifact("sourcesJar")
                } else from(components["java"])
            }
        }
    }
}
