package xyz.unifycraft.gradle.snippets

import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData

plugins {
    java
}

afterEvaluate {
    tasks.processResources {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)
        val data = mapOf(
            "mod_version" to modData.version,
            "mod_id" to modData.id,
            "mod_name" to modData.name,
            "mc_version" to mcData.versionStr,
            "format_mc_version" to mcData.version,
            "java_version" to if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_16)) "JAVA_16" else "JAVA_17",
            "file" to mapOf("jarVersion" to modData.version.toString().let { if (it[0].isDigit()) it else "0.$it" })
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
