package xyz.unifycraft.gradle.snippets

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.processResources
import xyz.unifycraft.gradle.MCData

plugins {
    java
}

afterEvaluate {
    tasks.processResources {
        val mcData = MCData.fromExisting(project)
        val data = mapOf(
            "version" to project.version,
            "mcversion" to mcData.versionStr,
            "fmcversion" to mcData.version,
            "javaversion" to mcData.javaVersion,
            "file" to mapOf("jarVersion" to project.version.toString().let { if (it[0].isDigit()) it else "0.$it" })
        )

        inputs.property("data", data)
        filesMatching(listOf("mcmod.info", "fabric.mod.json", "mixins.*.json")) {
            expand(data)
        }

        if (!mcData.isFabric) exclude("fabric.mod.json")
        if (!mcData.isModLauncher) exclude("META-INF/mods.toml")
        if (!mcData.isLegacyForge) exclude("mcmod.info")
    }
}