package xyz.deftu.gradle.tools

import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData

plugins {
    java
}

afterEvaluate {
    tasks.processResources {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)

        inputs.property("mod_version", modData.version)
        inputs.property("mod_id", modData.id)
        inputs.property("mod_name", modData.name)
        inputs.property("mc_version", mcData.versionStr)
        inputs.property("minor_mc_version", mcData.minorVersionStr)
        inputs.property("format_mc_version", mcData.version)
        inputs.property("java_version", if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_16)) "JAVA_16" else "JAVA_17")
        inputs.property("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
        filesMatching(listOf("mcmod.info", "fabric.mod.json", "META-INF/mods.toml", "mixins.*.json", "*.mixins.json")) {
            expand(mapOf(
                "mod_version" to modData.version,
                "mod_id" to modData.id,
                "mod_name" to modData.name,
                "mc_version" to mcData.versionStr,
                "minor_mc_version" to mcData.minorVersionStr,
                "format_mc_version" to mcData.version,
                "java_version" to if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_16)) "JAVA_16" else "JAVA_17",
                "file" to mapOf("jarVersion" to modData.version.let { if (it[0].isDigit()) it else "0.$it" })
            ))
        }

        if (!mcData.isFabric) exclude("fabric.mod.json")
        if (!mcData.isModLauncher) exclude("META-INF/mods.toml")
        if (!mcData.isLegacyForge) exclude("mcmod.info")
    }
}
