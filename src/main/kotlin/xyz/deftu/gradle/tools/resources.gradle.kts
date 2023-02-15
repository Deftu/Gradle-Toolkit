package xyz.deftu.gradle.tools

import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData

plugins {
    java
}

afterEvaluate {
    tasks.processResources {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)
        val projectData = ProjectData.from(project)

        if (mcData.present) {
            inputs.property("mc_version", mcData.versionStr)
            inputs.property("minor_mc_version", mcData.minorVersionStr)
            inputs.property("format_mc_version", mcData.version)
            inputs.property("java_version", if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_16)) "JAVA_16" else "JAVA_17")
        }

        if (modData.present) {
            inputs.property("mod_version", modData.version)
            inputs.property("mod_id", modData.id)
            inputs.property("mod_name", modData.name)
            inputs.property("mod_description", modData.description)
            inputs.property("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
        }

        if (projectData.present) {
            inputs.property("project_version", projectData.version)
            inputs.property("project_group", projectData.group)
            inputs.property("project_name", projectData.name)
            inputs.property("project_description", projectData.description)
        }

        filesMatching(listOf("mcmod.info", "fabric.mod.json", "META-INF/mods.toml", "mixins.*.json", "*.mixins.json")) {
            expand(mutableMapOf<String, Any>().apply {
                if (mcData.present) {
                    put("mc_version", mcData.versionStr)
                    put("minor_mc_version", mcData.minorVersionStr)
                    put("format_mc_version", mcData.version)
                    put("java_version", if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_16)) "JAVA_16" else "JAVA_17")
                }

                if (modData.present) {
                    put("mod_version", modData.version)
                    put("mod_id", modData.id)
                    put("mod_name", modData.name)
                    put("mod_description", modData.description)
                    put("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
                }
            })
        }

        filesMatching(listOf("*")) {
            expand(mutableMapOf<String, Any>().apply {
                if (projectData.present) {
                    put("project_version", projectData.version)
                    put("project_group", projectData.group)
                    put("project_name", projectData.name)
                    put("project_description", projectData.description)
                }
            })
        }

        if (!mcData.isFabric) exclude("fabric.mod.json")
        if (!mcData.isModLauncher) exclude("META-INF/mods.toml")
        if (!mcData.isLegacyForge) exclude("mcmod.info")
    }
}
