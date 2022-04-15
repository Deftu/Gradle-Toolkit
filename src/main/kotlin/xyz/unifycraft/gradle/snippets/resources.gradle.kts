package xyz.unifycraft.gradle.snippets

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.processResources
import xyz.unifycraft.gradle.MCData

plugins {
    java
}

afterEvaluate {
    tasks.processResources {
        val mcData: MCData = MCData.fromExisting(project)

        filesMatching("mcmod.info") {
            expand(
                "version" to project.version,
                "mcversion" to mcData.versionStr
            )
        }

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "mcversion" to mcData.versionStr
            )
        }

        filesMatching("mixins.*.json") {
            expand(
                "version" to project.version,
                "mcversion" to mcData.versionStr,
                "fmcversion" to mcData.version,
                "javaversion" to mcData.javaVersion
            )
        }
    }
}