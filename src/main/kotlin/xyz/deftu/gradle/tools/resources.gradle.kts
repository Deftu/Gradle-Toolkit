package xyz.deftu.gradle.tools

import xyz.deftu.gradle.GameInfo
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData
import xyz.deftu.gradle.tools.minecraft.LoomHelperExtension
import xyz.deftu.gradle.utils.Constants

plugins {
    java
}

afterEvaluate {
    val loomHelperExtension = extensions.findByType<LoomHelperExtension>()
    tasks.processResources {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)
        val projectData = ProjectData.from(project)

        val forgeLoaderVersion: String? = run {
            if (!mcData.present) {
                if (Constants.debug) logger.warn("Forge loader version not provided for ${project.name} because MCData is not present!")
                return@run null
            }

            if (!mcData.isForge) {
                if (Constants.debug) logger.warn("Forge loader version not provided for ${project.name} because Forge is not being used!")
                return@run null
            }

            if (mcData.isLegacyForge) {
                if (Constants.debug) logger.warn("Forge loader version not provided for ${project.name} because legacy Forge is being used!")
                return@run null
            }

            if (loomHelperExtension == null) {
                if (Constants.debug) logger.warn("Forge loader version not provided for ${project.name} because LoomHelperExtension is not present!")
                return@run null
            }

            if (!loomHelperExtension.usingKotlinForForge) {
                if (Constants.debug) logger.warn("Forge loader version not provided for ${project.name} because KotlinForForge is not being used!")
                return@run null
            }

            val version = GameInfo.fetchKotlinForForgeVersion(mcData.version)
            val majorVersion = version.split(".")[0]
            "[$majorVersion,)"
        }

        if (mcData.present) {
            inputs.property("mc_version", mcData.versionStr)
            inputs.property("minor_mc_version", mcData.minorVersionStr)
            inputs.property("format_mc_version", mcData.version)
            inputs.property(
                "java_version",
                if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(JavaVersion.VERSION_17)) "JAVA_17" else "JAVA_16"
            )
        }

        if (modData.present) {
            inputs.property("mod_version", modData.version)
            inputs.property("mod_id", modData.id)
            inputs.property("mod_name", modData.name)
            inputs.property("mod_group", modData.group)
            inputs.property("mod_description", modData.description)
            inputs.property("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
            if (forgeLoaderVersion != null) inputs.property("forge_loader_version", forgeLoaderVersion)
        }

        if (projectData.present) {
            inputs.property("project_version", projectData.version)
            inputs.property("project_group", projectData.group)
            inputs.property("project_name", projectData.name)
            inputs.property("project_description", projectData.description)
        }

        filesMatching(
            listOf(
                "mcmod.info",
                "fabric.mod.json",
                "quilt.mod.json",
                "META-INF/mods.toml",
                "mixins.*.json",
                "*.mixins.json"
            )
        ) {
            expand(mutableMapOf<String, Any>().apply {
                if (mcData.present) {
                    put("mc_version", mcData.versionStr)
                    put("minor_mc_version", mcData.minorVersionStr)
                    put("format_mc_version", mcData.version)
                    put(
                        "java_version",
                        if (mcData.javaVersion.isJava8) "JAVA_8" else if (mcData.javaVersion.isCompatibleWith(
                                JavaVersion.VERSION_16
                            )
                        ) "JAVA_16" else "JAVA_17"
                    )
                }

                if (modData.present) {
                    put("mod_version", modData.version)
                    put("mod_id", modData.id)
                    put("mod_name", modData.name)
                    put("mod_group", modData.group)
                    put("mod_description", modData.description)
                    put("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
                    if (forgeLoaderVersion != null) put("forge_loader_version", forgeLoaderVersion)
                }
            })
        }

        filesMatching(
            listOf(
                "*.txt",
                "*.json",
                "*.properties",
                "*.md",
                "*.yml",
                "*.yaml",
                "*.toml",
                "*.xml",
                "*.info",
                "*.cfg",
                "*.conf",
                "*.config",
                "*.lang"
            )
        ) {
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
