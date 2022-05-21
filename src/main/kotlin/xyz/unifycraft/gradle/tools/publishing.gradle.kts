package xyz.unifycraft.gradle.tools

import gradle.kotlin.dsl.accessors._06e55093d2b2796fa8ca19eb1df48cd4.java
import gradle.kotlin.dsl.accessors._1c15fb89b58737430628bcc1daf4c274.publishing
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData

plugins {
    `maven-publish`
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
                    artifact("unishadowJar")
                    artifact("sourcesJar")
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
}
